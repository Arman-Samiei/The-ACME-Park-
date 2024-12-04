package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.dto.UpdateLotStatisticsDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.LotStatistics;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    public void updateExitLotStatistics(UpdateLotStatisticsDTO updateLotStatisticsDTO) {
        log.info("Updating statistics {}", updateLotStatisticsDTO);
        rabbitTemplate.convertAndSend(exchange, "lot.update.status.request", translate(updateLotStatisticsDTO));
    }

    private String translate(UpdateLotStatisticsDTO updateLotStatisticsDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(updateLotStatisticsDTO);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
