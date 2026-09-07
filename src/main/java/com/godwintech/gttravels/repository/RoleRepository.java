package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.Role;
import com.godwintech.gttravels.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}