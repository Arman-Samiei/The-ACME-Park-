package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.PermitOrderDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static ca.mcmaster.cas735.group2.payment_service.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.translate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPPermitPurchaseSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPPermitPurchaseSender amqpPermitPurchaseSender;

    @Test
    void testSendPaymentPurchaseResponse() {
        // Arrange
        PermitOrderDTO permitOrderDTO = new PermitOrderDTO(PLATE_NUMBER, true);
        String routingKey = "permit.payment.response";
        String message = translate(permitOrderDTO);

        // Act
        amqpPermitPurchaseSender.processPermitPurchase(permitOrderDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}
