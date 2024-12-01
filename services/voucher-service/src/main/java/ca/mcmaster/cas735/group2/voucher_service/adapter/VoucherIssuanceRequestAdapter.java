package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherIssuanceRequestData;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.VoucherIssuanceRequest;
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
public class VoucherIssuanceRequestAdapter {
    private final VoucherIssuanceRequest voucherIssuanceRequest;

    @Autowired
    public VoucherIssuanceRequestAdapter(VoucherIssuanceRequest voucherIssuanceRequest) {
        this.voucherIssuanceRequest = voucherIssuanceRequest;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.permit.issue.request.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "voucher.issue.request"))
    public void listen(String data) {
        log.debug("Receiving issuance request {}", data);
        voucherIssuanceRequest.issue(translate(data));

    }


    private VoucherIssuanceRequestData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, VoucherIssuanceRequestData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
