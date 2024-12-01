package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.ValidateVisitorExit;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPVisitorValidationSender implements ValidateVisitorExit {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPVisitorValidationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendVisitorExitValidationRequest(VisitorGateActionDTO visitorGateActionDTO) {
        log.info("Asking validation for visitor {}", visitorGateActionDTO);
        rabbitTemplate.convertAndSend(exchange, "payment.activity.request", translate(visitorGateActionDTO));
    }

    private String translate(VisitorGateActionDTO visitorGateActionDTO) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(visitorGateActionDTO);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Bean
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }
}
