package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitLotRequestData;
import ca.mcmaster.cas735.group2.permit.ports.required.LotRequest;
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
public class LotRequestAdapter implements LotRequest {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public LotRequestAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void requestSpot(PermitLotRequestData permitLotRequestData) {
        log.debug("Sending message to {}: {}", exchange, permitLotRequestData);
        rabbitTemplate.convertAndSend(exchange, "permit.lot.request", translate(permitLotRequestData));
    }


    @Bean(name = "lotOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(PermitLotRequestData permitLotRequestData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(permitLotRequestData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
