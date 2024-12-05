package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitIssuanceRequestData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitIssuanceRequest;
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

class PermitIssuanceRequestAdapterTest {

    @Mock
    private PermitIssuanceRequest permitIssuanceRequest;

    @InjectMocks
    private PermitIssuanceRequestAdapter adapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void listen_shouldReceiveAndProcessPermitIssuanceRequest() throws JsonProcessingException {
        PermitIssuanceRequestData requestData = new PermitIssuanceRequestData();
        requestData.setTransponderID("s123");
        requestData.setPlateNumber("PLATE123");
        requestData.setFirstName("John");
        requestData.setLastName("Doe");
        requestData.setEmployeeID("E123");
        requestData.setLotID("LOT42");
        requestData.setMemberPaymentType("Credit");
        requestData.setCcNumber("1234567890123456");
        requestData.setCcExpiry("12/34");
        requestData.setCcCVC("123");
        requestData.setMonthsPurchased(6);

        String json = objectMapper.writeValueAsString(requestData);

        adapter.listen(json);

        ArgumentCaptor<PermitIssuanceRequestData> captor = ArgumentCaptor.forClass(PermitIssuanceRequestData.class);
        verify(permitIssuanceRequest).issue(captor.capture());

        assertEquals(requestData, captor.getValue());
    }

    @Test
    void translate_shouldReturnCorrectObjectForValidJson() throws Exception {
        PermitIssuanceRequestData expected = new PermitIssuanceRequestData();
        expected.setTransponderID("s123");
        expected.setPlateNumber("PLATE123");
        expected.setFirstName("John");
        expected.setLastName("Doe");
        expected.setEmployeeID("E123");
        expected.setLotID("LOT42");
        expected.setMemberPaymentType("Credit");
        expected.setCcNumber("1234567890123456");
        expected.setCcExpiry("12/34");
        expected.setCcCVC("123");
        expected.setMonthsPurchased(6);

        String json = objectMapper.writeValueAsString(expected);

        PermitIssuanceRequestData actual = invokeTranslate(json);

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

    private PermitIssuanceRequestData invokeTranslate(String raw) throws Exception {
        Method translateMethod = PermitIssuanceRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);
        return (PermitIssuanceRequestData) translateMethod.invoke(adapter, raw);
    }
}
