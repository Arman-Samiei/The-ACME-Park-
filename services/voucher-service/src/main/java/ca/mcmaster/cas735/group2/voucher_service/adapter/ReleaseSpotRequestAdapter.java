package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.ports.required.ReleaseSpotRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReleaseSpotRequestAdapter implements ReleaseSpotRequest {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public ReleaseSpotRequestAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendReleaseSpotRequest(String plateNumber) {
        log.debug("Sending message to {}: {}", exchange, plateNumber);
        rabbitTemplate.convertAndSend(exchange, "spot.release.request", plateNumber);
    }


    @Bean(name = "voucherReleaseSpotRequestOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

}
