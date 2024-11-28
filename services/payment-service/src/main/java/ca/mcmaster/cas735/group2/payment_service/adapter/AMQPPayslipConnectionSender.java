package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.PayslipConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPPayslipConnectionSender implements PayslipConnection {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPPayslipConnectionSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void processPayment(PaymentRequestDTO paymentRequestDTO) {
        log.info("Sending uni payment for {}", paymentRequestDTO.id());
        rabbitTemplate.convertAndSend(exchange, "uni.payment", paymentRequestDTO);
    }
}
