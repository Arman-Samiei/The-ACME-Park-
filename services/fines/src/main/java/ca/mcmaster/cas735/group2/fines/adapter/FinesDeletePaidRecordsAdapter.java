package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesDeletePaidRecordsData;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesDeletePaidRecords;
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
public class FinesDeletePaidRecordsAdapter {
    private final FinesDeletePaidRecords finesDeletePaidRecords;

    @Autowired
    public FinesDeletePaidRecordsAdapter(FinesDeletePaidRecords finesDeletePaidRecords) {
        this.finesDeletePaidRecords = finesDeletePaidRecords;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.fines.update.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "fines.update"))
    public void listen(String data) {
        log.debug("Receiving issuance request {}", data);
        finesDeletePaidRecords.deleteRecords(translate(data));

    }

    private FinesDeletePaidRecordsData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, FinesDeletePaidRecordsData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
