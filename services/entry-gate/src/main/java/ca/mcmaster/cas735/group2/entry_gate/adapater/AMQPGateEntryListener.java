package ca.mcmaster.cas735.group2.entry_gate.adapater;

import ca.mcmaster.cas735.group2.entry_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.entry_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.entry_gate.ports.VoucherGateActivity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AMQPGateEntryListener {

    private final TransponderGateActivity transponderGateActivity;
    private final VisitorGateActivity visitorGateActivity;
    private final VoucherGateActivity voucherGateActivity;

    @Autowired
    public AMQPGateEntryListener(TransponderGateActivity transponderGateActivity,
                                 VisitorGateActivity visitorGateActivity,
                                 VoucherGateActivity voucherGateActivity) {
        this.transponderGateActivity = transponderGateActivity;
        this.visitorGateActivity = visitorGateActivity;
        this.voucherGateActivity = voucherGateActivity;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.entry.transponder.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.entry.transponder"))
    public void receiveTransponder(String data, Channel channel) {
        TransponderGateActionDTO transponderGateActionDTO = convertToTransponderDTO(data);
        log.info("Received gate action for transponder: {} - channel: {}", transponderGateActionDTO, channel);
        transponderGateActivity.receiveTransponderGateActivity(transponderGateActionDTO);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.entry.visitor.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.entry.visitor"))
    public void receiveVisitor(String data, Channel channel) {
        VisitorGateActionDTO visitorGateActionDTO = convertToVisitorDTO(data);
        log.info("Received gate action for visitor: {} - channel: {}", visitorGateActionDTO, channel);
        visitorGateActivity.receiveVisitorGateActivity(visitorGateActionDTO);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.entry.voucher.queue", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "gate.entry.voucher"))
    public void receiveVoucher(String data, Channel channel) {
        VoucherGateActionDTO voucherGateActionDTO = convertToVoucherDTO(data);
        log.info("Received gate action for voucher: {} - channel: {}", voucherGateActionDTO, channel);
        voucherGateActivity.receiveVoucherGateActivity(voucherGateActionDTO);
    }

    private TransponderGateActionDTO convertToTransponderDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, TransponderGateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VisitorGateActionDTO convertToVisitorDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, VisitorGateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private VoucherGateActionDTO convertToVoucherDTO(String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(data, VoucherGateActionDTO.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
