package ca.mcmaster.cas735.group2.permit.dto;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitLotRequestData {
    String lotID;
    String customerType;
    String transponderID;
    String employeeID;

    public PermitLotRequestData(PermitData permitData) {
        this.lotID = permitData.getLotID();
        this.customerType = permitData.getCustomerType();
        this.transponderID = permitData.getTransponderID();
        this.employeeID = permitData.getEmployeeID();
    }
}
