package ca.mcmaster.cas735.group2.permit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequestData {
    private String plateNumber;
    private String type = "permit";

    public PaymentRequestData(String plateNumber) {
        this.plateNumber = plateNumber;
    }
}
