package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PaymentRequestData;
import ca.mcmaster.cas735.group2.permit.dto.PermitLotRequestData;
import ca.mcmaster.cas735.group2.permit.dto.PermitLotResponseData;
import ca.mcmaster.cas735.group2.permit.utils.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentRequestAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PaymentRequestAdapter paymentRequestAdapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(paymentRequestAdapter, "exchange", "exchange-topic");
    }

    @Test
    void requestPayment_shouldSendCorrectMessage() throws Exception {
        PaymentRequestData paymentRequestData = new PaymentRequestData();
        paymentRequestData.setPlateNumber("PLATE123");
        paymentRequestData.setStaffId("STAFF001");
        paymentRequestData.setCcNumber("1234567890123456");
        paymentRequestData.setCcExpiry("12/34");
        paymentRequestData.setCcCVC("123");
        paymentRequestData.setMonthsPurchased(12);

        paymentRequestAdapter.requestPayment(paymentRequestData);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(rabbitTemplate).convertAndSend(eq("exchange-topic"), eq("payment.activity.request"), messageCaptor.capture());

        String actualJson = messageCaptor.getValue();
        String expectedJson = objectMapper.writeValueAsString(paymentRequestData);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void translate_shouldReturnCorrectJson() throws Exception {
        PaymentRequestData paymentRequestData = new PaymentRequestData();
        paymentRequestData.setPlateNumber("PLATE123");
        paymentRequestData.setStaffId("STAFF001");
        paymentRequestData.setCcNumber("1234567890123456");
        paymentRequestData.setCcExpiry("12/34");
        paymentRequestData.setCcCVC("123");
        paymentRequestData.setMonthsPurchased(12);

        String result = invokeTranslate(paymentRequestData);

        String expected = objectMapper.writeValueAsString(paymentRequestData);
        assertEquals(expected, result);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionForInvalidData() throws Exception {
        PaymentRequestData invalidData = mock(PaymentRequestData.class);
        when(invalidData.getPlateNumber()).thenThrow(new RuntimeException("Serialization error"));

        try {
            invokeTranslate(invalidData);
            fail("should have thrown an exception");
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }

    private String invokeTranslate(PaymentRequestData data) throws Exception {
        Method translateMethod = PaymentRequestAdapter.class.getDeclaredMethod("translate", PaymentRequestData.class);
        translateMethod.setAccessible(true);
        return (String) translateMethod.invoke(paymentRequestAdapter, data);
    }
}
