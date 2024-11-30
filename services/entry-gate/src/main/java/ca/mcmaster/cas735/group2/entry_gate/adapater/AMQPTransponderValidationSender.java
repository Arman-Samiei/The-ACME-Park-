package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateTransponderEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPTransponderValidationSender implements ValidateTransponderEntry {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPTransponderValidationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendTransponderEntryValidationRequest(TransponderGateActionDTO transponderGateActionDTO) {
        log.info("Asking validation for transponder {}", transponderGateActionDTO);
        rabbitTemplate.convertAndSend(exchange, "permit.entry.validation", transponderGateActionDTO);
    }

    @Bean
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }
}
