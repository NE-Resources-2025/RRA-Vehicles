package rca.rw.secure.dtos.vehicle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponseDTO {
    private Long id;
    private String chassisNumber;
    private String manufactureCompany;
    private Year manufactureYear;
    private BigDecimal price;
    private String modelName;
    private String plateNumber;
    private Long ownerId;
    private String ownerNames;
    private String nationalId;
}