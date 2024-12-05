package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesDeletePaidRecordsData;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesDeletePaidRecords;
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

class FinesDeletePaidRecordsAdapterTest {

    @Mock
    private FinesDeletePaidRecords finesDeletePaidRecords;

    @InjectMocks
    private FinesDeletePaidRecordsAdapter adapter;

    private FinesDeletePaidRecordsData requestData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestData = new FinesDeletePaidRecordsData();
        requestData.setIsPaid(false);
        requestData.setPlateNumber("ABC123");
    }

    @Test
    void listen_shouldInvokeDeleteRecords() {
        String rawData = "{\"isPaid\":\"false\",\"plateNumber\":\"ABC123\"}";
        adapter.listen(rawData);

        ArgumentCaptor<FinesDeletePaidRecordsData> captor = ArgumentCaptor.forClass(FinesDeletePaidRecordsData.class);
        verify(finesDeletePaidRecords).deleteRecords(captor.capture());

        FinesDeletePaidRecordsData actualData = captor.getValue();
        assertEquals(requestData, actualData);
    }

    @Test
    void translate_shouldReturnCorrectObject() throws Exception {
        String rawData = "{\"isPaid\":\"false\",\"plateNumber\":\"ABC123\"}";

        Method translateMethod = FinesDeletePaidRecordsAdapter.class.getDeclaredMethod("translate", String.class);
        translateMethod.setAccessible(true);

        FinesDeletePaidRecordsData actualData = (FinesDeletePaidRecordsData) translateMethod.invoke(adapter, rawData);
        assertEquals(requestData, actualData);
    }

    @Test
    void translate_shouldThrowRuntimeExceptionOnInvalidData() throws Exception {
        String invalidData = "INVALID_JSON";

        Method translateMethod = FinesDeletePaidRecordsAdapter.class.getDeclaredMethod("translate", String.class);
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
