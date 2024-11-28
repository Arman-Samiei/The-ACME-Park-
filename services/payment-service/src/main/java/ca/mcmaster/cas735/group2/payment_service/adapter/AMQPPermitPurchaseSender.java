package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.PermitOrderDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.PermitPurchase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPPermitPurchaseSender implements PermitPurchase {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPPermitPurchaseSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    public void processPermitPurchase(PermitOrderDTO permitOrderDTO) {
        log.info("Sending permit purchase: {}", permitOrderDTO);
        rabbitTemplate.convertAndSend(exchange, "permit.purchase", permitOrderDTO);
    }
}
