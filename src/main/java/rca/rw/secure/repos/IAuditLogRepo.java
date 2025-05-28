package rca.rw.secure.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IAuditLogRepo extends JpaRepository<rca.rw.secure.models.AuditLog, UUID> {
}