package com.dbs.app.repository;

import com.dbs.app.model.Temperature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemperatureRepository extends JpaRepository<Temperature, String> {
    List<Temperature> findBySurvey_IdOrderByRating(String surveyId);
}
