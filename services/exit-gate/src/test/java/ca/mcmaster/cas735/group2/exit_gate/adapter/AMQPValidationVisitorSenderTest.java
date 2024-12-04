package ca.mcmaster.cas735.group2.exit_gate.adapter;

import ca.mcmaster.cas735.group2.exit_gate.adapater.AMQPValidationVisitorSender;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_CVC;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_EXPIRY;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_NUMBER;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.translate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPValidationVisitorSenderTest {


    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPValidationVisitorSender amqpValidateVoucherFinesSender;

    @Test
    void testSendUpdateValidationForVisitor() {
        // Arrange
        VisitorGateActionDTO visitorGateActionDTO = new VisitorGateActionDTO(
                PLATE_NUMBER, LOT_ID, SPOT_ID, 3, CC_NUMBER, CC_EXPIRY, CC_CVC, "VISITOR_EXIT"
        );
        String routingKey = "payment.activity.request";
        String message = translate(visitorGateActionDTO);

        // Act
        amqpValidateVoucherFinesSender.sendVisitorExitValidationRequest(visitorGateActionDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}
