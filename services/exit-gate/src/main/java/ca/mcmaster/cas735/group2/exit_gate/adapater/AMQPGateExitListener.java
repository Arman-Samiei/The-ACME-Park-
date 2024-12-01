package ca.mcmaster.cas735.group2.exit_gate.adapater;

import ca.mcmaster.cas735.group2.exit_gate.dto.TransponderGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VisitorGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.dto.VoucherGateActionDTO;
import ca.mcmaster.cas735.group2.exit_gate.ports.TransponderGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.VisitorGateActivity;
import ca.mcmaster.cas735.group2.exit_gate.ports.VoucherGateActivity;
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
public class AMQPGateExitListener {

    private final TransponderGateActivity transponderGateActivity;
    private final VisitorGateActivity visitorGateActivity;
    private final VoucherGateActivity voucherGateActivity;

    @Autowired
    public AMQPGateExitListener(TransponderGateActivity transponderGateActivity,
                                VisitorGateActivity visitorGateActivity,
                                VoucherGateActivity voucherGateActivity) {
        this.transponderGateActivity = transponderGateActivity;
        this.visitorGateActivity = visitorGateActivity;
        this.voucherGateActivity = voucherGateActivity;
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.exit.transponder", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    private void receiveTransponder(String data, Channel channel, long tag) {
        TransponderGateActionDTO transponderGateActionDTO = convertToTransponderDTO(data);
        log.info("Received gate action for transponder: {} - with tag: {} - channel: {}", transponderGateActionDTO, tag, channel);
        transponderGateActivity.receiveTransponderGateActivity(transponderGateActionDTO);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.exit.visitor", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    private void receiveVisitor(String data, Channel channel, long tag) {
        VisitorGateActionDTO visitorGateActionDTO = convertToVisitorDTO(data);
        log.info("Received gate action for visitor: {} - with tag: {} - channel: {}", visitorGateActionDTO, tag, channel);
        visitorGateActivity.receiveVisitorGateActivity(visitorGateActionDTO);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "gate.exit.voucher", durable = "true"),
            exchange = @Exchange(value = "${app.exchange}",
                    ignoreDeclarationExceptions = "true", type = "topic"),
            key = "*"))
    private void receiveVoucher(String data, Channel channel, long tag) {
        VoucherGateActionDTO voucherGateActionDTO = convertToVoucherDTO(data);
        log.info("Received gate action for voucher: {} - with tag: {} - channel: {}", voucherGateActionDTO, tag, channel);
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
