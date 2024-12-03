package ca.mcmaster.cas735.group2.permit.dto;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentRequestData {
    private String plateNumber;

    private String paymentType = "NEW_PERMIT";
    private String staffId;
    private String ccNumber;
    private String ccExpiry;
    private String ccCVC;
    private Integer monthsPurchased;

    public PaymentRequestData(PermitData permitData, String staffId) {
        this.plateNumber = permitData.getPlateNumber();
        this.staffId = staffId;
        this.ccNumber = permitData.getCcNumber();
        this.ccExpiry = permitData.getCcExpiry();
        this.ccCVC = permitData.getCcCVC();
        this.monthsPurchased = permitData.getMonthsPurchased();
    }
}

