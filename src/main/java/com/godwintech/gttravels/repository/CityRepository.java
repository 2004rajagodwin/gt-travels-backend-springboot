package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.City;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long>, JpaSpecificationExecutor<City> {

    @Query("""
            SELECT c FROM City c
            WHERE c.active = true
            AND LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
            ORDER BY c.name ASC
            """)
    List<City> searchActiveByName(@Param("query") String query, Pageable pageable);

    List<City> findByActiveTrueOrderByNameAsc(Pageable pageable);

    boolean existsByNameAndState(String name, String state);
}
