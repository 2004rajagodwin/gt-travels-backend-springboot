package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.BusImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BusImageRepository extends JpaRepository<BusImage, Long> {

    List<BusImage> findByBusIdAndActiveTrueOrderByPrimaryImageDesc(Long busId);
}
