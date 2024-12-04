package ca.mcmaster.cas735.group2.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PermitIssuanceResponse {
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "permit.issue.response.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange}", type = "topic"),
            key = "permit.issue.response"))
    public void listen(String data) {
        log.info("Receiving issuance response: {}", data);
    }
}
