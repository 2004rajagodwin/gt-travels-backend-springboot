package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.AuditLog;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.AuditActionType;
import com.godwintech.gttravels.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Transactional
    public void log(AuditActionType actionType, String entityType, String entityId, User actor, String details) {
        AuditLog log = new AuditLog();
        log.setActionType(actionType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        if (actor != null) {
            log.setActorId(actor.getId());
            log.setActorEmail(actor.getEmail());
        }
        log.setDetails(details);
        auditLogRepository.save(log);
    }
}
