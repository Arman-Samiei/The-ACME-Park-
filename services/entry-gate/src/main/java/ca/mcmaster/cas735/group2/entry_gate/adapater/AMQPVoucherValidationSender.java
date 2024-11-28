package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.ports.ValidateVoucherEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPVoucherValidationSender implements ValidateVoucherEntry {

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public AMQPVoucherValidationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${app.exchange}")
    private String exchange;

    @Override
    public void sendVoucherEntryValidationRequest(String voucherId) {
        log.info("Asking validation for voucher {}", voucherId);
        rabbitTemplate.convertAndSend(exchange, "voucher.entry.validation", voucherId);
    }

//    @Bean
//    public TopicExchange outbound() {
//        // this will create the outbound exchange if it does not exist
//        return new TopicExchange(exchange);
//    }
}
