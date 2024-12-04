package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.NotifyFineDTO;
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
public class AMQPNotifyFinesSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPNotifyFinesSender amqpNotifyFinesSender;

    @Test
    void testSendNotifyFines() {
        // Arrange
        NotifyFineDTO notifyFineDTO = new NotifyFineDTO(PLATE_NUMBER, true);
        String routingKey = "fines.update";
        String message = translate(notifyFineDTO);

        // Act
        amqpNotifyFinesSender.sendFineNotification(notifyFineDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}
