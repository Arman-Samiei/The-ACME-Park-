package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PaymentResponseData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PaymentResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentResponseAdapterTest {

    @Mock
    private PaymentResponse paymentResponse;

    @InjectMocks
    private PaymentResponseAdapter paymentResponseAdapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void listen_shouldReceiveAndProcessPaymentResponse() throws Exception {
        PaymentResponseData responseData = new PaymentResponseData();
        responseData.setPlateNumber("PLATE123");
        responseData.setSuccess(true);

        String json = objectMapper.writeValueAsString(responseData);

        paymentResponseAdapter.listen(json);

        ArgumentCaptor<PaymentResponseData> captor = ArgumentCaptor.forClass(PaymentResponseData.class);
        verify(paymentResponse).receivePaymentResponse(captor.capture());

        assertEquals(responseData, captor.getValue());
    }

    @Test
    void translate_shouldReturnCorrectObjectForValidJson() throws Exception {
        PaymentResponseData expected = new PaymentResponseData();
        expected.setPlateNumber("PLATE123");
        expected.setSuccess(true);

        String json = objectMapper.writeValueAsString(expected);

        PaymentResponseData actual = invokeTranslate(json);

        assertEquals(expected, actual);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionForInvalidJson() throws Exception {
        String invalidJson = "invalid-json";

        try {
            invokeTranslate(invalidJson);
            fail("should have thrown an exception");
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }

    private PaymentResponseData invokeTranslate(String raw) throws Exception {
        Method translateMethod = PaymentResponseAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);
        return (PaymentResponseData) translateMethod.invoke(paymentResponseAdapter, raw);
    }
}
