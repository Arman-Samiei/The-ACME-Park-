package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.dto.LotAvailabilityRequestData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotAvailabilityCheckRequest;
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
import static org.mockito.Mockito.verify;

class LotAvailabilityCheckRequestAdapterTest {

    @Mock
    private LotAvailabilityCheckRequest lotAvailabilityCheckRequest;

    @InjectMocks
    private LotAvailabilityCheckRequestAdapter adapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void listen_shouldInvokeCheckLotAvailabilityWithValidData() throws JsonProcessingException {
        LotAvailabilityRequestData requestData = new LotAvailabilityRequestData();
        requestData.setLotID("LOT42");
        requestData.setCustomerType("student");
        requestData.setPlateNumber("PLATE123");
        requestData.setAccessPassProcessingStatus("confirmed");
        requestData.setRequestSender("visitor");

        String validJson = objectMapper.writeValueAsString(requestData);

        adapter.listen(validJson);

        ArgumentCaptor<LotAvailabilityRequestData> captor = ArgumentCaptor.forClass(LotAvailabilityRequestData.class);
        verify(lotAvailabilityCheckRequest).checkLotAvailability(captor.capture());

        LotAvailabilityRequestData capturedData = captor.getValue();
        assertEquals(requestData, capturedData);
    }

    @Test
    void listen_shouldThrowRuntimeExceptionOnInvalidJson() {
        String invalidJson = "invalid-json";

        try {
            adapter.listen(invalidJson);
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e);
        }
    }

    @Test
    void translate_shouldReturnCorrectObjectForValidJson() throws Exception {
        LotAvailabilityRequestData requestData = new LotAvailabilityRequestData();
        requestData.setLotID("LOT42");
        requestData.setCustomerType("visitor");
        requestData.setPlateNumber("PLATE123");
        requestData.setAccessPassProcessingStatus("confirmed");
        requestData.setRequestSender("voucher");

        String validJson = objectMapper.writeValueAsString(requestData);

        Method translateMethod = LotAvailabilityCheckRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        LotAvailabilityRequestData result = (LotAvailabilityRequestData) translateMethod.invoke(adapter, validJson);

        assertEquals(requestData, result);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionForInvalidJson() throws Exception {
        String invalidJson = "invalid-json";

        Method translateMethod = LotAvailabilityCheckRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        try {
            translateMethod.invoke(adapter, invalidJson);
            fail("should have thrown an exception");
        } catch (InvocationTargetException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }

}
