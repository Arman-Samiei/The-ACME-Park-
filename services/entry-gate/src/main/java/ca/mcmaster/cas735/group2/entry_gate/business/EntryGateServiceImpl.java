package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.*;
import ca.mcmaster.cas735.group2.entry_gate.ports.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EntryGateServiceImpl implements
        TransponderGateActivity,
        VisitorGateActivity,
        VoucherGateActivity,
        ValidationResponseHandler,
        VisitorLotResponse {

    private final ValidateTransponderEntry validateTransponderEntry;
    private final ValidateVoucherEntry validateVoucherEntry;
    private final ValidateVisitorEntry validateVisitorEntry;
    private final LotStatistics lotStatistics;
    private final ForwardGateAction forwardGateAction;

    @Autowired
    public EntryGateServiceImpl(ValidateTransponderEntry validateTransponderEntry,
                                ValidateVoucherEntry validateVoucherEntry,
                                ValidateVisitorEntry validateVisitorEntry,
                                LotStatistics lotStatistics,
                                ForwardGateAction forwardGateAction) {
        this.validateTransponderEntry = validateTransponderEntry;
        this.validateVoucherEntry = validateVoucherEntry;
        this.validateVisitorEntry = validateVisitorEntry;
        this.lotStatistics = lotStatistics;
        this.forwardGateAction = forwardGateAction;
    }

    @Override
    public void receiveTransponderGateActivity(TransponderGateActionDTO transponderGateActionDTO) {
        validateTransponderEntry.sendTransponderEntryValidationRequest(transponderGateActionDTO);
    }

    @Override
    public void receiveVoucherGateActivity(VoucherGateActionDTO voucherGateActionDTO) {
        validateVoucherEntry.sendVoucherEntryValidationRequest(voucherGateActionDTO);
    }

    @Override
    public void receiveVisitorGateActivity(VisitorGateActionDTO visitorGateActionDTO) {
        VisitorGateRequestForLotDTO visitorGateRequestForLotDTO = new VisitorGateRequestForLotDTO(
                visitorGateActionDTO.gateId(),
                "visitor",
                visitorGateActionDTO.licencePlate()
        );
        validateVisitorEntry.sendVisitorEntryValidationRequest(visitorGateRequestForLotDTO);
    }

    @Override
    public void sendVisitorLotResponse(VisitorGateLotResponseDTO visitorGateLotResponseDTO) {
        if (!visitorGateLotResponseDTO.spotID().isEmpty()) {
            forwardGateAction.sendGateAction(new GateActionDTO(true, visitorGateLotResponseDTO.lotID(), visitorGateLotResponseDTO.spotID()));
            lotStatistics.updateEntryLotStatistics(visitorGateLotResponseDTO.lotID());
        } else {
            forwardValidationToGate(new GateActionDTO(false, visitorGateLotResponseDTO.lotID(), ""));
        }
    }

    @Override
    public void forwardValidationToGate(GateActionDTO gateActionDTO) {
        forwardGateAction.sendGateAction(gateActionDTO);
        if (gateActionDTO.shouldOpen()) {
            lotStatistics.updateEntryLotStatistics(gateActionDTO.gateId());
        }
    }
}
