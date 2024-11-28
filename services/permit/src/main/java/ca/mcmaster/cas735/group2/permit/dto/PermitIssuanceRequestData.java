package ca.mcmaster.cas735.group2.permit.dto;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PermitIssuanceRequestData {

    private String transponderID;

    private String plateNumber;

    private String firstName;

    private String lastName;

    private String employeeID;

    private String lotID;

    private String paymentType;

    private String cardNumber;

    public PermitData asPermitData() {
        PermitData result = new PermitData();
        result.setTransponderID(this.transponderID);
        result.setPlateNumber(this.plateNumber);
        result.setFirstName(this.firstName);
        result.setLastName(this.lastName);
        result.setEmployeeID(this.employeeID);
        result.setLotID(this.lotID);
        result.setPaymentType(this.paymentType);
        result.setCardNumber(this.cardNumber);
        result.setStatus("pending");
        switch (transponderID.charAt(0)) {
            case 's' -> result.setCustomerType("student");
            case 'f' -> result.setCustomerType("faculty");
            default -> result.setCustomerType("staff");
        }

        return result;
    }

}
