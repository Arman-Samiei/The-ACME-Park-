package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateRequestForLotDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVisitorEntry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPVisitorValidationSender implements ValidateVisitorEntry {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPVisitorValidationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendVisitorEntryValidationRequest(VisitorGateRequestForLotDTO visitorGateRequestForLotDTO) {
        log.info("Asking validation for visitor {}", visitorGateRequestForLotDTO);
        rabbitTemplate.convertAndSend(exchange, "spot.availability.request", translate(visitorGateRequestForLotDTO));
    }

    private String translate(VisitorGateRequestForLotDTO visitorGateRequestForLotDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(visitorGateRequestForLotDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
