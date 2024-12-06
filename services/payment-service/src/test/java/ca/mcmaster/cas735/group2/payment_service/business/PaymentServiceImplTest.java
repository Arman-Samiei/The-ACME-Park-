package ca.mcmaster.cas735.group2.payment_service.business;

import ca.mcmaster.cas735.group2.payment_service.business.argurment_matchers.FinesRequestDTOArgumentMatcher;
import ca.mcmaster.cas735.group2.payment_service.business.entity.Order;
import ca.mcmaster.cas735.group2.payment_service.business.entity.PaymentType;
import ca.mcmaster.cas735.group2.payment_service.dto.ExistingFinesDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.FinesRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.GateActionDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.NotifyFineDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingBankPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.OutgoingPayslipPaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PaymentResponseDTO;
import ca.mcmaster.cas735.group2.payment_service.dto.PermitOrderDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.BankConnection;
import ca.mcmaster.cas735.group2.payment_service.ports.DetermineFines;
import ca.mcmaster.cas735.group2.payment_service.ports.NotifyFines;
import ca.mcmaster.cas735.group2.payment_service.ports.OrderDataRepository;
import ca.mcmaster.cas735.group2.payment_service.ports.PayslipConnection;
import ca.mcmaster.cas735.group2.payment_service.ports.PermitPurchase;
import ca.mcmaster.cas735.group2.payment_service.ports.VisitorExit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static ca.mcmaster.cas735.group2.payment_service.TestUtils.CC_CVC;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.CC_EXPIRY;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.ORDER_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.SUCCESS_CC_NUMBER;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.SUCCESS_STAFF_ID;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {

    @Mock
    private OrderDataRepository ordersDatabase;
    @Mock
    private DetermineFines determineFines;
    @Mock
    private BankConnection bankConnection;
    @Mock
    private PayslipConnection payslipConnection;
    @Mock
    private NotifyFines notifyFines;
    @Mock
    private VisitorExit visitorExit;
    @Mock
    private PermitPurchase permitPurchase;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Test
    public void testReceiveVisitorPaymentActivityRoutesToFines() {
        // Arrange
        PaymentRequestDTO paymentRequestDTO = setUpVisitorPaymentRequest(1);
        FinesRequestDTO sendingFinesRequestDTO = new FinesRequestDTO("", PLATE_NUMBER);

        // Act
        paymentService.receivePaymentActivity(paymentRequestDTO);

        // Assert
        verify(ordersDatabase, times(1)).saveAndFlush(any(Order.class));
        verify(determineFines, times(1)).requestFineAmount(argThat(new FinesRequestDTOArgumentMatcher(sendingFinesRequestDTO)));
    }

    @Test
    public void testReceivePermitBankPaymentActivityRoutesToFines() {
        // Arrange
        PaymentRequestDTO paymentRequestDTO = setUpPermitPurchaseForBankRequest(1);
        FinesRequestDTO sendingFinesRequestDTO = new FinesRequestDTO("", PLATE_NUMBER);

        // Act
        paymentService.receivePaymentActivity(paymentRequestDTO);

        // Assert
        verify(ordersDatabase, times(1)).saveAndFlush(any(Order.class));
        verify(determineFines, times(1)).requestFineAmount(argThat(new FinesRequestDTOArgumentMatcher(sendingFinesRequestDTO)));
    }

    @Test
    public void testReceivePermitUniPaymentActivityRoutesToFines() {
        // Arrange
        PaymentRequestDTO paymentRequestDTO = setUpPermitPurchaseForPayslipRequest(1);
        FinesRequestDTO sendingFinesRequestDTO = new FinesRequestDTO("", PLATE_NUMBER);

        // Act
        paymentService.receivePaymentActivity(paymentRequestDTO);

        // Assert
        verify(ordersDatabase, times(1)).saveAndFlush(any(Order.class));
        verify(determineFines, times(1)).requestFineAmount(argThat(new FinesRequestDTOArgumentMatcher(sendingFinesRequestDTO)));
    }

    @Test
    public void testOrderNotFound() {
        // Arrange
        ExistingFinesDTO existingFinesDTO = new ExistingFinesDTO(ORDER_ID, PLATE_NUMBER, 105.50);

        // Act
        assertThrows(IllegalArgumentException.class, () -> paymentService.commitOrderAndRoute(existingFinesDTO));

        // Assert
        verify(bankConnection, times(0)).processBankPayment(any());
        verify(payslipConnection, times(0)).processPayslipPayment(any());
        verify(ordersDatabase, times(0)).saveAndFlush(any());
        verify(notifyFines, times(0)).sendFineNotification(any());
        verify(visitorExit, times(0)).processVisitorExit(any());
        verify(permitPurchase, times(0)).processPermitPurchase(any());
    }

    @Test
    public void testCommitVisitorOrderAndRoute() {
        // Arrange
        ExistingFinesDTO existingFinesDTO = new ExistingFinesDTO(ORDER_ID, PLATE_NUMBER, 105.50);
        Order existingOrder = getExistingVisitorOrder();
        when(ordersDatabase.findById(existingFinesDTO.id())).thenReturn(Optional.of(existingOrder));
        when(bankConnection.processBankPayment(any())).thenReturn(new PaymentResponseDTO(true));

        // Act
        paymentService.commitOrderAndRoute(existingFinesDTO);

        // Assert
        verify(bankConnection, times(1)).processBankPayment(new OutgoingBankPaymentRequestDTO(
                SUCCESS_CC_NUMBER,
                CC_EXPIRY,
                CC_CVC,
                existingFinesDTO.fineAmount() + 2.5
        ));
        verify(ordersDatabase, times(1)).saveAndFlush(existingOrder);
        verify(notifyFines, times(1)).sendFineNotification(new NotifyFineDTO(PLATE_NUMBER, true));
        verify(visitorExit, times(1)).processVisitorExit(new GateActionDTO(true, LOT_ID, SPOT_ID));
    }

    @Test
    public void testCommitVisitorOrderAndRouteWithNoPayment() {
        // Arrange
        ExistingFinesDTO existingFinesDTO = new ExistingFinesDTO(ORDER_ID, PLATE_NUMBER, 0);
        Order existingOrder = getExistingVisitorOrder();
        existingOrder.setAmount(0);
        when(ordersDatabase.findById(existingFinesDTO.id())).thenReturn(Optional.of(existingOrder));

        // Act
        paymentService.commitOrderAndRoute(existingFinesDTO);

        // Assert
        verify(bankConnection, times(0)).processBankPayment(any());
        verify(ordersDatabase, times(1)).saveAndFlush(existingOrder);
        verify(notifyFines, times(0)).sendFineNotification(any());
        verify(visitorExit, times(1)).processVisitorExit(new GateActionDTO(true, LOT_ID, SPOT_ID));
    }

    @Test
    public void testCommitPermitOrderAndRoute_BankPayment() {
        // Arrange
        ExistingFinesDTO existingFinesDTO = new ExistingFinesDTO(ORDER_ID, PLATE_NUMBER, 105.50);
        Order existingOrder = getExistingPermitBankOrder();
        when(ordersDatabase.findById(existingFinesDTO.id())).thenReturn(Optional.of(existingOrder));
        when(bankConnection.processBankPayment(any())).thenReturn(new PaymentResponseDTO(true));

        // Act
        paymentService.commitOrderAndRoute(existingFinesDTO);

        // Assert
        verify(bankConnection, times(1)).processBankPayment(new OutgoingBankPaymentRequestDTO(
                SUCCESS_CC_NUMBER,
                CC_EXPIRY,
                CC_CVC,
                existingFinesDTO.fineAmount() + 100
        ));
        verify(ordersDatabase, times(1)).saveAndFlush(existingOrder);
        verify(notifyFines, times(1)).sendFineNotification(new NotifyFineDTO(PLATE_NUMBER, true));
        verify(permitPurchase, times(1)).processPermitPurchase(new PermitOrderDTO(PLATE_NUMBER, true));
    }

    @Test
    public void testCommitPermitOrderAndRoute_UniPayment() {
        // Arrange
        ExistingFinesDTO existingFinesDTO = new ExistingFinesDTO(ORDER_ID, PLATE_NUMBER, 105.50);
        Order existingOrder = getExistingPermitPayslipOrder();
        when(ordersDatabase.findById(existingFinesDTO.id())).thenReturn(Optional.of(existingOrder));
        when(payslipConnection.processPayslipPayment(any())).thenReturn(new PaymentResponseDTO(true));

        // Act
        paymentService.commitOrderAndRoute(existingFinesDTO);

        // Assert
        verify(payslipConnection, times(1)).processPayslipPayment(new OutgoingPayslipPaymentRequestDTO(
                SUCCESS_STAFF_ID,
                existingFinesDTO.fineAmount() + 100
        ));
        verify(ordersDatabase, times(1)).saveAndFlush(existingOrder);
        verify(notifyFines, times(1)).sendFineNotification(new NotifyFineDTO(PLATE_NUMBER, true));
        verify(permitPurchase, times(1)).processPermitPurchase(new PermitOrderDTO(PLATE_NUMBER, true));
    }

    private PaymentRequestDTO setUpVisitorPaymentRequest(int hoursOccupied) {
        return new PaymentRequestDTO(
                null,
                LOT_ID,
                SPOT_ID,
                null,
                PLATE_NUMBER,
                SUCCESS_CC_NUMBER,
                CC_EXPIRY,
                CC_CVC,
                hoursOccupied,
                0,
                "VISITOR_EXIT"
        );
    }

    private PaymentRequestDTO setUpPermitPurchaseForBankRequest(int monthsPurchased) {
        return new PaymentRequestDTO(
                null,
                LOT_ID,
                SPOT_ID,
                null,
                PLATE_NUMBER,
                SUCCESS_CC_NUMBER,
                CC_EXPIRY,
                CC_CVC,
                0,
                monthsPurchased,
                "NEW_PERMIT"
        );
    }

    private PaymentRequestDTO setUpPermitPurchaseForPayslipRequest(int monthsPurchased) {
        return new PaymentRequestDTO(
                null,
                LOT_ID,
                SPOT_ID,
                SUCCESS_STAFF_ID,
                PLATE_NUMBER,
                null,
                null,
                null,
                0,
                monthsPurchased,
                "NEW_PERMIT"
        );
    }

    private static Order getExistingVisitorOrder() {
        Order existingOrder = new Order();
        existingOrder.setId(ORDER_ID);
        existingOrder.setCcNumber(SUCCESS_CC_NUMBER);
        existingOrder.setCcCVC(CC_CVC);
        existingOrder.setCcExpiry(CC_EXPIRY);
        existingOrder.setPlateNumber(PLATE_NUMBER);
        existingOrder.setLotID(LOT_ID);
        existingOrder.setSpotID(SPOT_ID);
        existingOrder.setAmount(2.5);
        existingOrder.setPaymentType(PaymentType.VISITOR_EXIT);
        existingOrder.setPaid(false);
        return existingOrder;
    }

    private static Order getExistingPermitBankOrder() {
        Order existingOrder = new Order();
        existingOrder.setId(ORDER_ID);
        existingOrder.setCcNumber(SUCCESS_CC_NUMBER);
        existingOrder.setCcCVC(CC_CVC);
        existingOrder.setCcExpiry(CC_EXPIRY);
        existingOrder.setPlateNumber(PLATE_NUMBER);
        existingOrder.setLotID(LOT_ID);
        existingOrder.setSpotID(SPOT_ID);
        existingOrder.setAmount(100);
        existingOrder.setPaymentType(PaymentType.NEW_PERMIT);
        existingOrder.setPaid(false);
        return existingOrder;
    }

    private static Order getExistingPermitPayslipOrder() {
        Order existingOrder = new Order();
        existingOrder.setId(ORDER_ID);
        existingOrder.setStaffId(SUCCESS_STAFF_ID);
        existingOrder.setPlateNumber(PLATE_NUMBER);
        existingOrder.setLotID(LOT_ID);
        existingOrder.setSpotID(SPOT_ID);
        existingOrder.setAmount(100);
        existingOrder.setPaymentType(PaymentType.NEW_PERMIT);
        existingOrder.setPaid(false);
        return existingOrder;
    }
}