package com.dbs.app.handler;

import com.dbs.app.exception.TeamTempException;
import com.dbs.app.resource.GroupResource;
import com.dbs.app.resource.SurveyResource;
import com.dbs.app.resource.TemperatureResource;
import com.dbs.app.service.SurveyElsService;
import com.dbs.app.service.SurveyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SurveyHandler {

    private SurveyService surveyService;

    private SurveyElsService surveyElsService;

    public Mono<ServerResponse> createSurvey(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SurveyResource.class)
                .map(surveyService::createSurvey)
                .flatMap(survey -> ServerResponse.status(survey.getStatus()).body(BodyInserters.fromObject(survey)));
    }

    public Mono<ServerResponse> getTemparatureDetail(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(GroupResource.class)
                .map(GroupResource::getName)
                .map(groupName -> surveyElsService.getAllTemparatureData(groupName))
                .flatMap(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)));
    }

    public Mono<ServerResponse> renewSurvey(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SurveyResource.class)
                .map(surveyResource -> surveyService.renewSurvey(surveyResource))
                .flatMap(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)));
    }

    public Mono<ServerResponse> getSurvey(ServerRequest serverRequest) {
        return serverRequest.queryParam("surveyId")
                .map(surveyId -> surveyService.getSurvey(surveyId))
                .map(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)))
                .orElse(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteSurvey(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SurveyResource.class)
                .map(resource -> surveyService.deleteSurvey(resource))
                .flatMap(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)));
    }

    public Mono<ServerResponse> getSurveyByLink(ServerRequest serverRequest) {
        return serverRequest.queryParam("linkId")
                .map(linkId -> surveyService.getSurveyByLink(linkId))
                .map(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)))
                .orElse(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getSurveys(ServerRequest serverRequest) {
        List<SurveyResource> surveys = surveyService.getSurveys();
        return ServerResponse.ok().body(BodyInserters.fromObject(surveys));
    }

    public Mono<ServerResponse> getSurveysBySquadName(ServerRequest serverRequest) {

        String squadName = serverRequest.queryParam("name").orElseThrow(() -> new TeamTempException("Squad name is required"));
        String projectName = serverRequest.queryParam("projectName").orElseThrow(() -> new TeamTempException("Project name is required"));
        List<SurveyResource> surveys = surveyService.getSurveysBySquadName(squadName);
        List<SurveyResource> surveyProjectName = new ArrayList<>();
        if (surveys.size() > 0)
            surveyProjectName = surveys.stream()
                    .filter(survey -> projectName.equalsIgnoreCase(survey.getProject().getName()))
                    .collect(Collectors.toList());
        return ServerResponse.ok().body(BodyInserters.fromObject(surveyProjectName));
    }

    public Mono<ServerResponse> updateSurvey(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(TemperatureResource.class)
                .map(surveyService::updateSurvey)
                .flatMap(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)));
    }

    public Mono<ServerResponse> updateSurveyDetails(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SurveyResource.class)
                .map(surveyService::updateSurveyDetail)
                .flatMap(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)));
    }

    public Mono<ServerResponse> getSurveySummary(ServerRequest serverRequest) {
        return Optional.of(serverRequest.pathVariable("surveyId"))
                .map(surveyId -> surveyService.getSurveySummary(surveyId))
                .map(surveyResource -> ServerResponse.ok().body(BodyInserters.fromObject(surveyResource)))
                .orElse(ServerResponse.notFound().build());
    }
}
