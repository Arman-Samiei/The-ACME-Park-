package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitLotRequestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class LotRequestAdapterTest {

    private LotRequestAdapter lotRequestAdapter;

    @Mock
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);
        lotRequestAdapter = new LotRequestAdapter(rabbitTemplate);
        Field exchangeField = LotRequestAdapter.class.getDeclaredField("exchange");
        exchangeField.setAccessible(true);
        exchangeField.set(lotRequestAdapter, "test-exchange");
    }

    @Test
    void requestSpot_shouldSendMessageToCorrectExchange() throws Exception {
        PermitLotRequestData permitLotRequestData = new PermitLotRequestData();
        permitLotRequestData.setLotID("LOT42");
        permitLotRequestData.setPlateNumber("PLATE123");
        permitLotRequestData.setRequestSender("PERMIT");

        String expectedJson = objectMapper.writeValueAsString(permitLotRequestData);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        lotRequestAdapter.requestSpot(permitLotRequestData);

        verify(rabbitTemplate).convertAndSend(eq("test-exchange"), eq("spot.availability.request"), messageCaptor.capture());

        String actualJson = messageCaptor.getValue();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionOnSerializationError() throws Exception {
        PermitLotRequestData invalidData = mock(PermitLotRequestData.class);
        when(invalidData.getLotID()).thenThrow(new RuntimeException("Serialization Error"));

        Method translateMethod = LotRequestAdapter.class.getDeclaredMethod("translate", PermitLotRequestData.class);
        translateMethod.setAccessible(true);

        try {
            translateMethod.invoke(lotRequestAdapter, invalidData);
            fail("should have thrown an exception");
        } catch (InvocationTargetException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }
}
