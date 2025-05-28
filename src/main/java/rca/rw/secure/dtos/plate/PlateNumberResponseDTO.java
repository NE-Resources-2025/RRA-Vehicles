package rca.rw.secure.dtos.plate;

import lombok.AllArgsConstructor;
import lombok.Data;
import rca.rw.secure.enums.vehicle.EPlateStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PlateNumberResponseDTO {
    private Long id;
    private String plateNumber;
    private LocalDateTime issueDate;
    private Long ownerId;
    private String ownerNames;
    private EPlateStatus status;
}