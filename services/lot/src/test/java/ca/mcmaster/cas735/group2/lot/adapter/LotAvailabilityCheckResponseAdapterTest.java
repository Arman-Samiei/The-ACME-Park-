package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityResponseData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LotAvailabilityCheckResponseAdapterTest {

    private LotAvailabilityCheckResponseAdapter adapter;
    private RabbitTemplate rabbitTemplate;

    @Value("${app.custom.messaging.exchange-topic}")
    private String exchange = "test-exchange";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() throws IllegalAccessException, NoSuchFieldException {
        rabbitTemplate = mock(RabbitTemplate.class);
        adapter = new LotAvailabilityCheckResponseAdapter(rabbitTemplate);

        adapter = new LotAvailabilityCheckResponseAdapter(rabbitTemplate);
        Field exchangeField = LotAvailabilityCheckResponseAdapter.class.getDeclaredField("exchange");
        exchangeField.setAccessible(true);
        exchangeField.set(adapter, "test-exchange");
    }

    @Test
    void sendAvailableSpot_shouldSendCorrectMessage() throws JsonProcessingException {
        LotAvailabilityResponseData responseData = new LotAvailabilityResponseData();
        responseData.setLotID("LOT42");
        responseData.setSpotID("SPOT1");
        responseData.setPlateNumber("PLATE123");

        String responseReceiver = "visitor";

        adapter.sendAvailableSpot(responseData, responseReceiver);

        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(rabbitTemplate).convertAndSend(eq(exchange), routingKeyCaptor.capture(), messageCaptor.capture());

        String expectedRoutingKey = responseReceiver + ".lot.response";
        String expectedMessage = objectMapper.writeValueAsString(responseData);

        assertEquals(expectedRoutingKey, routingKeyCaptor.getValue());
        assertEquals(expectedMessage, messageCaptor.getValue());
    }

    @Test
    void translate_shouldReturnCorrectJson() throws Exception {
        LotAvailabilityResponseData responseData = new LotAvailabilityResponseData();
        responseData.setLotID("LOT42");
        responseData.setSpotID("SPOT1");
        responseData.setPlateNumber("PLATE123");

        String expectedJson = objectMapper.writeValueAsString(responseData);

        Method translateMethod = LotAvailabilityCheckResponseAdapter.class.getDeclaredMethod("translate", LotAvailabilityResponseData.class);
        translateMethod.setAccessible(true);

        String actualJson = (String) translateMethod.invoke(adapter, responseData);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionOnSerializationError() throws Exception {
        LotAvailabilityResponseData invalidData = mock(LotAvailabilityResponseData.class);
        when(invalidData.getLotID()).thenThrow(new RuntimeException("Serialization Error"));

        Method translateMethod = LotAvailabilityCheckResponseAdapter.class.getDeclaredMethod("translate", LotAvailabilityResponseData.class);
        translateMethod.setAccessible(true);

        try {
            translateMethod.invoke(adapter, invalidData);
            fail("should have thrown an exception");
        } catch (InvocationTargetException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }
}
