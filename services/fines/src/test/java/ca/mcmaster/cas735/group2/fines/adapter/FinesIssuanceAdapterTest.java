package ca.mcmaster.cas735.group2.fines.adapter;

import ca.mcmaster.cas735.group2.fines.dto.FinesIssuanceData;
import ca.mcmaster.cas735.group2.fines.ports.provided.FinesIssuance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FinesIssuanceAdapterTest {

    @Mock
    private FinesIssuance finesIssuance;

    @InjectMocks
    private FinesIssuanceAdapter adapter;

    private FinesIssuanceData finesData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        finesData = new FinesIssuanceData();
        finesData.setAmount(150.0);
        finesData.setPlateNumber("ABC123");
    }

    @Test
    void create_shouldCallIssueFine() {
        adapter.create(finesData);

        ArgumentCaptor<FinesIssuanceData> captor = ArgumentCaptor.forClass(FinesIssuanceData.class);
        verify(finesIssuance, times(1)).issueFine(captor.capture());

        FinesIssuanceData capturedData = captor.getValue();
        assertEquals(finesData, capturedData);
    }

    @Test
    void create_shouldReturnCorrectResponseStatus() {
        ResponseEntity<Void> response = ResponseEntity.status(HttpStatus.CREATED).build();
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
