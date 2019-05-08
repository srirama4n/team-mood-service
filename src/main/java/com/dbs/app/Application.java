package com.dbs.app;

import com.dbs.app.assembler.SurveySummaryAssembler;
import com.dbs.app.repository.SurveyElsRepository;
import com.dbs.app.repository.SurveyRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = {"com.dbs.app.repository"})
@AllArgsConstructor
public class Application {

    private SurveyRepository surveyRepository;
    private SurveyElsRepository surveyElsRepository;
    private SurveySummaryAssembler surveySummaryAssembler;
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
