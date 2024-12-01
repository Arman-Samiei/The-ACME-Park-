package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationResponseData;
import ca.mcmaster.cas735.group2.fines.ports.required.FinesCalculationResponse;
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
public class FinesCalculationResponseAdapter implements FinesCalculationResponse {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public FinesCalculationResponseAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendResponse(FinesCalculationResponseData finesCalculationResponseData) {
        log.debug("Sending message to {}: {}", exchange, finesCalculationResponseData);
        rabbitTemplate.convertAndSend(exchange, "fines", translate(finesCalculationResponseData));
    }


    @Bean(name = "finesOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(FinesCalculationResponseData finesCalculationResponseData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(finesCalculationResponseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
