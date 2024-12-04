package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.GateActionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static ca.mcmaster.cas735.group2.payment_service.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.translate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPVisitorExitSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPVisitorExitSender amqpVisitorExitSender;

    @Test
    void testSendVisitorExitResponse() {
        // Arrange
        GateActionDTO gateActionDTO = new GateActionDTO(true, LOT_ID, SPOT_ID);
        String routingKey = "gate.exit.action";
        String message = translate(gateActionDTO);

        // Act
        amqpVisitorExitSender.processVisitorExit(gateActionDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}
