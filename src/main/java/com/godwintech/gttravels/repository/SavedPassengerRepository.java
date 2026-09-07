package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.SavedPassenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedPassengerRepository extends JpaRepository<SavedPassenger, Long> {

    List<SavedPassenger> findByUserIdOrderByNameAsc(Long userId);

    Optional<SavedPassenger> findByIdAndUserId(Long id, Long userId);
}
