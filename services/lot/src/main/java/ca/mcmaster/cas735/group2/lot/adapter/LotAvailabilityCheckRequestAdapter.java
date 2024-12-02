package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityRequestData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotAvailabilityCheckRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LotAvailabilityCheckRequestAdapter {

    private final LotAvailabilityCheckRequest lotAvailabilityCheckRequest;

    @Autowired
    public LotAvailabilityCheckRequestAdapter(LotAvailabilityCheckRequest lotAvailabilityCheckRequest) {
        this.lotAvailabilityCheckRequest = lotAvailabilityCheckRequest;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.spot.availability.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "spot.availability.request"))
    public void listen(String data) {
        log.debug("Receiving spot availability check request {}", data);
        lotAvailabilityCheckRequest.checkLotAvailability(translate(data));

    }


    private LotAvailabilityRequestData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, LotAvailabilityRequestData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
