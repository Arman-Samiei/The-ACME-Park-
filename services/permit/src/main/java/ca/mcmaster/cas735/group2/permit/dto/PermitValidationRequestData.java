package ca.mcmaster.cas735.group2.permit.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitValidationRequestData {
    private String lotID;
    private String transponderID;
}
