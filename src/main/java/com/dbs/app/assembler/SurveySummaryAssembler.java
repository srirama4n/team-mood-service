package com.dbs.app.assembler;

import com.dbs.app.model.Survey;
import com.dbs.app.model.Temperature;
import com.dbs.app.resource.SurveyResource;
import com.dbs.app.resource.TemperatureResource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SurveySummaryAssembler {

    private SurveyAssembler surveyAssembler;

    public SurveyResource assemble(Survey survey) {
        SurveyResource resource = surveyAssembler.assemble(survey);
        assembleTemperatureInfo(resource, survey);
        return resource;
    }

    private void assembleTemperatureInfo(SurveyResource resource, Survey survey) {
        List<Temperature> temperatures = survey.getTemperature()
            .stream()
            .sorted(Comparator.comparingInt(Temperature::getRating))
            .collect(Collectors.toList());
        if (!temperatures.isEmpty()) {
            resource.setSubmissions(temperatures.size());
            resource.setMinTemperature(temperatures.get(0).getRating());
            resource.setMaxTemperature(temperatures.get(temperatures.size() - 1).getRating());
            assembleAvgTemperature(temperatures, resource);
            assembleIndividualTemperature(temperatures, resource);
        }
    }

    private void assembleAvgTemperature(List<Temperature> temperatures, SurveyResource resource) {
        Double averageRating = temperatures.stream()
            .map(Temperature::getRating)
            .mapToDouble(Double::valueOf)
            .average()
            .orElse(0d);
        resource.setAvgTemperature(averageRating);
    }

    private void assembleIndividualTemperature(List<Temperature> temperatures, SurveyResource resource) {
        List<TemperatureResource> temperatureResources = temperatures.stream().map(temperature -> {
            TemperatureResource temperatureResource = new TemperatureResource();
            temperatureResource.setId(temperature.getId());
            temperatureResource.setRating(temperature.getRating());
            temperatureResource.setComment(temperature.getComment());
            return temperatureResource;
        }).collect(Collectors.toList());
        resource.setTemperatures(temperatureResources);
    }
}
