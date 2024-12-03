package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationResponseData;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherValidationResponse;
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
public class VoucherValidationResponseAdapter implements VoucherValidationResponse {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange;

    @Autowired
    public VoucherValidationResponseAdapter(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendValidationResult(VoucherValidationResponseData voucherValidationResponseData) {
        log.debug("Sending message to {}: {}", exchange, voucherValidationResponseData);
        rabbitTemplate.convertAndSend(exchange, "gate.entry.action", translate(voucherValidationResponseData));
    }


    @Bean(name = "voucherValidationResponseOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(VoucherValidationResponseData voucherValidationResponseData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(voucherValidationResponseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}


