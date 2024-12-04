package ca.mcmaster.cas735.group2.entry_gate.adapter;

import ca.mcmaster.cas735.group2.entry_gate.adapater.AMQPVisitorLotResponseListener;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateLotResponseDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.VisitorLotResponse;
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
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.convertMapToJson;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPVisitorLotResponseListenerTest {

    @Mock
    private VisitorLotResponse visitorLotResponse;
    @Mock
    private Channel channel;

    @InjectMocks
    private AMQPVisitorLotResponseListener amqpVisitorLotResponseListener;

    @Test
    void testReceiveLotResponseForVisitor() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "plateNumber", PLATE_NUMBER
        ));
        VisitorGateLotResponseDTO visitorGateLotResponseDTO = new ObjectMapper().readValue(data, VisitorGateLotResponseDTO.class);

        // Act
        amqpVisitorLotResponseListener.receive(data, channel);

        // Assert
        verify(visitorLotResponse, times(1)).sendVisitorLotResponse(visitorGateLotResponseDTO);
    }
}
