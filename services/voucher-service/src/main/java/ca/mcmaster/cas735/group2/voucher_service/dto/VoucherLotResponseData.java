package ca.mcmaster.cas735.group2.voucher_service.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data @Slf4j
public class VoucherLotResponseData {
    String plateNumber;
    String lotID;
    String spotID;
}
