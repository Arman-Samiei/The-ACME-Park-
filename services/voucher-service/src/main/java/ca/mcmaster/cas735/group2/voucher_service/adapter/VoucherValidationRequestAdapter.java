package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationRequestData;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.VoucherValidationRequest;
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
public class VoucherValidationRequestAdapter {

    private final VoucherValidationRequest voucherValidationRequest;

    @Autowired
    public VoucherValidationRequestAdapter(VoucherValidationRequest voucherValidationRequest) {
        this.voucherValidationRequest = voucherValidationRequest;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "pac.exchange.permit.entry.validation.queue", durable = "true"),
            exchange = @Exchange(value = "${app.custom.messaging.exchange-topic}", type = "topic"),
            key = "voucher.entry.validation"))
    public void listen(String data) {
        log.debug("Receiving validation request {}", data);
        voucherValidationRequest.validate(translate(data));

    }


    private VoucherValidationRequestData translate(String raw) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(raw, VoucherValidationRequestData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
