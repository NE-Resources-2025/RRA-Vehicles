package rca.rw.secure.dtos.vehicle;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class VehiclesResponseDTO {
    private List<VehicleResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public VehiclesResponseDTO(Page<VehicleResponseDTO> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}