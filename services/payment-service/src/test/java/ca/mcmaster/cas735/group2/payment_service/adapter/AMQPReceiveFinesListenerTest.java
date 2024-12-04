package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.ExistingFinesDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.ReceiveFines;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static ca.mcmaster.cas735.group2.payment_service.TestUtils.ORDER_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.convertMapToJson;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPReceiveFinesListenerTest {

    @Mock
    private ReceiveFines receiveFines;

    @Mock
    private Channel channel;

    @InjectMocks
    private AMQPReceiveFinesListener amqpReceiveFinesListener;

    @Test
    public void testReceiveFines() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "id", ORDER_ID,
                "plateNumber", PLATE_NUMBER,
                "fineAmount", "105.10"
        ));
        ExistingFinesDTO existingFinesDTO = new ObjectMapper().readValue(data, ExistingFinesDTO.class);

        // Act
        amqpReceiveFinesListener.receive(data, channel);

        // Assert
        verify(receiveFines, times(1)).commitOrderAndRoute(existingFinesDTO);
    }
}
