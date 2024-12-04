package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.VisitorExit;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPVisitorExitSender implements VisitorExit {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPVisitorExitSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    public void processVisitorExit(GateActionDTO gateActionDTO) {
        log.info("Sending exit action: {}", gateActionDTO);
        rabbitTemplate.convertAndSend(exchange, "gate.exit.action", translate(gateActionDTO));
    }

    private String translate(GateActionDTO gateActionDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(gateActionDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
