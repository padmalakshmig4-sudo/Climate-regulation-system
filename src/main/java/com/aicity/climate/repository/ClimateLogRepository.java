package com.aicity.climate.repository;

import com.aicity.climate.model.ClimateLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClimateLogRepository extends JpaRepository<ClimateLog, Long> {

    List<ClimateLog> findAllByOrderByIdDesc();

    List<ClimateLog> findByCategory(String category);
}
