package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitLotRequestData;
import ca.mcmaster.cas735.group2.permit.dto.PermitLotResponseData;
import ca.mcmaster.cas735.group2.permit.ports.provided.LotResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class LotResponseAdapterTest {

    private LotResponseAdapter lotResponseAdapter;

    @Mock
    private LotResponse lotResponse;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        lotResponseAdapter = new LotResponseAdapter(lotResponse);
    }

    @Test
    void listen_shouldCallReserveSpotWithCorrectData() throws Exception {
        PermitLotResponseData responseData = new PermitLotResponseData();
        responseData.setLotID("LOT42");
        responseData.setSpotID("SPOT123");
        responseData.setPlateNumber("PLATE123");

        String jsonData = objectMapper.writeValueAsString(responseData);

        lotResponseAdapter.listen(jsonData);

        ArgumentCaptor<PermitLotResponseData> captor = ArgumentCaptor.forClass(PermitLotResponseData.class);
        verify(lotResponse).reserveSpot(captor.capture());

        PermitLotResponseData capturedData = captor.getValue();
        assertEquals(responseData, capturedData);
    }

    @Test
    void translate_shouldReturnCorrectObjectForValidJson() throws Exception {
        PermitLotResponseData responseData = new PermitLotResponseData();
        responseData.setLotID("LOT42");
        responseData.setSpotID("SPOT123");
        responseData.setPlateNumber("PLATE123");

        String validJson = objectMapper.writeValueAsString(responseData);

        PermitLotResponseData result = invokeTranslate(validJson);

        assertEquals(responseData, result);
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

    private PermitLotResponseData invokeTranslate(String jsonData) throws Exception {
        var translateMethod = LotResponseAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);
        return (PermitLotResponseData) translateMethod.invoke(lotResponseAdapter, jsonData);
    }
}
