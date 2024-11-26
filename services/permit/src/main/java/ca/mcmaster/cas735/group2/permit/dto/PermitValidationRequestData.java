package ca.mcmaster.cas735.group2.permit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitValidationRequestData {
    private String lotNumber;
    private String transponderID;
}
