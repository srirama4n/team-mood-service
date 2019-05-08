package com.dbs.app.repository;

import com.dbs.app.model.SurveyLink;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyLinkRepository extends CrudRepository<SurveyLink, String> {

    SurveyLink findByGroupIdAndTowerIdAndProjectIdAndSquadId(String groupId, String towerId, String projectId, String squadId);
}
