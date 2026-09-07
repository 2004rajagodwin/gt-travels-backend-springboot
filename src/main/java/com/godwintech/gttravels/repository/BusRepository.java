package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Bus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Long> {

    List<Bus> findByOperatorId(Long operatorId);

    long countByOperatorId(Long operatorId);

    List<Bus> findAllByOrderByIdDesc();

    Optional<Bus> findByBusNumber(String busNumber);

    boolean existsByBusNumber(String busNumber);

    boolean existsByRegistrationNumber(String registrationNumber);

    boolean existsByRegistrationNumberAndIdNot(String registrationNumber, Long id);
}