package ca.mcmaster.cas735.Group2.Voucher.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor @Data
public class FilterRequest {

    private String verb;
    private String parameter;

}
