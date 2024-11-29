package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityResponseData;
import ca.mcmaster.cas735.group2.lot.ports.required.LotAvailabilityCheckResponse;
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
public class LotAvailabilityCheckResponseAdapter implements LotAvailabilityCheckResponse {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public LotAvailabilityCheckResponseAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendAvailableSpot(LotAvailabilityResponseData lotAvailabilityResponseData, String responseReceiver) {
        log.debug("Sending message to {}: {}", exchange, lotAvailabilityResponseData);
        rabbitTemplate.convertAndSend(exchange, responseReceiver + ".lot.response", translate(lotAvailabilityResponseData));
    }


    @Bean(name = "lotOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(LotAvailabilityResponseData lotAvailabilityResponseData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(lotAvailabilityResponseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
