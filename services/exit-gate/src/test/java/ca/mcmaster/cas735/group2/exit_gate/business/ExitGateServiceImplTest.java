package ca.mcmaster.cas735.group2.exit_gate.business;

import ca.mcmaster.cas735.group2.exit_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.UpdateLotStatisticsDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.ForwardGateAction;
import ca.mcmaster.cas735.group2.exit_gate.ports.LotStatistics;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateVisitorExit;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateVoucherFines;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_CVC;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_EXPIRY;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_NUMBER;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.SPOT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ExitGateServiceImplTest {

    @Mock
    private ValidateVisitorExit validateVisitorExit;
    @Mock
    private ValidateVoucherFines validateVoucherFines;
    @Mock
    private LotStatistics lotStatistics;
    @Mock
    private ForwardGateAction forwardGateAction;

    @InjectMocks
    private ExitGateServiceImpl exitGateService;

    @Test
    public void testReceiveTransponderGateActivityRoutesToOpenGate() {
        // Arrange
        TransponderGateActionDTO transponderGateActionDTO = new TransponderGateActionDTO(PLATE_NUMBER, LOT_ID, SPOT_ID);
        GateActionDTO sendingGateActionDTO = new GateActionDTO(true, LOT_ID, SPOT_ID);

        // Act
        exitGateService.receiveTransponderGateActivity(transponderGateActionDTO);

        // Assert
        verify(forwardGateAction, times(1)).sendGateAction(sendingGateActionDTO);
    }

    @Test
    public void testReceiveVoucherGateActivity() {
        // Arrange
        VoucherGateActionDTO voucherGateActionDTO = new VoucherGateActionDTO(
                PLATE_NUMBER, LOT_ID, SPOT_ID, CC_NUMBER, CC_EXPIRY, CC_CVC
        );
        VisitorGateActionDTO voucherAsVisitorDTO = new VisitorGateActionDTO(
                PLATE_NUMBER, LOT_ID, SPOT_ID, 0, CC_NUMBER, CC_EXPIRY, CC_CVC, "VISITOR_EXIT"
        );

        // Act
        exitGateService.receiveVoucherGateActivity(voucherGateActionDTO);

        // Assert
        verify(validateVoucherFines, times(1)).sendVoucherValidationForFines(voucherAsVisitorDTO);
    }

    @Test
    public void testReceiveVisitorGateActivity() {
        // Arrange
        VisitorGateActionDTO visitorGateActionDTO = new VisitorGateActionDTO(
                PLATE_NUMBER, LOT_ID, SPOT_ID, 3, CC_NUMBER, CC_EXPIRY, CC_CVC, ""
        );
        VisitorGateActionDTO sendingVisitorGateActionDTO = new VisitorGateActionDTO(
                PLATE_NUMBER, LOT_ID, SPOT_ID, 3, CC_NUMBER, CC_EXPIRY, CC_CVC, "VISITOR_EXIT"
        );

        // Act
        exitGateService.receiveVisitorGateActivity(visitorGateActionDTO);

        // Assert
        verify(validateVisitorExit, times(1)).sendVisitorExitValidationRequest(sendingVisitorGateActionDTO);
    }

    @Test
    public void testForwardValidationToGateSuccess() {
        // Arrange
        GateActionDTO gateActionDTO = new GateActionDTO(true, LOT_ID, SPOT_ID);
        UpdateLotStatisticsDTO sendingUpdateLotStatisticsDTO = new UpdateLotStatisticsDTO(SPOT_ID, false);

        // Act
        exitGateService.forwardValidationToGate(gateActionDTO);

        // Assert
        verify(forwardGateAction, times(1)).sendGateAction(gateActionDTO);
        verify(lotStatistics, times(1)).updateExitLotStatistics(sendingUpdateLotStatisticsDTO);
    }

    @Test
    public void testForwardValidationToGateFailure() {
        // Arrange
        GateActionDTO gateActionDTO = new GateActionDTO(false, LOT_ID, SPOT_ID);

        // Act
        exitGateService.forwardValidationToGate(gateActionDTO);

        // Assert
        verify(forwardGateAction, times(1)).sendGateAction(gateActionDTO);
        verify(lotStatistics, times(0)).updateExitLotStatistics(any());
    }
}