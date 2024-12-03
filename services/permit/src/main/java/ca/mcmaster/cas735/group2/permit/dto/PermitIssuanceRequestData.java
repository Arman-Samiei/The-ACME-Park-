package ca.mcmaster.cas735.group2.permit.dto;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.utils.Constants;
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
    private String memberPaymentType;
    private String ccNumber;
    private String ccExpiry;
    private String ccCVC;
    private Integer monthsPurchased;

    public PermitData asPermitData() {
        PermitData result = new PermitData();
        result.setTransponderID(this.transponderID);
        result.setPlateNumber(this.plateNumber);
        result.setFirstName(this.firstName);
        result.setLastName(this.lastName);
        result.setEmployeeID(this.employeeID);
        result.setLotID(this.lotID);
        result.setMemberPaymentType(this.memberPaymentType);
        result.setCcNumber(this.ccNumber);
        result.setCcCVC(this.ccCVC);
        result.setCcExpiry(this.ccExpiry);
        result.setMonthsPurchased(this.monthsPurchased);
        result.setStatus(Constants.PENDING_PERMIT_STATUS);
        switch (transponderID.charAt(0)) {
            case 's' -> result.setMemberRole(Constants.STUDENT_MEMBER_ROLE);
            case 'f' -> result.setMemberRole(Constants.FACULTY_MEMBER_ROLE);
            default -> result.setMemberRole(Constants.STAFF_MEMBER_ROLE);
        }

        return result;
    }

}
