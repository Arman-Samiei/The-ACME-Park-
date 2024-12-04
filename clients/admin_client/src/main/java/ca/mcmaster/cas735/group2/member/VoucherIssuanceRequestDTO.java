package ca.mcmaster.cas735.group2.member;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherIssuanceRequestDTO {
    String plateNumber;
    String lotID;
    Integer days;

    public VoucherIssuanceRequestDTO(String plateNumber, String lotID, Integer days) {
        this.plateNumber = plateNumber;
        this.lotID = lotID;
        this.days = days;
    }
}
