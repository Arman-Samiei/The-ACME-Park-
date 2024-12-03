package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.ports.required.PermitIssuanceResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class PermitIssuanceResponseAdapter implements PermitIssuanceResponse {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public PermitIssuanceResponseAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendPermitIssuanceResponse(String response){
        log.debug("Sending message to {}: {}", exchange, response);
        rabbitTemplate.convertAndSend(exchange, "permit.issue.response", translate(response));
    }


    @Bean(name = "permitIssuanceResponseOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(String response) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
