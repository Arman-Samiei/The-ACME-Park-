package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinesCalculationResponseAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private FinesCalculationResponseAdapter responseAdapter;

    private FinesCalculationResponseData responseData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        responseData = new FinesCalculationResponseData("1", "PLATE123", 150.0);
    }

    @Test
    void sendResponse_shouldSendCorrectMessage() throws Exception {
        String exchange = "test-exchange";
        String routingKey = "fines.payment.response";

        ReflectionTestUtils.setField(responseAdapter, "exchange", exchange);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        responseAdapter.sendResponse(responseData);

        verify(rabbitTemplate).convertAndSend(eq(exchange), eq(routingKey), messageCaptor.capture());

        String actualJson = messageCaptor.getValue();
        String expectedJson = new ObjectMapper().writeValueAsString(responseData);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void translate_shouldReturnCorrectJson() throws Exception {
        Method translateMethod = FinesCalculationResponseAdapter.class.getDeclaredMethod("translate", FinesCalculationResponseData.class);
        translateMethod.setAccessible(true);

        String actualJson = (String) translateMethod.invoke(responseAdapter, responseData);
        String expectedJson = new ObjectMapper().writeValueAsString(responseData);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionOnSerializationError() throws Exception {
        FinesCalculationResponseData invalidData = mock(FinesCalculationResponseData.class);
        when(invalidData.getId()).thenThrow(new RuntimeException("Serialization Error"));

        Method translateMethod = FinesCalculationResponseAdapter.class.getDeclaredMethod("translate", FinesCalculationResponseData.class);
        translateMethod.setAccessible(true);

        try {
            translateMethod.invoke(responseAdapter, invalidData);
            fail("should have thrown an exception");
        } catch (InvocationTargetException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }
}
