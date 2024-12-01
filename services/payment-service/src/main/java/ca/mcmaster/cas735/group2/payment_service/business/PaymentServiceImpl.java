package ca.mcmaster.cas735.group2.payment_service.business;

import ca.mcmaster.cas735.group2.payment_service.business.entity.Order;
import ca.mcmaster.cas735.group2.payment_service.business.entity.PaymentType;
import ca.mcmaster.cas735.group2.payment_service.dto.ExistingFinesDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingBankPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingPayslipPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PermitOrderDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.BankConnection;
import ca.mcmaster.cas735.group2.payment_service.ports.DetermineFines;
import ca.mcmaster.cas735.group2.payment_service.ports.OrderDataRepository;
import ca.mcmaster.cas735.group2.payment_service.ports.PaymentActivity;
import ca.mcmaster.cas735.group2.payment_service.ports.PayslipConnection;
import ca.mcmaster.cas735.group2.payment_service.ports.PermitPurchase;
import ca.mcmaster.cas735.group2.payment_service.ports.ReceiveFines;
import ca.mcmaster.cas735.group2.payment_service.ports.VisitorExit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentActivity, ReceiveFines {

    private final OrderDataRepository ordersDatabase;
    private final DetermineFines determineFines;
    private final BankConnection bankConnection;
    private final PayslipConnection payslipConnection;
    private final VisitorExit visitorExit;
    private final PermitPurchase permitPurchase;

    @Autowired
    public PaymentServiceImpl(OrderDataRepository ordersDatabase,
                              DetermineFines determineFines,
                              BankConnection bankConnection,
                              PayslipConnection payslipConnection,
                              VisitorExit visitorExit,
                              PermitPurchase permitPurchase) {
        this.ordersDatabase = ordersDatabase;
        this.determineFines = determineFines;
        this.bankConnection = bankConnection;
        this.payslipConnection = payslipConnection;
        this.visitorExit = visitorExit;
        this.permitPurchase = permitPurchase;
    }

    @Override
    public void receivePaymentActivity(PaymentRequestDTO paymentRequestDTO) {
        PaymentType paymentType = PaymentType.valueOf(paymentRequestDTO.paymentType());

        double paymentAmount = 0;
        if (paymentType == PaymentType.VISITOR_EXIT) {
            paymentAmount = getPaymentAmountForParking(paymentRequestDTO.hoursOccupied());
        } else if (paymentType == PaymentType.NEW_PERMIT) {
            paymentAmount = getPaymentAmountForPermit(paymentRequestDTO.monthsPurchased());
        }

        Order order = new Order();
        order.setId(paymentRequestDTO.id() + "-" + UUID.randomUUID());
        order.setStaffId(paymentRequestDTO.staffId());
        order.setCcNumber(paymentRequestDTO.ccNumber());
        order.setCcCVC(paymentRequestDTO.ccCVC());
        order.setCcExpiry(paymentRequestDTO.ccExpiry());
        order.setLicensePlate(paymentRequestDTO.licensePlate());
        order.setGateId(paymentRequestDTO.gateId());
        order.setAmount(paymentAmount);
        order.setPaymentType(paymentType);
        order.setPaid(false);
        ordersDatabase.saveAndFlush(order);

        determineFines.requestFineAmount(new ExistingFinesDTO(
                paymentRequestDTO.id(),
                paymentRequestDTO.licensePlate(),
                0)
        );
    }

    @Override
    public void commitOrderAndRoute(ExistingFinesDTO existingFinesDTO) {
        Order order = ordersDatabase
                .findById(existingFinesDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + existingFinesDTO.id()));

        double existingPayment = order.getAmount();
        double cumulativePaymentAmount = existingPayment + existingFinesDTO.fineAmount();

        PaymentResponseDTO paymentResponseDTO;
        if (order.getStaffId() == null) {
            paymentResponseDTO = bankConnection.processBankPayment(new OutgoingBankPaymentRequestDTO(
                    order.getCcNumber(),
                    order.getCcExpiry(),
                    order.getCcCVC(),
                    cumulativePaymentAmount
            ));
        } else {
            paymentResponseDTO = payslipConnection.processPayslipPayment(new OutgoingPayslipPaymentRequestDTO(
                    order.getStaffId(),
                    cumulativePaymentAmount
            ));
        }

        order.setAmount(cumulativePaymentAmount);
        order.setPaid(true);
        ordersDatabase.saveAndFlush(order);

        if (order.getPaymentType() == PaymentType.VISITOR_EXIT) {
            visitorExit.processVisitorExit(new GateActionDTO(paymentResponseDTO.success(), order.getGateId()));
        } else if (order.getPaymentType() == PaymentType.NEW_PERMIT) {
            permitPurchase.processPermitPurchase(new PermitOrderDTO(order.getId(), paymentResponseDTO.success()));
        }

    }

    private double getPaymentAmountForParking(int hoursOccupied) {
        return 2.5 * hoursOccupied;
    }

    private double getPaymentAmountForPermit(int monthsPurchased) {
        return 100 * monthsPurchased;
    }
}
