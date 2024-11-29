package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.dto.LotOccupationStatusUpdateData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotOccupationStatusUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class LotStatusUpdateRequestAdapter {

    private final LotOccupationStatusUpdate lotOccupationStatusUpdate;

    @Autowired
    public LotStatusUpdateRequestAdapter(LotOccupationStatusUpdate lotOccupationStatusUpdate) {
        this.lotOccupationStatusUpdate = lotOccupationStatusUpdate;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.lot.update.status.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "lot.update.status.request"))
    public void listen(String data) {
        log.debug("Receiving status updating request {}", data);
        lotOccupationStatusUpdate.updateSpotOccupationStatus(translate(data));

    }

    private LotOccupationStatusUpdateData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, LotOccupationStatusUpdateData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
