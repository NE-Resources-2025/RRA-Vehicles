package rca.rw.secure.dtos.vehicleOwnerHistory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleOwnershipHistoryResponseDTO {
    private Long id;
    private Long vehicleId;
    private String chassisNumber;
    private Long ownerId;
    private String ownerNames;
    private String nationalId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal purchasePrice;
    private String previousPlateNumber;
    private String comments;
}