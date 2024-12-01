package ca.mcmaster.cas735.group2.fines.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinesCalculationResponseData {
    String plateNumber;
    Double amount;
    public FinesCalculationResponseData (String plateNumber, Double amount){
        this.plateNumber = plateNumber;
        this.amount = amount;

    }
}
