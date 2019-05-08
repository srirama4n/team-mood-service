package com.dbs.app.repository;

import com.dbs.app.resource.SurveyResource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyElsRepository extends ElasticsearchRepository<SurveyResource, String> {

    List<SurveyResource> findSurveyResourceBySquad_Id(String id);

    List<SurveyResource> findSurveyResourceByGroup_Name(String id);
}
