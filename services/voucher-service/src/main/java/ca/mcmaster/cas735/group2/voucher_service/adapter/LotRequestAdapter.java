package ca.mcmaster.cas735.group2.voucher_service.adapter;


import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotRequestData;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.LotRequest;
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
    public void requestSpot(VoucherLotRequestData voucherLotRequestData) {
        log.debug("Sending message to {}: {}", exchange, voucherLotRequestData);
        rabbitTemplate.convertAndSend(exchange, "spot.availability.request", translate(voucherLotRequestData));
    }


    @Bean(name = "voucherLotRequestOutbound")
    public TopicExchange outbound() {
        // this will create the outbound exchange if it does not exist
        return new TopicExchange(exchange);
    }

    private String translate(VoucherLotRequestData voucherLotRequestData) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(voucherLotRequestData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
