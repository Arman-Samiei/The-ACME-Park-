package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotResponseData;
import ca.mcmaster.cas735.group2.voucher_service.ports.provided.LotResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;

class LotResponseAdapterTest {

    private LotResponse mockLotResponse;
    private LotResponseAdapter adapter;

    @BeforeEach
    void setUp() {
        mockLotResponse = mock(LotResponse.class);
        adapter = new LotResponseAdapter(mockLotResponse);


    }

    @Test
    void listen_shouldCallReserveSpotWithCorrectData() throws JsonProcessingException {
        String rawData = """
                    {
                        "lotID": "LOT42",
                        "spotID": "SPOT123",
                        "plateNumber": "PLATE123"
                    }
                """;

        adapter.listen(rawData);

        ArgumentCaptor<VoucherLotResponseData> captor = ArgumentCaptor.forClass(VoucherLotResponseData.class);
        verify(mockLotResponse).reserveSpot(captor.capture());

        VoucherLotResponseData capturedData = captor.getValue();
        assertEquals("LOT42", capturedData.getLotID());
        assertEquals("SPOT123", capturedData.getSpotID());
        assertEquals("PLATE123", capturedData.getPlateNumber());
    }

    @Test
    void listen_shouldThrowRuntimeExceptionForInvalidJson() {
        String invalidJson = "invalid-json";

        try {
            adapter.listen(invalidJson);
            fail("should have thrown an exception");
        } catch (Exception e) {
            assertInstanceOf(com.fasterxml.jackson.core.JsonParseException.class, e.getCause());
        }
    }
}
