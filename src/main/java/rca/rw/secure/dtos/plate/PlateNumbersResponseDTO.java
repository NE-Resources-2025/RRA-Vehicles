package rca.rw.secure.dtos.plate;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PlateNumbersResponseDTO {
    private List<PlateNumberResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public PlateNumbersResponseDTO(Page<PlateNumberResponseDTO> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}
