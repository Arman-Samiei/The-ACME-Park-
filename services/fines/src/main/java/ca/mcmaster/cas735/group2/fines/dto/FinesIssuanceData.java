package ca.mcmaster.cas735.group2.fines.dto;

import ca.mcmaster.cas735.group2.fines.business.entities.FinesData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FinesIssuanceData {
    String plateNumber;
    Double amount;

    public FinesData toFinesData() {
        FinesData finesData = new FinesData();
        finesData.setPlateNumber(plateNumber);
        finesData.setAmount(amount);
        return finesData;

    }

}
