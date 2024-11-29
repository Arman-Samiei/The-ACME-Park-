package ca.mcmaster.cas735.group2.permit.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data @Slf4j
public class PermitLotResponseData {
    String plateNumber;
    String lotID;
    String spotID;
}
