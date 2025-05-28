package rca.rw.secure.services;

public interface AuditLogService {
    void logAction(String entityType, String entityId, String action, String performedBy, String details);
}