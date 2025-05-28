package rca.rw.secure.impls;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import rca.rw.secure.dtos.book.CreateBookDTO;
import rca.rw.secure.dtos.book.UpdateBookDTO;
import rca.rw.secure.models.Book;
import rca.rw.secure.repos.IBookRepo;
import rca.rw.secure.services.AuditLogService;
import rca.rw.secure.services.MailService;
import rca.rw.secure.services.BookService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final IBookRepo bookRepo;
    private final MailService mailService;
    private final AuditLogService auditLogService;
    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Book createBook(CreateBookDTO dto, String username) {
        Book book = new Book();
        book.setName(dto.getName());
        book.setDescription(dto.getDescription());

        Book savedBook = bookRepo.save(book);
        auditLogService.logAction("Book", savedBook.getId().toString(), "CREATE", username,
                "Created resource: " + savedBook.getName());
        mailService.sendResourceCreatedEmail(username, savedBook);
        logger.info("Resource created: {}", savedBook.getId());
        return savedBook;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Book updateBook(UUID id, UpdateBookDTO dto, String username) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Resource not found: " + id));
        if (dto.getName() != null) {
            book.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            book.setDescription(dto.getDescription());
        }

        Book updatedBook = bookRepo.save(book);
        auditLogService.logAction("Resource", id.toString(), "UPDATE", username,
                "Updated resource: " + updatedBook.getName());
        mailService.sendResourceUpdatedEmail(username, updatedBook);
        logger.info("Resource updated: {}", id);
        return updatedBook;
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public void deleteBook(UUID id, String username) {
        Book book = bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
        bookRepo.delete(book);
        auditLogService.logAction("Book", id.toString(), "DELETE", username,
                "Deleted resource: " + book.getName());
        mailService.sendResourceDeletedEmail(username, book);
        logger.info("Book deleted: {}", id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public Book getBookById(UUID id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found: " + id));
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepo.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public Page<Book> searchBooks(String searchKey, Pageable pageable) {
        return bookRepo.findByNameContainingIgnoreCase(searchKey, pageable);
    }
}