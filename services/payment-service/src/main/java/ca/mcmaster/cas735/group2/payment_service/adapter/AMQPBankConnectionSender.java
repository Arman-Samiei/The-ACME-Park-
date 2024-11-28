package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.BankConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPBankConnectionSender implements BankConnection {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPBankConnectionSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void processPayment(PaymentRequestDTO paymentRequestDTO) {
        log.info("Sending bank payment for {}", paymentRequestDTO.id());
        rabbitTemplate.convertAndSend(exchange, "bank.payment", paymentRequestDTO);
    }

    @Bean
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }
}
