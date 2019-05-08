package com.dbs.app.handler;

import com.dbs.app.exception.TeamTempException;
import com.dbs.app.model.Tower;
import com.dbs.app.repository.TowerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class TowersHandler {

    private TowerRepository towerRepository;

    public Mono<ServerResponse> findTowersByGroup(ServerRequest serverRequest) {
        String groupName = serverRequest.queryParam("groupId")
            .orElseThrow(() -> new TeamTempException("Group Id is required"));
        List<Tower> towers = towerRepository.findTowersByGroup_Id(groupName);
        return ServerResponse.ok().body(BodyInserters.fromObject(towers));
    }
}
