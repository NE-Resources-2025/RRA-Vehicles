package rca.rw.secure.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rca.rw.secure.dtos.book.CreateBookDTO;
import rca.rw.secure.dtos.book.BookResponseDTO;
import rca.rw.secure.dtos.book.BooksResponseDTO;
import rca.rw.secure.dtos.book.UpdateBookDTO;
import rca.rw.secure.dtos.response.ApiResponse;
import rca.rw.secure.models.Book;
import rca.rw.secure.services.BookService;
import rca.rw.secure.utils.Constants;

import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class BookController {

    private final BookService bookService;

    @PostMapping("/create-book")
    public ResponseEntity<ApiResponse<BookResponseDTO>> createbook(
            @Valid @RequestBody CreateBookDTO createBookDTO,
            Authentication authentication) {
        String username = authentication.getName();
        Book book = bookService.createBook(createBookDTO, username);
        BookResponseDTO responseDTO = mapToResponseDTO(book);
        return ApiResponse.success("book created successfully", HttpStatus.CREATED, responseDTO);
    }

    @PatchMapping("/update-book/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> updatebook(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookDTO updateBookDTO,
            Authentication authentication) {
        String username = authentication.getName();
        Book book = bookService.updateBook(id, updateBookDTO, username);
        BookResponseDTO responseDTO = mapToResponseDTO(book);
        return ApiResponse.success("book updated successfully", HttpStatus.OK, responseDTO);
    }

    @DeleteMapping("/delete-book/{id}")
    public ResponseEntity<ApiResponse<Void>> deletebook(
            @PathVariable UUID id,
            Authentication authentication) {
        String username = authentication.getName();
        bookService.deleteBook(id, username);
        return ApiResponse.success("book deleted successfully", HttpStatus.OK, null);
    }

    @GetMapping("/get-book/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getbookById(@PathVariable UUID id) {
        Book book = bookService.getBookById(id);
        BookResponseDTO responseDTO = mapToResponseDTO(book);
        return ApiResponse.success("book retrieved successfully", HttpStatus.OK, responseDTO);
    }

    @GetMapping("/get-books")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<BooksResponseDTO>> getAllbooks(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Book> bookPage = bookService.getAllBooks(pageable);
        Page<BookResponseDTO> responsePage = bookPage.map(this::mapToResponseDTO);
        BooksResponseDTO responseDTO = new BooksResponseDTO(responsePage);
        return ApiResponse.success("books retrieved successfully", HttpStatus.OK, responseDTO);
    }

    @GetMapping("/search-books")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<BooksResponseDTO>> searchbooks(
            @RequestParam String searchKey,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Book> bookPage = bookService.searchBooks(searchKey, pageable);
        Page<BookResponseDTO> responsePage = bookPage.map(this::mapToResponseDTO);
        BooksResponseDTO responseDTO = new BooksResponseDTO(responsePage);
        return ApiResponse.success("books searched successfully", HttpStatus.OK, responseDTO);
    }

    private BookResponseDTO mapToResponseDTO(Book book) {
        BookResponseDTO dto = new BookResponseDTO();
        dto.setId(book.getId());
        dto.setName(book.getName());
        dto.setDescription(book.getDescription());
        dto.setCreatedAt(book.getCreatedAt());
        dto.setUpdatedAt(book.getUpdatedAt());
        return dto;
    }
}