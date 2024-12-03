package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitIssuanceRequestData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitIssuanceRequest;
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
public class PermitIssuanceRequestAdapter {
    private final PermitIssuanceRequest permitIssuanceRequest;

    @Autowired
    public PermitIssuanceRequestAdapter(PermitIssuanceRequest permitIssuanceRequest) {
        this.permitIssuanceRequest = permitIssuanceRequest;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "permit.issue.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "permit.issue.request"))
    public void listen(String data) {
        log.debug("Receiving issuance request {}", data);
        permitIssuanceRequest.issue(translate(data));

    }


    private PermitIssuanceRequestData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
                return mapper.readValue(raw, PermitIssuanceRequestData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
