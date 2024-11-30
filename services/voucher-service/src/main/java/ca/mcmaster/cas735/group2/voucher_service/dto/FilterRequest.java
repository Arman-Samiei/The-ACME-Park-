package ca.mcmaster.cas735.group2.voucher_service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class FilterRequest {

    private String verb;
    private String parameter;
}
