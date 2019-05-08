package com.dbs.app.handler;

import com.dbs.app.exception.TeamTempException;
import com.dbs.app.model.Squad;
import com.dbs.app.repository.SquadRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class SquadsHandler {

    private SquadRepository squadRepository;

    public Mono<ServerResponse> findSquadsByProject(ServerRequest serverRequest){
        String projectName = serverRequest.queryParam("projectId")
            .orElseThrow(() -> new TeamTempException("Project Id is required"));
        List<Squad> squads = squadRepository.findSquadsByProject_Id(projectName);
        return ServerResponse.ok().body(BodyInserters.fromObject(squads));
    }
}
