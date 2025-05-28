package rca.rw.secure.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import rca.rw.secure.dtos.resource.CreateResourceDTO;
import rca.rw.secure.dtos.resource.UpdateResourceDTO;
import rca.rw.secure.models.Resource;
import rca.rw.secure.repos.IResourceRepo;
import rca.rw.secure.services.AuditLogService;
import rca.rw.secure.services.MailService;
import rca.rw.secure.services.ResourceService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final IResourceRepo resourceRepo;
    private final MailService mailService;
    private final AuditLogService auditLogService;
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Resource createResource(CreateResourceDTO dto, String username) {
        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setDescription(dto.getDescription());

        Resource savedResource = resourceRepo.save(resource);
        auditLogService.logAction("Resource", savedResource.getId().toString(), "CREATE", username,
                "Created resource: " + savedResource.getName());
        mailService.sendResourceCreatedEmail(username, savedResource);
        logger.info("Resource created: {}", savedResource.getId());
        return savedResource;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Resource updateResource(UUID id, UpdateResourceDTO dto, String username) {
        Resource resource = resourceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + id));
        if (dto.getName() != null) {
            resource.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            resource.setDescription(dto.getDescription());
        }

        Resource updatedResource = resourceRepo.save(resource);
        auditLogService.logAction("Resource", id.toString(), "UPDATE", username,
                "Updated resource: " + updatedResource.getName());
        mailService.sendResourceUpdatedEmail(username, updatedResource);
        logger.info("Resource updated: {}", id);
        return updatedResource;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteResource(UUID id, String username) {
        Resource resource = resourceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + id));
        resourceRepo.delete(resource);
        auditLogService.logAction("Resource", id.toString(), "DELETE", username,
                "Deleted resource: " + resource.getName());
        mailService.sendResourceDeletedEmail(username, resource);
        logger.info("Resource deleted: {}", id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Resource getResourceById(UUID id) {
        return resourceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Resource> getAllResources(Pageable pageable) {
        return resourceRepo.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Page<Resource> searchResources(String searchKey, Pageable pageable) {
        return resourceRepo.findByNameContainingIgnoreCase(searchKey, pageable);
    }
}