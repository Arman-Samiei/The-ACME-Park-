package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitValidationResponseData;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitValidationResponse;
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
public class PermitValidationResponseAdapter implements PermitValidationResponse {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public PermitValidationResponseAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendValidationResult(PermitValidationResponseData permitValidationResponseData) {
        log.debug("Sending message to {}: {}", exchange, permitValidationResponseData);
        rabbitTemplate.convertAndSend(exchange, "permit.entry.validation.response", translate(permitValidationResponseData));
    }


    @Bean(name = "validationOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(PermitValidationResponseData permitValidationResponseData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(permitValidationResponseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}


