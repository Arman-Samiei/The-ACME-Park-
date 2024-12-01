package ca.mcmaster.cas735.group2.voucher_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherValidationRequestData {
    private String lotID;
    private String plateNumber;
}
