package ca.mcmaster.cas735.group2.admin;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherIssuanceRequestDTO {
    String plateNumber;
    String lotID;

    public VoucherIssuanceRequestDTO(String plateNumber, String lotID) {
        this.plateNumber = plateNumber;
        this.lotID = lotID;
    }
}
