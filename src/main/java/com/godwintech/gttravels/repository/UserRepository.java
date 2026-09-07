package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByGoogleId(String googleId);

    Boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    long countByRoles_Name(ERole name);
}