package rca.rw.secure.dtos.vehicleOwner;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class VehicleOwnersResponseDTO {
    private List<VehicleOwnerResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public VehicleOwnersResponseDTO(Page<VehicleOwnerResponseDTO> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}