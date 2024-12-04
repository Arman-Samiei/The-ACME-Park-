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
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateTransponderEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVisitorEntry;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVoucherEntry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.SPOT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EntryGateServiceImplTest {

    @Mock
    private ValidateTransponderEntry validateTransponderEntry;
    @Mock
    private ValidateVoucherEntry validateVoucherEntry;
    @Mock
    private ValidateVisitorEntry validateVisitorEntry;
    @Mock
    private LotStatistics lotStatistics;
    @Mock
    private ForwardGateAction forwardGateAction;

    @InjectMocks
    private EntryGateServiceImpl entryGateService;

    @Test
    public void testReceiveTransponderGateActivityRoutesToValidateTransponderEntry() {
        // Arrange
        TransponderGateActionDTO transponderGateActionDTO = new TransponderGateActionDTO(PLATE_NUMBER, LOT_ID);

        // Act
        entryGateService.receiveTransponderGateActivity(transponderGateActionDTO);

        // Assert
        verify(validateTransponderEntry, times(1)).sendTransponderEntryValidationRequest(transponderGateActionDTO);
    }

    @Test
    public void testReceiveVoucherGateActivityRoutesToValidateVoucherEntry() {
        // Arrange
        VoucherGateActionDTO voucherGateActionDTO = new VoucherGateActionDTO(PLATE_NUMBER, LOT_ID);

        // Act
        entryGateService.receiveVoucherGateActivity(voucherGateActionDTO);

        // Assert
        verify(validateVoucherEntry, times(1)).sendVoucherEntryValidationRequest(voucherGateActionDTO);
    }

    @Test
    public void testReceiveVisitorGateActivityRoutesToVisitorRequestForLot() {
        // Arrange
        VisitorGateActionDTO visitorGateActionDTO = new VisitorGateActionDTO(PLATE_NUMBER, LOT_ID);
        VisitorGateRequestForLotDTO visitorGateRequestForLotDTO = new VisitorGateRequestForLotDTO(LOT_ID, "visitor", PLATE_NUMBER);

        // Act
        entryGateService.receiveVisitorGateActivity(visitorGateActionDTO);

        // Assert
        verify(validateVisitorEntry, times(1)).sendVisitorEntryValidationRequest(visitorGateRequestForLotDTO);
    }

    @Test
    public void testSendVisitorLotResponseIsSuccess() {
        // Arrange
        VisitorGateLotResponseDTO visitorGateLotResponseDTO = new VisitorGateLotResponseDTO(LOT_ID, SPOT_ID, PLATE_NUMBER);
        GateActionDTO responseGateActionDTO = new GateActionDTO(true, LOT_ID, SPOT_ID, true);
        UpdateLotStatisticsDTO responseUpdateLotStatisticsDTO = new UpdateLotStatisticsDTO(SPOT_ID, true);

        // Act
        entryGateService.sendVisitorLotResponse(visitorGateLotResponseDTO);

        // Assert
        verify(forwardGateAction, times(1)).sendGateAction(responseGateActionDTO);
        verify(lotStatistics, times(1)).updateEntryLotStatistics(responseUpdateLotStatisticsDTO);
    }

    @Test
    public void testSendVisitorLotResponseIsFailure() {
        // Arrange
        VisitorGateLotResponseDTO visitorGateLotResponseDTO = new VisitorGateLotResponseDTO(LOT_ID, "", PLATE_NUMBER);
        GateActionDTO responseGateActionDTO = new GateActionDTO(false, LOT_ID, "", true);

        // Act
        entryGateService.sendVisitorLotResponse(visitorGateLotResponseDTO);

        // Assert
        verify(forwardGateAction, times(1)).sendGateAction(responseGateActionDTO);
        verify(lotStatistics, times(0)).updateEntryLotStatistics(any());
    }

    @Test
    public void testForwardValidationToGateSuccess() {
        // Arrange
        GateActionDTO gateActionDTO = new GateActionDTO(true, LOT_ID, SPOT_ID, false);
        UpdateLotStatisticsDTO responseUpdateLotStatisticsDTO = new UpdateLotStatisticsDTO(SPOT_ID, true);

        // Act
        entryGateService.forwardValidationToGate(gateActionDTO);

        // Assert
        verify(forwardGateAction, times(1)).sendGateAction(gateActionDTO);
        verify(lotStatistics, times(1)).updateEntryLotStatistics(responseUpdateLotStatisticsDTO);
    }

    @Test
    public void testForwardValidationToGateFailure() {
        // Arrange
        GateActionDTO gateActionDTO = new GateActionDTO(false, LOT_ID, SPOT_ID, false);

        // Act
        entryGateService.forwardValidationToGate(gateActionDTO);

        // Assert
        verify(forwardGateAction, times(1)).sendGateAction(gateActionDTO);
        verify(lotStatistics, times(0)).updateEntryLotStatistics(any());
    }
}