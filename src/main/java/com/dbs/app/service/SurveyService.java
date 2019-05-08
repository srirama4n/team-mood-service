package com.dbs.app.service;

import com.dbs.app.assembler.SurveyAssembler;
import com.dbs.app.assembler.SurveySummaryAssembler;
import com.dbs.app.model.*;
import com.dbs.app.repository.*;
import com.dbs.app.resource.SurveyResource;
import com.dbs.app.resource.TemperatureResource;
import com.dbs.app.utils.DateUtils;
import lombok.AllArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
@Transactional
public class SurveyService {

    private GroupRepository groupRepository;
    private TowerRepository towerRepository;
    private ProjectRepository projectRepository;
    private SquadRepository squadRepository;
    private SurveyRepository surveyRepository;
    private TemperatureRepository temperatureRepository;
    private SurveyLinkRepository surveyLinkRepository;

    private SurveyElsService surveyElsService;

    private SurveyAssembler surveyAssembler;
    private SurveySummaryAssembler surveySummaryAssembler;

    private static Survey buildSurvey(Survey survey) {
        Survey newSurvey = new Survey();
        DateTime previousSurveyStartDate = new DateTime(survey.getCreateAt());
        DateTime previousSurveyExpiryDate = new DateTime(survey.getExpiresOn());
        Integer previousSurveyDuration = Days.daysBetween(previousSurveyStartDate, previousSurveyExpiryDate).getDays();

        newSurvey.setCreateAt(previousSurveyExpiryDate.plusDays(1).toDate());
        newSurvey.setExpiresOn(previousSurveyExpiryDate.plusDays(1 + previousSurveyDuration).toDate());
        newSurvey.setUpdatedAt(newSurvey.getCreateAt());
        newSurvey.setCreateBy(survey.getCreateBy());
        newSurvey.setSurveyKey(survey.getSurveyKey());
        newSurvey.setSurveyStatus(survey.getSurveyStatus());

        newSurvey.setCreateBy(survey.getCreateBy());
        newSurvey.setSquad(survey.getSquad());
        newSurvey.setRespondents(survey.getRespondents());
        return newSurvey;
    }

    public SurveyResource createSurvey(SurveyResource resource) {
        Group group = findGroupOrCreateNew(resource);
        Tower tower = findTowerOrCreateNew(resource, group);
        Project project = findProjectOrCreateNew(resource, tower);
        Squad squad = findSquadOrCreateNew(resource, project);
        if (surveyLinkNotExists(group, tower, project, squad)) {
            Survey survey = createNewSurvey(resource, squad);
            String surveyLinkId = createOrUpdateSurveyLink(survey, group, tower, project, squad);
            return assembleSurvey(survey, surveyLinkId);
        } else {
            resource = new SurveyResource();
            resource.setStatus(CONFLICT.value());
            resource.setMessage("Survey already exists, please renew it.");
            return resource;
        }
    }

    public SurveyResource deleteSurvey(SurveyResource resource) {
        return Optional.ofNullable(resource)
            .filter(s -> surveyRepository.existsById(s.getId()))
            .map(this::findValidSurvey)
            .map(Survey::getId)
            .map(id -> {
                SurveyResource response = new SurveyResource();
                surveyRepository.deleteById(id);
                surveyElsService.deleteSurveyAsync(id);
                response.setStatus(OK.value());
                response.setMessage("Survey is deleted");
                return response;
            })
            .orElseGet(() -> {
                SurveyResource response = new SurveyResource();
                response.setStatus(NON_AUTHORITATIVE_INFORMATION.value());
                response.setMessage("Survey is not authenticated");
                return response;
            });
    }

    public SurveyResource updateSurveyDetail(SurveyResource resource) {
        return Optional.ofNullable(resource)
            .filter(s -> surveyRepository.existsById(s.getId()))
            .map(s -> {
                final Survey validSurvey = findValidSurvey(resource);
                if (validSurvey.getId() != null) {
                    validSurvey.setCreateAt(DateUtils.parseDate(resource.getCreatedAt()));
                    validSurvey.setExpiresOn(DateUtils.parseDate(resource.getExpiresOn()));
                    validSurvey.setRespondents(resource.getRespondents());
                    Survey survey = surveyRepository.save(validSurvey);
                    createOrUpdateSurveyLink(survey);
                    SurveyResource response = new SurveyResource();
                    response.setStatus(OK.value());
                    response.setMessage("Survey updated successfully");
                    return response;
                } else {
                    SurveyResource response = new SurveyResource();
                    response.setStatus(NON_AUTHORITATIVE_INFORMATION.value());
                    response.setMessage("Survey is not authenticated");
                    return response;
                }
            })
            .orElseGet(() -> {
                SurveyResource response = new SurveyResource();
                response.setStatus(NO_CONTENT.value());
                response.setMessage("Survey doesn't exits");
                return response;
            });
    }

    private boolean surveyLinkNotExists(Group group, Tower tower,
                                        Project project, Squad squad) {
        SurveyLink existingSurveyLink = surveyLinkRepository
            .findByGroupIdAndTowerIdAndProjectIdAndSquadId(group.getId(), tower.getId(), project.getId(), squad.getId());
        return existingSurveyLink == null;
    }

    private String createOrUpdateSurveyLink(Survey survey, Group group, Tower tower,
                                            Project project, Squad squad) {
        SurveyLink surveyLink = new SurveyLink();
        surveyLink.setGroup(group);
        surveyLink.setTower(tower);
        surveyLink.setProject(project);
        surveyLink.setSquad(squad);
        surveyLink.setSurvey(survey);
        SurveyLink existingSurveyLink = surveyLinkRepository
            .findByGroupIdAndTowerIdAndProjectIdAndSquadId(group.getId(), tower.getId(), project.getId(), squad.getId());
        SurveyLink savedEntity;
        if (existingSurveyLink != null) {
            existingSurveyLink.setSurvey(survey);
            savedEntity = surveyLinkRepository.save(existingSurveyLink);
        } else {
            savedEntity = surveyLinkRepository.save(surveyLink);
        }

        return savedEntity.getId();
    }

    private String createOrUpdateSurveyLink(Survey survey) {
        Optional<Squad> squad = squadRepository.findById(survey.getSquad().getId());
        Optional<Project> project = projectRepository.findById(squad.get().getProject().getId());
        Optional<Tower> tower = towerRepository.findById(project.get().getTower().getId());
        Optional<Group> group = groupRepository.findById(tower.get().getGroup().getId());
        return createOrUpdateSurveyLink(survey, group.get(), tower.get(), project.get(), squad.get());
    }

    public TemperatureResource updateSurvey(TemperatureResource resource) {
        Temperature temperature = Optional.ofNullable(resource.getId())
            .flatMap(temperatureRepository::findById)
            .orElseGet(Temperature::new);

        return surveyRepository.findById(resource.getSurveyId())
            .map(survey -> saveTemperature(resource, temperature, survey))
            .map(updatedTemperature -> {
                SurveyResource surveyResource = getSurveySummary(updatedTemperature.getSurvey().getId());
                surveyElsService.indexSurveyAsync(surveyResource);
                TemperatureResource temperatureResource = new TemperatureResource();
                temperatureResource.setId(updatedTemperature.getId());
                temperatureResource.setRating(updatedTemperature.getRating());
                temperatureResource.setComment(updatedTemperature.getComment());
                return temperatureResource;
            }).orElseGet(TemperatureResource::new);
    }

    public SurveyResource getSurvey(String surveyId) {
        return surveyRepository.findById(surveyId)
            .map(surveyAssembler::assemble)
            .orElseGet(SurveyResource::new);
    }

    public SurveyResource getSurveyByLink(String linkId) {
        return surveyLinkRepository.findById(linkId)
            .map(SurveyLink::getSurvey)
            .map(surveyAssembler::assemble)
            .orElseGet(SurveyResource::new);
    }

    public List<SurveyResource> getSurveys() {
        return surveyRepository.findAll(Sort.by("createAt")
                                            .descending())
            .stream()
            .map(surveyAssembler::assemblePartialData)
            .collect(Collectors.toList());
    }

    public List<SurveyResource> getSurveysBySquadName(String squadName) {
        return surveyRepository.findAllBySquadNameContainsOrderByExpiresOnDesc(squadName)
            .stream()
            .map(surveyAssembler::assemblePartialData)
            .collect(Collectors.toList());
    }

    public SurveyResource getSurveySummary(String surveyId) {
        return surveyRepository.findById(surveyId)
            .map(surveySummaryAssembler::assemble)
            .orElseGet(SurveyResource::new);
    }

    private Survey createNewSurvey(SurveyResource resource, Squad squad) {
        Survey survey = new Survey();
        survey.setSquad(squad);
        survey.setCreateBy(resource.getCreatedBy());
        survey.setSurveyKey(resource.getKey());
        survey.setSurveyStatus("ACTIVE");
        survey.setExpiresOn(DateUtils.atEndOfDay(resource.getExpiresOn()));
        survey.setRespondents(resource.getRespondents());
        return surveyRepository.save(survey);
    }

    private Temperature saveTemperature(TemperatureResource resource, Temperature temperature, Survey survey) {
        temperature.setId(resource.getId());
        temperature.setComment(resource.getComment());
        temperature.setRating(resource.getRating());
        temperature.setSurvey(survey);
        survey.getTemperature().add(temperature);
        surveyRepository.save(survey);
        return temperature;
    }

    private SurveyResource assembleSurvey(Survey survey, String surveyLinkId) {
        SurveyResource surveyResource = new SurveyResource();
        surveyResource.setId(survey.getId());
        surveyResource.setLinkId(surveyLinkId);
        surveyResource.setExpiresOn(DateUtils.formatDate(survey.getExpiresOn()));
        surveyResource.setCreatedAt(DateUtils.formatDate(survey.getCreateAt()));
        return surveyResource;
    }

    private Group findGroupOrCreateNew(SurveyResource resource) {
        return Optional.ofNullable(resource.getGroup().getId())
            .map(id -> groupRepository.getOne(id))
            .orElseGet(() -> {
                Group group = new Group();
                group.setName(resource.getGroup().getName());
                groupRepository.save(group);
                return group;
            });
    }

    private Tower findTowerOrCreateNew(SurveyResource resource, Group group) {
        return Optional.ofNullable(resource.getTower().getId())
            .map(id -> towerRepository.getOne(id))
            .orElseGet(() -> {
                Tower tower = new Tower();
                tower.setName(resource.getTower().getName());
                tower.setGroup(group);
                towerRepository.save(tower);
                return tower;
            });
    }

    private Project findProjectOrCreateNew(SurveyResource resource, Tower tower) {
        return Optional.ofNullable(resource.getProject().getId())
            .map(id -> projectRepository.getOne(id))
            .orElseGet(() -> {
                Project project = new Project();
                project.setName(resource.getProject().getName());
                project.setTower(tower);
                projectRepository.save(project);
                return project;
            });
    }

    private Squad findSquadOrCreateNew(SurveyResource resource, Project project) {
        return Optional.ofNullable(resource.getSquad().getId())
            .map(id -> squadRepository.getOne(id))
            .orElseGet(() -> {
                Squad squad = new Squad();
                squad.setName(resource.getSquad().getName());
                squad.setProject(project);
                squadRepository.save(squad);
                return squad;
            });
    }

    public SurveyResource renewSurvey(SurveyResource resource) {
        return Optional.ofNullable(resource)
                .filter(s -> surveyRepository.existsById(s.getId()))
                .map(r ->{
                    final Survey validSurvey = findValidSurvey(resource);
                    if (validSurvey.getId() != null) {
                        Survey newSurvey = this.buildSurvey(validSurvey);
                        Survey survey = surveyRepository.save(newSurvey);
                        String surveyLinkId = createOrUpdateSurveyLink(survey);
                        SurveyResource surveyResource = assembleSurvey(survey, surveyLinkId);
                        surveyResource.setMessage("Survey renew successfully.");
                        surveyResource.setStatus(OK.value());
                        return surveyResource;
                    }else {
                        SurveyResource response = new SurveyResource();
                        response.setStatus(NON_AUTHORITATIVE_INFORMATION.value());
                        response.setMessage("Survey is not authenticated");
                        return response;
                    }
                })
                .orElseGet(() -> {
                    SurveyResource response = new SurveyResource();
                    response.setStatus(NON_AUTHORITATIVE_INFORMATION.value());
                    response.setMessage("Survey is not authenticated");
                    return response;
                });
    }

    private Survey findValidSurvey(SurveyResource resource) {
        return surveyRepository.findById(resource.getId())
            .filter(survey -> survey.getCreateBy().equalsIgnoreCase(resource.getCreatedBy()))
            .filter(survey -> survey.getSurveyKey().equals(resource.getKey()))
            .orElseGet(Survey::new);
    }
}
