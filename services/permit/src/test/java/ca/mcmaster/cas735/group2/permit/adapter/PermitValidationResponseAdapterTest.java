package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitValidationResponseData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermitValidationResponseAdapterTest {

    private RabbitTemplate mockRabbitTemplate;
    private PermitValidationResponseAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        mockRabbitTemplate = mock(RabbitTemplate.class);
            adapter = new PermitValidationResponseAdapter(mockRabbitTemplate);
            Field exchangeField = PermitValidationResponseAdapter.class.getDeclaredField("exchange");
            exchangeField.setAccessible(true);
            exchangeField.set(adapter, "test-exchange");
    }

    @Test
    void sendValidationResult_shouldSendCorrectMessage() throws Exception {
        PermitValidationResponseData responseData = new PermitValidationResponseData(true, "LOT42");
        String expectedJson = new ObjectMapper().writeValueAsString(responseData);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        adapter.sendValidationResult(responseData);

        verify(mockRabbitTemplate).convertAndSend(eq("test-exchange"), eq("gate.entry.action"), messageCaptor.capture());
        assertEquals(expectedJson, messageCaptor.getValue());
    }

    @Test
    void translate_shouldReturnCorrectJson() throws Exception {
        PermitValidationResponseData responseData = new PermitValidationResponseData(true, "LOT42");
        Method translateMethod = PermitValidationResponseAdapter.class.getDeclaredMethod("translate", PermitValidationResponseData.class);
        translateMethod.setAccessible(true);

        String actualJson = (String) translateMethod.invoke(adapter, responseData);
        String expectedJson = new ObjectMapper().writeValueAsString(responseData);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionOnSerializationError() throws Exception {
        PermitValidationResponseData invalidData = mock(PermitValidationResponseData.class);
        when(invalidData.getShouldOpen()).thenThrow(new RuntimeException("Serialization Error"));

        Method translateMethod = PermitValidationResponseAdapter.class.getDeclaredMethod("translate", PermitValidationResponseData.class);
        translateMethod.setAccessible(true);

        try {
            translateMethod.invoke(adapter, invalidData);
            fail("should have thrown an exception");
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }

    @Test
    void outbound_shouldReturnCorrectExchange() {
        TopicExchange topicExchange = adapter.outbound();
        assertEquals("test-exchange", topicExchange.getName());
    }
}
