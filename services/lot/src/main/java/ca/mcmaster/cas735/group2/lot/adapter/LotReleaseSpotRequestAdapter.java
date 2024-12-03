package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.ports.provided.LotReleaseSpot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LotReleaseSpotRequestAdapter {
    private final LotReleaseSpot lotReleaseSpot;

    @Autowired
    public LotReleaseSpotRequestAdapter(LotReleaseSpot lotReleaseSpot) {
        this.lotReleaseSpot = lotReleaseSpot;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "spot.release.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "spot.release.request"))
    public void listen(String plateNumber) {
        log.debug("Receiving spot availability check request {}", plateNumber);
        lotReleaseSpot.releaseSpot(plateNumber);

    }
}
