package ca.mcmaster.cas735.group2.entry_gate.adapter;

import ca.mcmaster.cas735.group2.entry_gate.adapater.AMQPValidationResponseHandler;
import ca.mcmaster.cas735.group2.entry_gate.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidationResponseHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.convertMapToJson;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPValidationResponseHandlerTest {

    @Mock
    private ValidationResponseHandler validationResponseHandler;
    @Mock
    private Channel channel;

    @InjectMocks
    private AMQPValidationResponseHandler amqpValidationResponseHandler;

    @Test
    void testReceiveNonVisitorOpenEntryAction() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "shouldOpen", "true",
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "isVisitor", "false"
        ));
        GateActionDTO gateActionDTO = new ObjectMapper().readValue(data, GateActionDTO.class);

        // Act
        amqpValidationResponseHandler.receive(data, channel);

        // Assert
        verify(validationResponseHandler, times(1)).forwardValidationToGate(gateActionDTO);
    }

    @Test
    void testReceiveVisitorOpenEntryAction() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "shouldOpen", "true",
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "isVisitor", "true"
        ));
        GateActionDTO gateActionDTO = new ObjectMapper().readValue(data, GateActionDTO.class);

        // Act
        amqpValidationResponseHandler.receive(data, channel);

        // Assert
        verify(validationResponseHandler, times(1)).forwardValidationToGate(gateActionDTO);
    }

    @Test
    void testReceiveNonVisitorKeepClosedEntryAction() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "shouldOpen", "false",
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "isVisitor", "false"
        ));
        GateActionDTO gateActionDTO = new ObjectMapper().readValue(data, GateActionDTO.class);

        // Act
        amqpValidationResponseHandler.receive(data, channel);

        // Assert
        verify(validationResponseHandler, times(1)).forwardValidationToGate(gateActionDTO);
    }

    @Test
    void testReceiveVisitorKeepClosedEntryAction() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "shouldOpen", "false",
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "isVisitor", "true"
        ));
        GateActionDTO gateActionDTO = new ObjectMapper().readValue(data, GateActionDTO.class);

        // Act
        amqpValidationResponseHandler.receive(data, channel);

        // Assert
        verify(validationResponseHandler, times(1)).forwardValidationToGate(gateActionDTO);
    }
}
