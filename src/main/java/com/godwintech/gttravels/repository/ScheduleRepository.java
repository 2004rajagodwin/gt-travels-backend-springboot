package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {

    List<Schedule> findByRoute_SourceAndRoute_DestinationAndDepartureTimeBetween(
            String source,
            String destination,
            LocalDateTime start,
            LocalDateTime end
    );

    long countByBus_Operator_Id(Long operatorId);
}
