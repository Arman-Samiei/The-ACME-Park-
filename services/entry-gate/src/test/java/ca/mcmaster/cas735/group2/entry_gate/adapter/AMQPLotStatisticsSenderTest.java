package ca.mcmaster.cas735.group2.entry_gate.adapter;

import ca.mcmaster.cas735.group2.entry_gate.adapater.AMQPLotStatisticsSender;
import ca.mcmaster.cas735.group2.entry_gate.dto.UpdateLotStatisticsDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.entry_gate.TestUtils.translate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPLotStatisticsSenderTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private AMQPLotStatisticsSender amqpLotStatisticsSender;

    @Test
    void testSendUpdateEntryLot() {
        // Arrange
        UpdateLotStatisticsDTO updateLotStatisticsDTO = new UpdateLotStatisticsDTO(SPOT_ID, true);
        String routingKey = "lot.update.status.request";
        String message = translate(updateLotStatisticsDTO);

        // Act
        amqpLotStatisticsSender.updateEntryLotStatistics(updateLotStatisticsDTO);

        // Assert
        verify(rabbitTemplate, times(1)).convertAndSend(any(), eq(routingKey), eq(message));
    }
}
