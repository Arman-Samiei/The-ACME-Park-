package ca.mcmaster.cas735.Group2.Voucher.adapter;

import ca.mcmaster.cas735.Group2.Voucher.dto.FilterRequest;
import ca.mcmaster.cas735.Group2.Voucher.ports.Issue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service @Slf4j
public class VoucherIssuanceListener {

    private static final String REGISTER = "register";
    private static final String UNREGISTER = "unregister";

    private final Issue manager;

    @Autowired
    public VoucherIssuanceListener(Issue manager) {
        this.manager = manager;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "Voucher.filter.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.inbound-exchange-topic}",
                                 ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    public void listen(String data) {
        log.debug("Receiving filtering request {}", data);
        FilterRequest request = translate(data);
        switch (request.getVerb().toLowerCase()) {
            case REGISTER:
                manager.register(request.getParameter());
                break;
            case UNREGISTER:
                manager.unregister(request.getParameter());
                break;
            default:
                throw new IllegalArgumentException(request.getVerb());
        }
    }


    private FilterRequest translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, FilterRequest.class);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
