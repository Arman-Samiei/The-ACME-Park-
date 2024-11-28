package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitLotResponseData;
import ca.mcmaster.cas735.group2.permit.ports.provided.LotResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class LotResponseAdapter {
    private final LotResponse lotResponse;

    @Autowired
    LotResponseAdapter(LotResponse lotResponse) {
        this.lotResponse = lotResponse;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.permit.lot.response.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "permit.lot.response"))
    public void listen(String data) {
        log.debug("Receiving lot response {}", data);
        lotResponse.reserveSpot(translate(data));

    }


    private PermitLotResponseData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, PermitLotResponseData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
