package com.godwintech.gttravels.repository;

import com.godwintech.gttravels.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
