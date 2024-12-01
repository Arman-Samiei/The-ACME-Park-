package ca.mcmaster.cas735.group2.voucher_service.business;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherIssuanceRequestData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotRequestData;
import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotResponseData;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.LotResponse;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.VoucherIssuanceRequest;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.LotRequest;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherIssuanceResponse;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class VoucherIssuer implements VoucherIssuanceRequest, LotResponse {

    private final VoucherRepository database;
    private final LotRequest lotRequest;

    private final VoucherIssuanceResponse voucherIssuanceResponse;

    @Autowired
    public VoucherIssuer(VoucherRepository database, LotRequest lotRequest, VoucherIssuanceResponse voucherIssuanceResponse) {
        this.database = database;
        this.lotRequest = lotRequest;
        this.voucherIssuanceResponse = voucherIssuanceResponse;

    }

    @Override
    public void issue(VoucherIssuanceRequestData voucherIssuanceRequestData) {
        VoucherData voucherData = voucherIssuanceRequestData.toVoucherData();
        database.saveAndFlush(voucherData);
        lotRequest.requestSpot(new VoucherLotRequestData(voucherData));
    }

    @Override
    public void reserveSpot(VoucherLotResponseData voucherLotResponseData) {
        String plateNumber = voucherLotResponseData.getPlateNumber();
        String spotID = voucherLotResponseData.getSpotID();
        String lotID = voucherLotResponseData.getLotID();
        VoucherData voucherData = database.findByPlateNumber(plateNumber);
        if (spotID.isEmpty())
            handleNoAvailableSpot(plateNumber, lotID, voucherData);
        else
            finalizeVoucher(spotID, voucherData);
    }

    private void handleNoAvailableSpot(String plateNumber, String lotID, VoucherData voucherData) {
        String response = String.format("There is no space left for %s at lot %s.", plateNumber, lotID);
        voucherIssuanceResponse.sendVoucherIssuanceResponse(response);
        database.delete(voucherData);
    }

    private void finalizeVoucher(String spotID, VoucherData voucherData) {
        voucherData.setSpotID(spotID);
        voucherData.setStatus("issued");
        database.saveAndFlush(voucherData);
    }

}