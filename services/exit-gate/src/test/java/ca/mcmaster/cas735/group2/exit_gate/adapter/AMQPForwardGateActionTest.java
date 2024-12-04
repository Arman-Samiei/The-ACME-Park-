package ca.mcmaster.cas735.group2.exit_gate.adapter;

import ca.mcmaster.cas735.group2.exit_gate.adapater.AMQPForwardGateAction;
import ca.mcmaster.cas735.group2.exit_gate.dto.GateActionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.translate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPForwardGateActionTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPForwardGateAction amqpForwardGateAction;

    @Test
    void testSendGateAction() {
        // Arrange
        GateActionDTO gateActionDTO = new GateActionDTO(true, LOT_ID, SPOT_ID);
        String routingKey = gateActionDTO.lotID() + ".exit.action";
        String message = translate(gateActionDTO);

        // Act
        amqpForwardGateAction.sendGateAction(gateActionDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}