package rca.rw.secure.dtos.book;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ResourcesResponseDTO {
    private List<BookResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public ResourcesResponseDTO(Page<BookResponseDTO> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}