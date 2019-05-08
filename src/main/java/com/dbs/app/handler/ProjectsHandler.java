package com.dbs.app.handler;

import com.dbs.app.exception.TeamTempException;
import com.dbs.app.model.Project;
import com.dbs.app.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class ProjectsHandler {

    private ProjectRepository projectRepository;

    public Mono<ServerResponse> findProjectsByTower(ServerRequest serverRequest) {
        String towerName = serverRequest.queryParam("towerId")
            .orElseThrow(() -> new TeamTempException("Tower Id is required"));
        List<Project> projects = projectRepository.findProjectsByTower_Id(towerName);
        return ServerResponse.ok().body(BodyInserters.fromObject(projects));
    }
}
