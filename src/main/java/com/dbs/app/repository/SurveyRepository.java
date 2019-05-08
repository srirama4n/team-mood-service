package com.dbs.app.repository;

import com.dbs.app.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, String> {

    List<Survey> findAllBySquadNameContainsOrderByExpiresOnDesc(String squadName);

    void deleteById(String id);
}
