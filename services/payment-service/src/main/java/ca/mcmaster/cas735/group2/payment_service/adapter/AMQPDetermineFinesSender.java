package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.FinesRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.DetermineFines;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPDetermineFinesSender implements DetermineFines {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPDetermineFinesSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void requestFineAmount(FinesRequestDTO finesRequestDTO) {
        log.info("Checking fines for: {}", finesRequestDTO);
        rabbitTemplate.convertAndSend(exchange, "fines.payment.request", translate(finesRequestDTO));
    }

    private String translate(FinesRequestDTO finesRequestDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(finesRequestDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
