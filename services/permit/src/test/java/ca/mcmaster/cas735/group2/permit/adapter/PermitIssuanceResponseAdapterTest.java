package ca.mcmaster.cas735.group2.permit.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermitIssuanceResponseAdapterTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private PermitIssuanceResponseAdapter permitIssuanceResponseAdapter;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        permitIssuanceResponseAdapter = new PermitIssuanceResponseAdapter(rabbitTemplate);
        Field exchangeField = PermitIssuanceResponseAdapter.class.getDeclaredField("exchange");
        exchangeField.setAccessible(true);
        exchangeField.set(permitIssuanceResponseAdapter, "test-exchange");
    }

    @Test
    void sendPermitIssuanceResponse_shouldSendCorrectMessage() {
        String response = "Permit issued successfully";

        permitIssuanceResponseAdapter.sendPermitIssuanceResponse(response);

        String exchange = "test-exchange";
        verify(rabbitTemplate).convertAndSend(eq(exchange), eq("permit.issue.response"), eq("\"Permit issued successfully\""));
    }

    @Test
    void translate_shouldReturnCorrectJsonForValidResponse() throws Exception {
        String response = "Permit issued successfully";

        String actualJson = invokeTranslate(response);

        String expectedJson = "\"Permit issued successfully\"";
        assertEquals(expectedJson, actualJson);
    }

    private String invokeTranslate(String response) throws Exception {
        Method translateMethod = PermitIssuanceResponseAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        return (String) translateMethod.invoke(permitIssuanceResponseAdapter, response);
    }

    @Test
    void outbound_shouldCreateTopicExchange() throws Exception {
        Method outboundMethod = PermitIssuanceResponseAdapter.class.getDeclaredMethod("outbound");
        outboundMethod.setAccessible(true);

        Object topicExchange = outboundMethod.invoke(permitIssuanceResponseAdapter);

        assertNotNull(topicExchange);
        assertTrue(topicExchange instanceof TopicExchange);
        assertEquals("test-exchange", ((TopicExchange) topicExchange).getName());
    }
}
