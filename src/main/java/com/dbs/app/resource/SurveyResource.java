package com.dbs.app.resource;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.List;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(indexName = "team_mood", type = "survey")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyResource extends BaseResource {

    @Id
    String id;
    String createdAt;
    String updatedAt;
    String expiresOn;
    String createdBy;
    @Transient
    String key;
    Integer submissions;
    Integer minTemperature;
    Integer maxTemperature;
    Double avgTemperature;
    GroupResource group;
    TowerResource tower;
    ProjectResource project;
    SquadResource squad;
    List<TemperatureResource> temperatures;
    String linkId;
    Integer respondents;
}
