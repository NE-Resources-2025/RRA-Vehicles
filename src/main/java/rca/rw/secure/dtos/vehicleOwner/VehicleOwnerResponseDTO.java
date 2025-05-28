package rca.rw.secure.dtos.vehicleOwner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleOwnerResponseDTO {
    private Long id;
    private String ownerNames;
    private String nationalId;
    private String phoneNumber;
    private String address;
}
