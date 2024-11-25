package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateTransponderExit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPTransponderValidationSender implements ValidateTransponderExit {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPTransponderValidationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendTransponderExitValidationRequest(String transponderId) {
        log.info("Asking validation for transponder {}", transponderId);
        rabbitTemplate.convertAndSend(exchange, "permit.exit.validation", transponderId);
    }

    @Bean
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }
}
