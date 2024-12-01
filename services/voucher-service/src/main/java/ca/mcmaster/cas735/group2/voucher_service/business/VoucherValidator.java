package ca.mcmaster.cas735.group2.voucher_service.business;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationRequestData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationResponseData;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.VoucherValidationRequest;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherRepository;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class VoucherValidator implements VoucherValidationRequest {
    private final VoucherRepository database;
    private final VoucherValidationResponse voucherValidationResponse;

    @Autowired
    public VoucherValidator(VoucherRepository database, VoucherValidationResponse voucherValidationResponse) {
        this.database = database;
        this.voucherValidationResponse = voucherValidationResponse;
    }

    @Override
    public void validate(VoucherValidationRequestData voucherValidationRequestData) {
        String plateNumber = voucherValidationRequestData.getPlateNumber();
        String lotID = voucherValidationRequestData.getLotID();
        VoucherData voucherData = database.findByPlateNumber(plateNumber);
        VoucherValidationResponseData voucherValidationResponseData;
        if (voucherData != null && Objects.equals(voucherData.getLotID(), lotID))
            voucherValidationResponseData = new VoucherValidationResponseData(true, String.format("The voucher with plateNumber %s is valid", plateNumber));
        else
            voucherValidationResponseData = new VoucherValidationResponseData(false, String.format("The voucher with plateNumber %s is not valid. FUCK OFF!", plateNumber));
        voucherValidationResponse.sendValidationResult(voucherValidationResponseData);
    }

}
