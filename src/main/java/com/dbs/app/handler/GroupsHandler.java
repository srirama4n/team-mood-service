package com.dbs.app.handler;

import com.dbs.app.model.Group;
import com.dbs.app.repository.GroupRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@AllArgsConstructor
public class GroupsHandler {

    private GroupRepository groupRepository;

    public Mono<ServerResponse> findAllGroups(ServerRequest serverRequest) {
        List<Group> groups = groupRepository.findAll(Sort.by("name"));
        return ServerResponse.ok().body(BodyInserters.fromObject(groups));
    }
}
