package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.dto.UpdateLotStatisticsDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.LotStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPLotStatisticsSender implements LotStatistics {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPLotStatisticsSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void updateEntryLotStatistics(UpdateLotStatisticsDTO updateLotStatisticsDTO) {
        log.info("Updating statistics {}", updateLotStatisticsDTO);
        rabbitTemplate.convertAndSend(exchange, "lot.update.status.request", updateLotStatisticsDTO);
    }
}
