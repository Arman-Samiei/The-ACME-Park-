package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.ports.LotStatistics;
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
    public void updateExitLotStatistics(String lot) {
        log.info("Updating statistics for lot {}", lot);
        rabbitTemplate.convertAndSend(exchange, "statistics.lot.exit", lot);
    }
}
