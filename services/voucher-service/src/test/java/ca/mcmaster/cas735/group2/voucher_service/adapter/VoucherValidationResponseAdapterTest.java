package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoucherValidationResponseAdapterTest {

    @Mock
    private RabbitTemplate mockRabbitTemplate;

    @InjectMocks
    private VoucherValidationResponseAdapter adapter;

    private VoucherValidationResponseData responseData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        responseData = new VoucherValidationResponseData(true, "LOT42", "SPOT42");
    }

    @Test
    void sendValidationResult_shouldSendCorrectMessage() {
        adapter.sendValidationResult(responseData);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(mockRabbitTemplate).convertAndSend(eq(null), eq("gate.entry.action"), messageCaptor.capture());

        String actualJson = messageCaptor.getValue();
        ObjectMapper mapper = new ObjectMapper();
        String expectedJson;
        try {
            expectedJson = mapper.writeValueAsString(responseData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void translate_shouldReturnCorrectJson() throws Exception {
        Method translateMethod = VoucherValidationResponseAdapter.class.getDeclaredMethod("translate", VoucherValidationResponseData.class);
        translateMethod.setAccessible(true);

        String result = (String) translateMethod.invoke(adapter, responseData);

        ObjectMapper mapper = new ObjectMapper();
        String expectedJson = mapper.writeValueAsString(responseData);

        assertEquals(expectedJson, result);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionForInvalidObject() throws Exception {
        Method translateMethod = VoucherValidationResponseAdapter.class.getDeclaredMethod("translate", VoucherValidationResponseData.class);
        translateMethod.setAccessible(true);

        VoucherValidationResponseData invalidData = mock(VoucherValidationResponseData.class);
        when(invalidData.getLotID()).thenThrow(new RuntimeException("Serialization Error"));

        try {
            translateMethod.invoke(adapter, invalidData);
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }
}
