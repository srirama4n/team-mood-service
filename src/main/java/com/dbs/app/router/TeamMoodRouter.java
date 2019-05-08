package com.dbs.app.router;

import com.dbs.app.handler.GroupsHandler;
import com.dbs.app.handler.ProjectsHandler;
import com.dbs.app.handler.SquadsHandler;
import com.dbs.app.handler.SurveyHandler;
import com.dbs.app.handler.TowersHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class TeamMoodRouter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeamMoodRouter.class);
    @Bean
    public RouterFunction<ServerResponse> getGroups(GroupsHandler groupsHandler) {
        LOGGER.info("Inside get groups");
        return RouterFunctions.route(
            RequestPredicates.GET("/groups").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            groupsHandler::findAllGroups
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getTowers(TowersHandler towersHandler) {
        LOGGER.info("Inside get towers");
        return RouterFunctions.route(
            RequestPredicates.GET("/towers").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            towersHandler::findTowersByGroup
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getProjects(ProjectsHandler projectsHandler) {
        LOGGER.info("Inside get projects");
        return RouterFunctions.route(
            RequestPredicates.GET("/projects").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            projectsHandler::findProjectsByTower
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getSquads(SquadsHandler squadsHandler) {
        LOGGER.info("Inside get squads");
        return RouterFunctions.route(
            RequestPredicates.GET("/squads").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            squadsHandler::findSquadsByProject
        );
    }

    @Bean
    public RouterFunction<ServerResponse> createSurvey(SurveyHandler surveyHandler) {
        LOGGER.info("Inside create survey");
        return RouterFunctions.route(
            RequestPredicates.POST("/surveys").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            surveyHandler::createSurvey
        );
    }

    @Bean
    public RouterFunction<ServerResponse> renewSurvey(SurveyHandler surveyHandler) {
        LOGGER.info("Inside renew survey");
        return RouterFunctions.route(
                RequestPredicates.POST("/surveys/renew").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                surveyHandler::renewSurvey
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getSurvey(SurveyHandler surveyHandler) {
        LOGGER.info("Inside get survey");
        return RouterFunctions.route(
            RequestPredicates.GET("/surveys").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            surveyHandler::getSurvey
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getSurveyByLink(SurveyHandler surveyHandler) {
        LOGGER.info("Inside get survey by link");
        return RouterFunctions.route(
                RequestPredicates.GET("/surveys/link").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                surveyHandler::getSurveyByLink
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getSurveys(SurveyHandler surveyHandler) {
        LOGGER.info("Inside get all surveys");
        return RouterFunctions.route(
            RequestPredicates.GET("/surveys/all").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            surveyHandler::getSurveys
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getSurveysBySquadName(SurveyHandler surveyHandler) {
        LOGGER.info("Inside get survey by squad name");
        return RouterFunctions.route(
                RequestPredicates.GET("/surveys/squad").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                surveyHandler::getSurveysBySquadName
        );
    }


    @Bean
    public RouterFunction<ServerResponse> updateSurvey(SurveyHandler surveyHandler) {
        LOGGER.info("Inside update survey");
        return RouterFunctions.route(
            RequestPredicates.PUT("/surveys").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            surveyHandler::updateSurvey
        );
    }

    @Bean
    public RouterFunction<ServerResponse> surveySummary(SurveyHandler surveyHandler) {
        LOGGER.info("Inside survey summary");
        return RouterFunctions.route(
            RequestPredicates.GET("/surveys/{surveyId}/summary").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            surveyHandler::getSurveySummary
        );
    }

    @Bean
    public RouterFunction<ServerResponse> deleteSurvey(SurveyHandler surveyHandler) {
        LOGGER.info("Inside delete survey");
        return RouterFunctions.route(
            RequestPredicates.PUT("/surveys/delete").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            surveyHandler::deleteSurvey
        );
    }

    @Bean
    public RouterFunction<ServerResponse> updateSurveyDetail(SurveyHandler surveyHandler) {
        LOGGER.info("Inside update survey detail");
        return RouterFunctions.route(
            RequestPredicates.PUT("/surveys/update").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
            surveyHandler::updateSurveyDetails
        );
    }
    @Bean
    public RouterFunction<ServerResponse> getTemparatureDetail(SurveyHandler surveyHandler) {
        LOGGER.info("Inside get temparature details");
        return RouterFunctions.route(
                RequestPredicates.POST("/reports").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                surveyHandler::getTemparatureDetail
        );
    }
}
