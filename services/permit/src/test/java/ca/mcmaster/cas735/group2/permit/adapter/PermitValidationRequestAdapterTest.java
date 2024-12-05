package ca.mcmaster.cas735.group2.permit.adapter;

import ca.mcmaster.cas735.group2.permit.dto.PermitValidationRequestData;
import ca.mcmaster.cas735.group2.permit.ports.provided.PermitValidationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PermitValidationRequestAdapterTest {

    @Mock
    private PermitValidationRequest mockPermitValidationRequest;

    private PermitValidationRequestAdapter adapter;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new PermitValidationRequestAdapter(mockPermitValidationRequest);
        objectMapper = new ObjectMapper();
    }

    @Test
    void listen_shouldCallValidateWithCorrectData() throws JsonProcessingException {
        PermitValidationRequestData requestData = new PermitValidationRequestData();
        requestData.setPlateNumber("PLATE123");
        String jsonData = objectMapper.writeValueAsString(requestData);

        adapter.listen(jsonData);

        ArgumentCaptor<PermitValidationRequestData> captor = ArgumentCaptor.forClass(PermitValidationRequestData.class);
        verify(mockPermitValidationRequest).validate(captor.capture());

        PermitValidationRequestData capturedData = captor.getValue();
        assertEquals("PLATE123", capturedData.getPlateNumber());
    }

    @Test
    void listen_shouldThrowRuntimeExceptionForInvalidJson() {
        String invalidJson = "invalid-json";

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            adapter.listen(invalidJson);
        });

        assertTrue(thrown.getCause() instanceof JsonProcessingException);
    }

    @Test
    void translate_shouldReturnCorrectObjectForValidJson() throws Exception {
        PermitValidationRequestData requestData = new PermitValidationRequestData();
        requestData.setPlateNumber("PLATE123");
        String validJson = objectMapper.writeValueAsString(requestData);

        Method translateMethod = PermitValidationRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        PermitValidationRequestData result = (PermitValidationRequestData) translateMethod.invoke(adapter, validJson);

        assertEquals("PLATE123", result.getPlateNumber());
    }

    @Test
    void translate_shouldThrowRuntimeExceptionForInvalidJson() throws Exception {
        String invalidJson = "invalid-json";

        Method translateMethod = PermitValidationRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        try {
            translateMethod.invoke(adapter, invalidJson);
            fail("should have thrown an exception");
        } catch (InvocationTargetException | IllegalAccessException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
        }
    }
}
