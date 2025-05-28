package rca.rw.secure.impls;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rca.rw.secure.models.AuditLog;
import rca.rw.secure.repos.IAuditLogRepo;
import rca.rw.secure.services.AuditLogService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final IAuditLogRepo auditLogRepo;
    private static final Logger logger = LoggerFactory.getLogger(AuditLogServiceImpl.class);

    @Override
    public void logAction(String entityType, String entityId, String action, String performedBy, String details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(java.util.UUID.fromString(entityId));
        auditLog.setAction(action);
        auditLog.setPerformedBy(performedBy);
        auditLog.setPerformedAt(LocalDateTime.now());
        auditLog.setDetails(details);

        auditLogRepo.save(auditLog);
        logger.info("Audit log created: {} - {} by {}", entityType, action, performedBy);
    }
}