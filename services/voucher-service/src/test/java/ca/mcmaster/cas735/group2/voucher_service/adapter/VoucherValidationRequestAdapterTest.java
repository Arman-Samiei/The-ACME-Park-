package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherValidationRequestData;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.VoucherValidationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoucherValidationRequestAdapterTest {

    @Mock
    private VoucherValidationRequest mockVoucherValidationRequest;

    private VoucherValidationRequestAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new VoucherValidationRequestAdapter(mockVoucherValidationRequest);
    }

    @Test
    void listen_shouldProcessValidationRequest() {
        VoucherValidationRequestData requestData = new VoucherValidationRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonData;
        try {
            jsonData = objectMapper.writeValueAsString(requestData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        adapter.listen(jsonData);

        ArgumentCaptor<VoucherValidationRequestData> captor = ArgumentCaptor.forClass(VoucherValidationRequestData.class);
        verify(mockVoucherValidationRequest).validate(captor.capture());

        assertEquals(requestData, captor.getValue());
    }

    @Test
    void translate_shouldReturnCorrectObject() throws Exception {
        VoucherValidationRequestData requestData = new VoucherValidationRequestData();
        requestData.setPlateNumber("PLATE123");
        requestData.setLotID("LOT42");

        ObjectMapper objectMapper = new ObjectMapper();
        String validJson = objectMapper.writeValueAsString(requestData);

        Method translateMethod = VoucherValidationRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        VoucherValidationRequestData result = (VoucherValidationRequestData) translateMethod.invoke(adapter, validJson);

        assertEquals(requestData, result);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionForInvalidJson() throws Exception {
        String invalidJson = "invalid-json";

        Method translateMethod = VoucherValidationRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);
        try {
            translateMethod.invoke(adapter, invalidJson);
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }
}
