package ca.mcmaster.cas735.group2.payment_service.business;

import ca.mcmaster.cas735.group2.payment_service.adapter.AMQPVisitorExitSender;
import ca.mcmaster.cas735.group2.payment_service.business.entity.Order;
import ca.mcmaster.cas735.group2.payment_service.business.entity.PaymentType;
import ca.mcmaster.cas735.group2.payment_service.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PermitOrderDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.BankConnection;
import ca.mcmaster.cas735.group2.payment_service.ports.OrderDataRepository;
import ca.mcmaster.cas735.group2.payment_service.ports.PayslipConnection;
import ca.mcmaster.cas735.group2.payment_service.ports.PermitPurchase;
import ca.mcmaster.cas735.group2.payment_service.ports.VisitorExit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final OrderDataRepository ordersDatabase;
    private final BankConnection bankConnection;
    private final PayslipConnection payslipConnection;
    private final VisitorExit visitorExit;
    private final PermitPurchase permitPurchase;

    @Autowired
    public PaymentServiceImpl(OrderDataRepository ordersDatabase,
                              BankConnection bankConnection,
                              PayslipConnection payslipConnection,
                              VisitorExit visitorExit,
                              PermitPurchase permitPurchase) {
        this.ordersDatabase = ordersDatabase;
        this.bankConnection = bankConnection;
        this.payslipConnection = payslipConnection;
        this.visitorExit = visitorExit;
        this.permitPurchase = permitPurchase;
    }

    @Override
    public void createOrderAndProcessPayment(PaymentRequestDTO paymentRequestDTO) {
        Order order = new Order();
        order.setId(paymentRequestDTO.id());
        order.setAmount(paymentRequestDTO.paymentAmount());
        order.setPaymentType(PaymentType.valueOf(paymentRequestDTO.paymentType()));
        order.setPaid(false);

        if (ordersDatabase.findById(order.getId()).isPresent()) {
            log.error("Order already exists: {}", order.getId());
            return;
        }

        ordersDatabase.saveAndFlush(order);

        if (paymentRequestDTO.ccNumber() != null) {
            bankConnection.processPayment(paymentRequestDTO);
        } else {
            payslipConnection.processPayment(paymentRequestDTO);
        }
    }

    @Override
    public void comfirmOrderAndRoute(PaymentResponseDTO paymentResponseDTO) {
        Order order = ordersDatabase
                .findById(paymentResponseDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + paymentResponseDTO.id()));

        order.setPaid(true);
        ordersDatabase.saveAndFlush(order);

        if (order.getPaymentType() == PaymentType.VISITOR_EXIT) {
            visitorExit.processVisitorExit(new GateActionDTO(paymentResponseDTO.success(), "A"));
        } else if (order.getPaymentType() == PaymentType.NEW_PERMIT || order.getPaymentType() == PaymentType.RENEW_PERMIT) {
            permitPurchase.processPermitPurchase(new PermitOrderDTO(paymentResponseDTO.success()));
        }

    }
}
