package ca.mcmaster.cas735.group2.lot.business;

import ca.mcmaster.cas735.group2.lot.business.entities.LotData;
import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityRequestData;
import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityResponseData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotAvailabilityCheckRequest;
import ca.mcmaster.cas735.group2.lot.ports.required.LotAvailabilityCheckResponse;
import ca.mcmaster.cas735.group2.lot.ports.required.LotRepository;
import ca.mcmaster.cas735.group2.lot.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class LotAvailability implements LotAvailabilityCheckRequest {

    private final LotRepository database;
    private final LotAvailabilityCheckResponse responseSender;

    @Autowired
    public LotAvailability(LotRepository database, LotAvailabilityCheckResponse responseSender) {
        this.database = database;
        this.responseSender = responseSender;
    }

    @Override
    public void checkLotAvailability(LotAvailabilityRequestData requestData) {
        String requestSender = requestData.getRequestSender();
        String accessPassProcessingStatus = requestData.getAccessPassProcessingStatus();

        if (isPermitRequest(requestSender))
            handlePermitRequest(requestData, accessPassProcessingStatus);
        else if (isVisitorSender(requestSender))
            handleVisitorRequest(requestData);
        else if (isVoucherSender(requestSender))
            handleVoucherRequest(requestData);

    }

    private boolean isPermitRequest(String requestSender) {
        return Constants.SENDER_RECEIVER_PERMIT.equals(requestSender);
    }

    private boolean isVisitorSender(String requestSender) {
        return Constants.SENDER_RECEIVER_VISITOR.equals(requestSender);
    }

    private boolean isVoucherSender(String requestSender) {
        return Constants.SENDER_RECEIVER_VOUCHER.equals(requestSender);
    }

    private void handlePermitRequest(LotAvailabilityRequestData requestData, String accessPassProcessingStatus) {
        if (Constants.ACCESS_PASS_PROCESSING_STATUS_PENDING.equals(accessPassProcessingStatus))
            handlePermitPendingRequest(requestData);
        else if (Constants.ACCESS_PASS_PROCESSING_STATUS_CONFIRMED.equals(accessPassProcessingStatus))
            handleConfirmedRequest(requestData);
        else if (Constants.ACCESS_PASS_PROCESSING_STATUS_NOT_REJECTED.equals(accessPassProcessingStatus))
            handleNotConfirmedRequest(requestData);

    }

    private void handlePermitPendingRequest(LotAvailabilityRequestData requestData) {
        LotData availableSpot = findAvailableSpot(requestData);

        if (availableSpot == null) {
            sendEmptyResponse(Constants.SENDER_RECEIVER_PERMIT);
            return;
        }

        updateSpot(availableSpot, Constants.SPOT_RESERVATION_STATUS_PENDING, requestData.getPlateNumber());
        sendSpotResponse(availableSpot, Constants.SENDER_RECEIVER_PERMIT);
    }

    private void handleConfirmedRequest(LotAvailabilityRequestData requestData) {
        String plateNumber = requestData.getPlateNumber();
        LotData spot = database.findByPlateNumber(plateNumber);
        updateSpot(spot, Constants.SPOT_RESERVATION_STATUS_RESERVED, plateNumber);
    }

    private void handleNotConfirmedRequest(LotAvailabilityRequestData requestData) {
        String plateNumber = requestData.getPlateNumber();
        LotData spot = database.findByPlateNumber(plateNumber);
        updateSpot(spot, Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED, plateNumber);
    }

    private void handleVisitorRequest(LotAvailabilityRequestData requestData) {
        LotData availableSpot = findAvailableSpot(requestData);

        if (availableSpot == null) {
            sendEmptyResponse(Constants.SENDER_RECEIVER_VISITOR);
            return;
        }

        updateSpot(availableSpot, Constants.SPOT_RESERVATION_STATUS_RESERVED, requestData.getPlateNumber());
        sendSpotResponse(availableSpot, Constants.SENDER_RECEIVER_VISITOR);
    }

    private void handleVoucherRequest(LotAvailabilityRequestData requestData) {
        LotData availableSpot = findAvailableSpot(requestData);

        if (availableSpot == null) {
            sendEmptyResponse(Constants.SENDER_RECEIVER_VOUCHER);
            return;
        }

        updateSpot(availableSpot, Constants.SPOT_RESERVATION_STATUS_RESERVED, requestData.getPlateNumber());
        sendSpotResponse(availableSpot, Constants.SENDER_RECEIVER_VOUCHER);
    }

    private LotData findAvailableSpot(LotAvailabilityRequestData requestData) {
        return database.findFirstByCustomerTypeAndSpotReservationStatusAndLotID(
                requestData.getCustomerType(),
                Constants.SPOT_RESERVATION_STATUS_NOT_RESERVED,
                requestData.getLotID()
        );
    }

    private void updateSpot(LotData spot, String status, String plateNumber) {
        spot.setSpotReservationStatus(status);
        spot.setPlateNumber(plateNumber);
        database.saveAndFlush(spot);
    }

    private void sendSpotResponse(LotData spot, String recipient) {
        LotAvailabilityResponseData responseData = new LotAvailabilityResponseData(spot);
        responseSender.sendAvailableSpot(responseData, recipient);
    }

    private void sendEmptyResponse(String recipient) {
        responseSender.sendAvailableSpot(LotAvailabilityResponseData.emptyResponse(), recipient);
    }
}
