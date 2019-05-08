package com.dbs.app.repository;

import com.dbs.app.model.Tower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TowerRepository extends JpaRepository<Tower, String> {
    List<Tower> findTowersByGroup_Id(String group);
}
