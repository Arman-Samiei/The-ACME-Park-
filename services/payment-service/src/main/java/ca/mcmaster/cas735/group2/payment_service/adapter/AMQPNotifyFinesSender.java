package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.NotifyFineDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.NotifyFines;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPNotifyFinesSender implements NotifyFines {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPNotifyFinesSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendFineNotification(NotifyFineDTO notifyFineDTO) {
        log.info("Telling fine was paid: {}", notifyFineDTO);
        rabbitTemplate.convertAndSend(exchange, "fines.update", translate(notifyFineDTO));
    }

    private String translate(NotifyFineDTO notifyFineDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(notifyFineDTO);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
