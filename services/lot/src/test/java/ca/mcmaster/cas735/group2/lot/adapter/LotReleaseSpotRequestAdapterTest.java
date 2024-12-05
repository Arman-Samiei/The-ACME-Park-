package ca.mcmaster.cas735.group2.lot.adapter;

import ca.mcmaster.cas735.group2.lot.ports.provided.LotReleaseSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

class LotReleaseSpotRequestAdapterTest {

    private LotReleaseSpotRequestAdapter adapter;

    @Mock
    private LotReleaseSpot lotReleaseSpot;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new LotReleaseSpotRequestAdapter(lotReleaseSpot);
    }

    @Test
    void listen_shouldInvokeReleaseSpotWithCorrectPlateNumber() {
        String plateNumber = "PLATE123";

        adapter.listen(plateNumber);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(lotReleaseSpot).releaseSpot(captor.capture());

        assertEquals(plateNumber, captor.getValue());
    }
}
