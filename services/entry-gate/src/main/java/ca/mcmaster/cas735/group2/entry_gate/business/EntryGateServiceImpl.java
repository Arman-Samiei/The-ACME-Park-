package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.UpdateLotStatisticsDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateLotResponseDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateRequestForLotDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ForwardGateAction;
import ca.mcmaster.cas735.group2.entry_gate.ports.LotStatistics;
import ca.mcmaster.cas735.group2.entry_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateTransponderEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVisitorEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVoucherEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidationResponseHandler;
import ca.mcmaster.cas735.group2.entry_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.VisitorLotResponse;
import ca.mcmaster.cas735.group2.entry_gate.ports.VoucherGateActivity;
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
                visitorGateActionDTO.lotID(),
                "visitor",
                visitorGateActionDTO.plateNumber()
        );
        validateVisitorEntry.sendVisitorEntryValidationRequest(visitorGateRequestForLotDTO);
    }

    @Override
    public void sendVisitorLotResponse(VisitorGateLotResponseDTO visitorGateLotResponseDTO) {
        if (!visitorGateLotResponseDTO.spotID().isEmpty()) {
            forwardGateAction.sendGateAction(new GateActionDTO(true, visitorGateLotResponseDTO.lotID(), visitorGateLotResponseDTO.spotID(), true));
            lotStatistics.updateEntryLotStatistics(new UpdateLotStatisticsDTO(visitorGateLotResponseDTO.spotID(), true));
        } else {
            forwardValidationToGate(new GateActionDTO(false, visitorGateLotResponseDTO.lotID(), "", true));
        }
    }

    @Override
    public void forwardValidationToGate(GateActionDTO gateActionDTO) {
        forwardGateAction.sendGateAction(gateActionDTO);
        if (gateActionDTO.shouldOpen()) {
            lotStatistics.updateEntryLotStatistics(new UpdateLotStatisticsDTO(gateActionDTO.spotID(), true));
        }
    }
}
