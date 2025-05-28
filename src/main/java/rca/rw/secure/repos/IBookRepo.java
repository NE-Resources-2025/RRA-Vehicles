package rca.rw.secure.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rca.rw.secure.models.Resource;

import java.util.UUID;

public interface IResourceRepo extends JpaRepository<Resource, UUID> {
    Page<Resource> findByNameContainingIgnoreCase(String name, Pageable pageable);
}