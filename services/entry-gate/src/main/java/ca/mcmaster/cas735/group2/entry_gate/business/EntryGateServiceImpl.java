package ca.mcmaster.cas735.group2.entry_gate.business;

import ca.mcmaster.cas735.group2.entry_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ForwardGateAction;
import ca.mcmaster.cas735.group2.entry_gate.ports.LotStatistics;
import ca.mcmaster.cas735.group2.entry_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateTransponderEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVoucherEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidationResponseHandler;
import ca.mcmaster.cas735.group2.entry_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.VoucherGateActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EntryGateServiceImpl implements
        TransponderGateActivity,
        VisitorGateActivity,
        VoucherGateActivity,
        ValidationResponseHandler {

    private final ValidateTransponderEntry validateTransponderEntry;
    private final ValidateVoucherEntry validateVoucherEntry;
    private final LotStatistics lotStatistics;
    private final ForwardGateAction forwardGateAction;

    @Autowired
    public EntryGateServiceImpl(ValidateTransponderEntry validateTransponderEntry,
                                ValidateVoucherEntry validateVoucherEntry,
                                LotStatistics lotStatistics,
                                ForwardGateAction forwardGateAction) {
        this.validateTransponderEntry = validateTransponderEntry;
        this.validateVoucherEntry = validateVoucherEntry;
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
    public String receiveVisitorGateActivity(VisitorGateActionDTO visitorGateActionDTO) {
        String qrId = UUID.randomUUID().toString();
        // QR Code is generated and sent to the visitor and enter the gate
        forwardValidationToGate(new GateActionDTO(true, visitorGateActionDTO.gateId()));
        return qrId;
    }

    @Override
    public void forwardValidationToGate(GateActionDTO gateActionDTO) {
        forwardGateAction.sendGateAction(gateActionDTO);
        if (gateActionDTO.shouldOpen()) {
            lotStatistics.updateEntryLotStatistics(gateActionDTO.gateId());
        }
    }
}
