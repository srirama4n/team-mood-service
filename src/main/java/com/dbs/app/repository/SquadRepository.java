package com.dbs.app.repository;

import com.dbs.app.model.Squad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SquadRepository extends JpaRepository<Squad, String> {
    List<Squad> findSquadsByProject_Id(String project);
}
