package rca.rw.secure.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rca.rw.secure.dtos.book.CreateBookDTO;
import rca.rw.secure.dtos.book.UpdateBookDTO;
import rca.rw.secure.models.Book;

import java.util.UUID;

public interface BookService {
    Book createBook(CreateBookDTO dto, String username);
    Book updateBook(UUID id, UpdateBookDTO dto, String username);
    void deleteBook(UUID id, String username);
    Book getBookById(UUID id);
    Page<Book> getAllBooks(Pageable pageable);
    Page<Book> searchBooks(String searchKey, Pageable pageable);
}