package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationRequestData;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesCalculator;
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

class FinesCalculationRequestAdapterTest {

    @Mock
    private FinesCalculator finesCalculator;

    @InjectMocks
    private FinesCalculationRequestAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listen_shouldInvokeFinesCalculator() {
        String rawData = "{\"id\":\"fine1\",\"plateNumber\":\"ABC123\"}";
        FinesCalculationRequestData expectedData = new FinesCalculationRequestData();
        expectedData.setId("fine1");
        expectedData.setPlateNumber("ABC123");

        adapter.listen(rawData);

        ArgumentCaptor<FinesCalculationRequestData> captor = ArgumentCaptor.forClass(FinesCalculationRequestData.class);
        verify(finesCalculator).calculateTotalFine(captor.capture());

        FinesCalculationRequestData actualData = captor.getValue();
        assertEquals(expectedData, actualData);
    }

    @Test
    void translate_shouldReturnCorrectObject() throws Exception {
        String rawData = "{\"id\":\"fine1\",\"plateNumber\":\"ABC123\"}";
        FinesCalculationRequestData expectedData = new FinesCalculationRequestData();
        expectedData.setId("fine1");
        expectedData.setPlateNumber("ABC123");

        Method translateMethod = FinesCalculationRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        FinesCalculationRequestData actualData = (FinesCalculationRequestData) translateMethod.invoke(adapter, rawData);

        assertEquals(expectedData, actualData);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionOnInvalidData() throws Exception {
        String invalidData = "INVALID_JSON";

        Method translateMethod = FinesCalculationRequestAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        try {
            translateMethod.invoke(adapter, invalidData);
            fail("should have thrown an exception");
        } catch (InvocationTargetException e) {
            assertInstanceOf(RuntimeException.class, e.getCause());
            assertTrue(e.getCause().getMessage().contains("Unrecognized token"));
        }
    }
}
