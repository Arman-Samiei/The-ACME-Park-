package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherIssuanceRequestData;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.VoucherIssuanceRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoucherIssuanceRequestAdapterTest {

    @Mock
    private VoucherIssuanceRequest mockVoucherIssuanceRequest;

    private VoucherIssuanceRequestAdapter adapter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new VoucherIssuanceRequestAdapter(mockVoucherIssuanceRequest);
    }

    @Test
    void listen_shouldCallIssueMethodWithCorrectData() throws JsonProcessingException {
        VoucherIssuanceRequestData requestData = new VoucherIssuanceRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");
        requestData.setDays(3);
        String jsonData = objectMapper.writeValueAsString(requestData);

        adapter.listen(jsonData);

        ArgumentCaptor<VoucherIssuanceRequestData> captor = ArgumentCaptor.forClass(VoucherIssuanceRequestData.class);
        verify(mockVoucherIssuanceRequest, times(1)).issue(captor.capture());
        VoucherIssuanceRequestData capturedData = captor.getValue();

        assertEquals("PLATE123", capturedData.getPlateNumber());
        assertEquals("LOT42", capturedData.getLotID());
        assertEquals(3, capturedData.getDays());
    }

    @Test
    void translate_shouldReturnObjectForValidJson() throws Exception {
        VoucherIssuanceRequestData requestData = new VoucherIssuanceRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");
        requestData.setDays(3);
        String jsonData = objectMapper.writeValueAsString(requestData);

        Method translateMethod = VoucherIssuanceRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);
        VoucherIssuanceRequestData result = (VoucherIssuanceRequestData) translateMethod.invoke(adapter, jsonData);

        assertEquals("PLATE123", result.getPlateNumber());
        assertEquals("LOT42", result.getLotID());
        assertEquals(3, result.getDays());
    }

    @Test
    void translate_shouldThrowRuntimeExceptionForInvalidJson() throws Exception {
        String invalidJson = "invalid-json";
        Method translateMethod = VoucherIssuanceRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);
        try {
            translateMethod.invoke(adapter, invalidJson);
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }
}
