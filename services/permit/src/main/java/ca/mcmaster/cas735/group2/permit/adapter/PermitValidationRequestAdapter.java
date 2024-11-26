package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitValidationRequestData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitValidationRequest;
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
public class PermitValidationRequestAdapter {

    private final PermitValidationRequest permitValidationRequest;

    @Autowired
    public PermitValidationRequestAdapter(PermitValidationRequest permitValidationRequest) {
        this.permitValidationRequest = permitValidationRequest;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.permit.entry.validation.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "permit.entry.validation"))
    public void listen(String data) {
        log.debug("Receiving validation request {}", data);
        permitValidationRequest.validate(translate(data));

    }


    private PermitValidationRequestData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, PermitValidationRequestData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
