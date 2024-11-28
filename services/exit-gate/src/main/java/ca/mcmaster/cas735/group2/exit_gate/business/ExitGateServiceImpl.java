package ca.mcmaster.cas735.group2.exit_gate.business;

import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.LotStatistics;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateTransponderExit;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateVisitorExit;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateVoucherExit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExitGateServiceImpl implements ExitGateService {

    private final ValidateTransponderExit validateTransponderExit;
    private final ValidateVoucherExit validateVoucherExit;
    private final ValidateVisitorExit validateVisitorExit;
    private final LotStatistics lotStatistics;

    @Autowired
    public ExitGateServiceImpl(ValidateTransponderExit validateTransponderExit,
                               ValidateVoucherExit validateVoucherExit,
                               ValidateVisitorExit validateVisitorExit,
                               LotStatistics lotStatistics) {
        this.validateTransponderExit = validateTransponderExit;
        this.validateVoucherExit = validateVoucherExit;
        this.validateVisitorExit = validateVisitorExit;
        this.lotStatistics = lotStatistics;
    }

    @Override
    public void validateAndProcessGateAction(TransponderGateActionDTO transponderGateActionDTO) {
        validateTransponderExit.sendTransponderExitValidationRequest(transponderGateActionDTO.transponderId());
    }

    @Override
    public void validateAndProcessGateAction(VoucherGateActionDTO voucherGateActionDTO) {
        validateVoucherExit.sendVoucherExitValidationRequest(voucherGateActionDTO.voucherId());
    }

    @Override
    public void validateAndProcessGateAction(VisitorGateActionDTO visitorGateActionDTO) {
        VisitorGateActionDTO calculatedVisitorExit = calculatePaymentAmount(visitorGateActionDTO);
        validateVisitorExit.sendVisitorExitValidationRequest(calculatedVisitorExit);
    }
    
    private VisitorGateActionDTO calculatePaymentAmount(VisitorGateActionDTO visitorGateActionDTO) {
        return new VisitorGateActionDTO(visitorGateActionDTO.qrId(),
                visitorGateActionDTO.ccNumber(),
                visitorGateActionDTO.ccExpiry(),
                visitorGateActionDTO.ccCVC(),
                10.5,
                "VISITOR_EXIT");
    }

    @Override
    public void processGateAction(boolean shouldOpen, String lot) {
        if (shouldOpen) {
            log.info("Exit Gate opened");
            lotStatistics.updateExitLotStatistics(lot);
        } else {
            log.info("Exit Gate remained closed");
        }
    }
}
