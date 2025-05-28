package rca.rw.secure.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rca.rw.secure.dtos.resource.CreateResourceDTO;
import rca.rw.secure.dtos.resource.UpdateResourceDTO;
import rca.rw.secure.models.Resource;

import java.util.UUID;

public interface ResourceService {
    Resource createResource(CreateResourceDTO dto, String username);
    Resource updateResource(UUID id, UpdateResourceDTO dto, String username);
    void deleteResource(UUID id, String username);
    Resource getResourceById(UUID id);
    Page<Resource> getAllResources(Pageable pageable);
    Page<Resource> searchResources(String searchKey, Pageable pageable);
}