package ca.mcmaster.cas735.group2.payment_service.adapter;

import ca.mcmaster.cas735.group2.payment_service.dto.PaymentRequestDTO;
import ca.mcmaster.cas735.group2.payment_service.ports.PaymentActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static ca.mcmaster.cas735.group2.payment_service.TestUtils.CC_CVC;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.CC_EXPIRY;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.LOT_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.PLATE_NUMBER;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.SPOT_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.SUCCESS_CC_NUMBER;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.SUCCESS_STAFF_ID;
import static ca.mcmaster.cas735.group2.payment_service.TestUtils.convertMapToJson;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AMQPPaymentListenerTest {

    @Mock
    private PaymentActivity paymentActivity;

    @Mock
    private Channel channel;

    @InjectMocks
    private AMQPPaymentListener amqpPaymentListener;

    @Test
    public void testReceiveVisitorPaymentRequest() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "plateNumber", PLATE_NUMBER,
                "ccNumber", SUCCESS_CC_NUMBER,
                "ccExpiry", CC_EXPIRY,
                "ccCVC", CC_CVC,
                "hoursOccupied", "1"
        ));
        PaymentRequestDTO paymentRequestDTO = new ObjectMapper().readValue(data, PaymentRequestDTO.class);

        // Act
        amqpPaymentListener.receive(data, channel);

        // Assert
        verify(paymentActivity, times(1)).receivePaymentActivity(paymentRequestDTO);
    }

    @Test
    public void testReceivePermitBankPaymentRequest() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "plateNumber", PLATE_NUMBER,
                "ccNumber", SUCCESS_CC_NUMBER,
                "ccExpiry", CC_EXPIRY,
                "ccCVC", CC_CVC,
                "monthsPurchased", "3"
        ));
        PaymentRequestDTO paymentRequestDTO = new ObjectMapper().readValue(data, PaymentRequestDTO.class);

        // Act
        amqpPaymentListener.receive(data, channel);

        // Assert
        verify(paymentActivity, times(1)).receivePaymentActivity(paymentRequestDTO);
    }

    @Test
    public void testReceivePermitPayslipPaymentRequest() throws JsonProcessingException {
        // Arrange
        String data = convertMapToJson(Map.of(
                "lotID", LOT_ID,
                "spotID", SPOT_ID,
                "plateNumber", PLATE_NUMBER,
                "staffId", SUCCESS_STAFF_ID,
                "monthsPurchased", "3"
        ));
        PaymentRequestDTO paymentRequestDTO = new ObjectMapper().readValue(data, PaymentRequestDTO.class);

        // Act
        amqpPaymentListener.receive(data, channel);

        // Assert
        verify(paymentActivity, times(1)).receivePaymentActivity(paymentRequestDTO);
    }
}
