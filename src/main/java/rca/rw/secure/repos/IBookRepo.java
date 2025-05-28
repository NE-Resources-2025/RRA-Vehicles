package rca.rw.secure.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import rca.rw.secure.models.Book;

import java.util.UUID;

public interface IBookRepo extends JpaRepository<Book, UUID> {
    Page<Book> findByNameContainingIgnoreCase(String name, Pageable pageable);
}