package ca.mcmaster.cas735.group2.exit_gate.business;

import ca.mcmaster.cas735.group2.exit_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.ForwardGateAction;
import ca.mcmaster.cas735.group2.exit_gate.ports.LotStatistics;
import ca.mcmaster.cas735.group2.exit_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateVisitorExit;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidationResponseHandler;
import ca.mcmaster.cas735.group2.exit_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.VoucherGateActivity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExitGateServiceImpl implements TransponderGateActivity, VisitorGateActivity, VoucherGateActivity, ValidationResponseHandler {

    private final ValidateVisitorExit validateVisitorExit;
    private final LotStatistics lotStatistics;
    private final ForwardGateAction forwardGateAction;

    @Autowired
    public ExitGateServiceImpl(ValidateVisitorExit validateVisitorExit,
                               LotStatistics lotStatistics,
                               ForwardGateAction forwardGateAction) {
        this.validateVisitorExit = validateVisitorExit;
        this.lotStatistics = lotStatistics;
        this.forwardGateAction = forwardGateAction;
    }

    @Override
    public void receiveTransponderGateActivity(TransponderGateActionDTO transponderGateActionDTO) {
        processGateAction(new GateActionDTO(true, transponderGateActionDTO.gateId()));
    }

    @Override
    public void receiveVoucherGateActivity(VoucherGateActionDTO voucherGateActionDTO) {
        processGateAction(new GateActionDTO(true, voucherGateActionDTO.gateId()));
    }

    @Override
    public void receiveVisitorGateActivity(VisitorGateActionDTO visitorGateActionDTO) {
        validateVisitorExit.sendVisitorExitValidationRequest(visitorGateActionDTO);
    }

    @Override
    public void processGateAction(GateActionDTO gateActionDTO) {
        forwardGateAction.sendGateAction(gateActionDTO);
        if (gateActionDTO.shouldOpen()) {
            lotStatistics.updateExitLotStatistics(gateActionDTO.gateId());
        }
    }
}
