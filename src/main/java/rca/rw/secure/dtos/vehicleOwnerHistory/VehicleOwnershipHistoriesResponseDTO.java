package rca.rw.secure.dtos.vehicleOwnerHistory;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class VehicleOwnershipHistoriesResponseDTO {
    private List<VehicleOwnershipHistoryResponseDTO> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;

    public VehicleOwnershipHistoriesResponseDTO(Page<VehicleOwnershipHistoryResponseDTO> page) {
        this.content = page.getContent();
        this.page = page.getNumber();
        this.size = page.getSize();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
    }
}