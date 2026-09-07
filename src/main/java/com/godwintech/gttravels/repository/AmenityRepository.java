package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.Set;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    List<Amenity> findByActiveTrueOrderByNameAsc();

    List<Amenity> findByIdIn(Set<Long> ids);
}
