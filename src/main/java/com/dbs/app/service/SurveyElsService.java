package com.dbs.app.service;

import com.dbs.app.assembler.SurveyAssembler;
import com.dbs.app.repository.SurveyElsRepository;
import com.dbs.app.resource.SurveyResource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SurveyElsService {

    private SurveyElsRepository surveyElsRepository;
    private SurveyAssembler surveyAssembler;
    public void indexSurveyAsync(SurveyResource resource) {
        CompletableFuture.supplyAsync(() -> surveyElsRepository.save(resource))
            .handle((res, throwable) -> {
                if (null != res) {
                    log.info("Indexed the results for the survey with id " + resource.getId());
                }
                if (throwable != null) {
                    log.info("Failed to index the results for the survey with id " + resource.getId());
                }
                return res;
            });
    }

    public void deleteSurveyAsync(String id) {
        CompletableFuture.runAsync(() -> surveyElsRepository.deleteById(id))
                .handle((res, throwable) -> {
                    if (null != res) {
                        log.info("Delete the index for the survey with id " + id);
                    }
                    if (throwable != null) {
                        log.info("Failed to delete index for the survey with id " + id);
                    }
                    return res;
                });
    }
    public List<SurveyResource> getAllTemparatureData(String groupName) {
       return surveyElsRepository.findSurveyResourceByGroup_Name(groupName);
    }
}
