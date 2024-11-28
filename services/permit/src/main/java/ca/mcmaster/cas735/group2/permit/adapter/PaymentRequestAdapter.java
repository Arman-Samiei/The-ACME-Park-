package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PaymentRequestData;
import ca.mcmaster.cas735.group2.permit.ports.required.PaymentRequest;
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
public class PaymentRequestAdapter implements PaymentRequest {
    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public PaymentRequestAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void requestPayment(PaymentRequestData paymentRequestData) {
        log.debug("Sending message to {}: {}", exchange, paymentRequestData);
        rabbitTemplate.convertAndSend(exchange, "permit.payment.request", translate(paymentRequestData));
    }


    @Bean(name = "paymentOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(PaymentRequestData paymentRequestData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(paymentRequestData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
