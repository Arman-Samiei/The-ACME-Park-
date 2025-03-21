package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationRequestData;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesCalculator;
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
public class FinesCalculationRequestAdapter {
    private final FinesCalculator finesCalculator;

    @Autowired
    public FinesCalculationRequestAdapter(FinesCalculator finesCalculator) {
        this.finesCalculator = finesCalculator;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "fines.payment.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "fines.payment.request"))
    public void listen(String data) {
        log.debug("Receiving issuance request {}", data);
        finesCalculator.calculateTotalFine(translate(data));

    }

    private FinesCalculationRequestData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, FinesCalculationRequestData.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}


