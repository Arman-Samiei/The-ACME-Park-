package ca.mcmaster.cas735.group2.exit_gate.adapter;

import ca.mcmaster.cas735.group2.exit_gate.TestUtils;
import ca.mcmaster.cas735.group2.exit_gate.adapater.AMQPGateExitListener;
import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.VoucherGateActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_CVC;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_EXPIRY;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_NUMBER;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.SPOT_ID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPGateExitListenerTest {

    @Mock
    private TransponderGateActivity transponderGateActivity;
    @Mock
    private VisitorGateActivity visitorGateActivity;
    @Mock
    private VoucherGateActivity voucherGateActivity;
    @Mock
    private Channel channel;

    @InjectMocks
    private AMQPGateExitListener amqpGateExitListener;

    @Test
    void testReceiveTransponderSuccess() throws JsonProcessingException {
        // Arrange
        String data = TestUtils.convertMapToJson(Map.of(
                "plateNumber", PLATE_NUMBER,
                "lotID", LOT_ID
        ));
        TransponderGateActionDTO transponderGateActionDTO = new ObjectMapper().readValue(data, TransponderGateActionDTO.class);

        // Act
        amqpGateExitListener.receiveTransponder(data, channel);

        // Assert
        verify(transponderGateActivity, times(1)).receiveTransponderGateActivity(transponderGateActionDTO);
    }

    @Test
    void testReceiveVisitor() throws JsonProcessingException {
        // Arrange
        String data = TestUtils.convertMapToJson(Map.of(
                "plateNumber", PLATE_NUMBER,
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "hoursOccupied", "3",
                "ccNumber", CC_NUMBER,
                "ccExpiry", CC_EXPIRY,
                "ccCVC", CC_CVC
        ));
        VisitorGateActionDTO visitorGateActionDTO = new ObjectMapper().readValue(data, VisitorGateActionDTO.class);

        // Act
        amqpGateExitListener.receiveVisitor(data, channel);

        // Assert
        verify(visitorGateActivity, times(1)).receiveVisitorGateActivity(visitorGateActionDTO);
    }

    @Test
    void testReceiveVoucher() throws JsonProcessingException {
        // Arrange
        String data = TestUtils.convertMapToJson(Map.of(
                "plateNumber", PLATE_NUMBER,
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "ccNumber", CC_NUMBER,
                "ccExpiry", CC_EXPIRY,
                "ccCVC", CC_CVC
        ));
        VoucherGateActionDTO voucherGateActionDTO = new ObjectMapper().readValue(data, VoucherGateActionDTO.class);

        // Act
        amqpGateExitListener.receiveVoucher(data, channel);

        // Assert
        verify(voucherGateActivity, times(1)).receiveVoucherGateActivity(voucherGateActionDTO);
    }
}