package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.dto.LotOccupationStatusUpdateData;
import ca.mcmaster.cas735.group2.lot.ports.provided.LotOccupationStatusUpdate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.verify;

class LotStatusUpdateRequestAdapterTest {

    private LotStatusUpdateRequestAdapter adapter;

    @Mock
    private LotOccupationStatusUpdate lotOccupationStatusUpdate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new LotStatusUpdateRequestAdapter(lotOccupationStatusUpdate);
    }

    @Test
    void listen_shouldProcessValidData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        LotOccupationStatusUpdateData updateData = new LotOccupationStatusUpdateData();
        updateData.setSpotID("SPOT1");
        updateData.setIsSpotOccupied(true);

        String validJson = mapper.writeValueAsString(updateData);

        adapter.listen(validJson);

        ArgumentCaptor<LotOccupationStatusUpdateData> captor = ArgumentCaptor.forClass(LotOccupationStatusUpdateData.class);
        verify(lotOccupationStatusUpdate).updateSpotOccupationStatus(captor.capture());

        LotOccupationStatusUpdateData capturedData = captor.getValue();
        assertEquals(updateData.getSpotID(), capturedData.getSpotID());
        assertEquals(updateData.getIsSpotOccupied(), capturedData.getIsSpotOccupied());
    }

    @Test
    void listen_shouldThrowRuntimeExceptionForInvalidJson() {
        String invalidJson = "invalid-json";

        try {
            adapter.listen(invalidJson);
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertInstanceOf(RuntimeException.class, e);
        }
    }
}
