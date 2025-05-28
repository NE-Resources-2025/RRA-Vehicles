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
@RequestMapping("/api/v1/resources")
@RequiredArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
public class ResourceController {

    private final BookService bookService;

    @PostMapping("/create-resource")
    public ResponseEntity<ApiResponse<BookResponseDTO>> createResource(
            @Valid @RequestBody CreateBookDTO createBookDTO,
            Authentication authentication) {
        String username = authentication.getName();
        Book book = bookService.createResource(createBookDTO, username);
        BookResponseDTO responseDTO = mapToResponseDTO(book);
        return ApiResponse.success("Resource created successfully", HttpStatus.CREATED, responseDTO);
    }

    @PatchMapping("/update-resource/{id}")
    public ResponseEntity<ApiResponse<BookResponseDTO>> updateResource(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBookDTO updateBookDTO,
            Authentication authentication) {
        String username = authentication.getName();
        Book book = bookService.updateResource(id, updateBookDTO, username);
        BookResponseDTO responseDTO = mapToResponseDTO(book);
        return ApiResponse.success("Resource updated successfully", HttpStatus.OK, responseDTO);
    }

    @DeleteMapping("/delete-resource/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResource(
            @PathVariable UUID id,
            Authentication authentication) {
        String username = authentication.getName();
        bookService.deleteResource(id, username);
        return ApiResponse.success("Resource deleted successfully", HttpStatus.OK, null);
    }

    @GetMapping("/get-resource/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<BookResponseDTO>> getResourceById(@PathVariable UUID id) {
        Book book = bookService.getResourceById(id);
        BookResponseDTO responseDTO = mapToResponseDTO(book);
        return ApiResponse.success("Resource retrieved successfully", HttpStatus.OK, responseDTO);
    }

    @GetMapping("/get-resources")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<BooksResponseDTO>> getAllResources(
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Book> resourcePage = bookService.getAllResources(pageable);
        Page<BookResponseDTO> responsePage = resourcePage.map(this::mapToResponseDTO);
        BooksResponseDTO responseDTO = new BooksResponseDTO(responsePage);
        return ApiResponse.success("Resources retrieved successfully", HttpStatus.OK, responseDTO);
    }

    @GetMapping("/search-resources")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STANDARD')")
    public ResponseEntity<ApiResponse<BooksResponseDTO>> searchResources(
            @RequestParam String searchKey,
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_PAGE_SIZE) int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Book> resourcePage = bookService.searchResources(searchKey, pageable);
        Page<BookResponseDTO> responsePage = resourcePage.map(this::mapToResponseDTO);
        BooksResponseDTO responseDTO = new BooksResponseDTO(responsePage);
        return ApiResponse.success("Resources searched successfully", HttpStatus.OK, responseDTO);
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