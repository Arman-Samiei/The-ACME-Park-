package ca.mcmaster.cas735.group2.exit_gate.adapter;

import ca.mcmaster.cas735.group2.exit_gate.adapater.AMQPValidateVoucherFinesSender;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static ca.mcmaster.cas735.group2.exit_gate.TestUtils.CC_CVV;
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
public class AMQPValidateVoucherFinesSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPValidateVoucherFinesSender amqpValidateVoucherFinesSender;

    @Test
    void testSendValidationForVoucherFines() {
        // Arrange
        VoucherGateActionDTO voucherGateActionDTO = new VoucherGateActionDTO(
                PLATE_NUMBER, LOT_ID, SPOT_ID, CC_NUMBER, CC_EXPIRY, CC_CVV
        );
        String routingKey = "payment.activity.request";
        String message = translate(voucherGateActionDTO);

        // Act
        amqpValidateVoucherFinesSender.sendVoucherValidationForFines(voucherGateActionDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}
