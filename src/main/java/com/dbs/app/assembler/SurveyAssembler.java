package com.dbs.app.assembler;

import com.dbs.app.model.Group;
import com.dbs.app.model.Project;
import com.dbs.app.model.Squad;
import com.dbs.app.model.Survey;
import com.dbs.app.model.SurveyLink;
import com.dbs.app.model.Tower;
import com.dbs.app.resource.GroupResource;
import com.dbs.app.resource.ProjectResource;
import com.dbs.app.resource.SquadResource;
import com.dbs.app.resource.SurveyResource;
import com.dbs.app.resource.TowerResource;
import com.dbs.app.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SurveyAssembler {

    public SurveyResource assemble(Survey survey) {
        String linkId = Optional.ofNullable(survey)
            .map(Survey::getSurveyLink)
            .map(SurveyLink::getId)
            .orElse(null);
        SurveyResource resource = new SurveyResource();
        resource.setRespondents(survey.getRespondents());
        resource.setId(survey.getId());
        resource.setLinkId(linkId);
        resource.setUpdatedAt(DateUtils.formatDate(survey.getUpdatedAt()));
        resource.setCreatedAt(DateUtils.formatDate(survey.getCreateAt()));
        resource.setExpiresOn(DateUtils.formatDate(survey.getExpiresOn()));
        resource.setCreatedBy(survey.getCreateBy());
        assembleSquad(survey, resource);
        assembleProject(survey, resource);
        assembleTower(survey, resource);
        assembleGroup(survey, resource);
        return resource;
    }

    public SurveyResource assemblePartialData(Survey survey) {
        String linkId = Optional.ofNullable(survey)
            .map(Survey::getSurveyLink)
            .map(SurveyLink::getId)
            .orElse(null);
        SurveyResource resource = new SurveyResource();
        resource.setRespondents(survey.getRespondents());
        resource.setId(survey.getId());
        resource.setLinkId(linkId);
        resource.setUpdatedAt(DateUtils.formatDate(survey.getUpdatedAt()));
        resource.setCreatedAt(DateUtils.formatDate(survey.getCreateAt()));
        resource.setExpiresOn(DateUtils.formatDate(survey.getExpiresOn()));
        assembleSquad(survey, resource);
        assembleProject(survey, resource);
        return resource;
    }

    private void assembleSquad(Survey survey, SurveyResource resource) {
        Squad squad = survey.getSquad();
        SquadResource squadResource = new SquadResource();
        squadResource.setId(squad.getId());
        squadResource.setName(squad.getName());
        resource.setSquad(squadResource);
    }

    private void assembleProject(Survey survey, SurveyResource resource) {
        Project project = survey.getSquad().getProject();
        ProjectResource projectResource = new ProjectResource();
        projectResource.setId(project.getId());
        projectResource.setName(project.getName());
        resource.setProject(projectResource);
    }

    private void assembleTower(Survey survey, SurveyResource resource) {
        Tower tower = survey.getSquad().getProject().getTower();
        TowerResource towerResource = new TowerResource();
        towerResource.setId(tower.getId());
        towerResource.setName(tower.getName());
        resource.setTower(towerResource);
    }

    private void assembleGroup(Survey survey, SurveyResource resource) {
        Group group = survey.getSquad().getProject().getTower().getGroup();
        GroupResource groupResource = new GroupResource();
        groupResource.setId(group.getId());
        groupResource.setName(group.getName());
        resource.setGroup(groupResource);
    }
}
