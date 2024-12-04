package ca.mcmaster.cas735.group2.entry_gate.adapter;

import ca.mcmaster.cas735.group2.entry_gate.adapater.AMQPVisitorValidationSender;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateRequestForLotDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.translate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPVisitorValidationSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPVisitorValidationSender amqpVisitorValidationSender;

    @Test
    void testSendVisitorEntryValidationRequest() {
        // Arrange
        VisitorGateRequestForLotDTO visitorGateRequestForLotDTO = new VisitorGateRequestForLotDTO(LOT_ID, "visitor", PLATE_NUMBER);
        String routingKey = "spot.availability.request";
        String message = translate(visitorGateRequestForLotDTO);

        // Act
        amqpVisitorValidationSender.sendVisitorEntryValidationRequest(visitorGateRequestForLotDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}
