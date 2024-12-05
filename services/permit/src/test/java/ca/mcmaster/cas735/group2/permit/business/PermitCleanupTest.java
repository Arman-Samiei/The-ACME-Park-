package ca.mcmaster.cas735.group2.permit.business;

import ca.mcmaster.cas735.group2.permit.business.entities.PermitData;
import ca.mcmaster.cas735.group2.permit.ports.required.PermitRepository;
import ca.mcmaster.cas735.group2.permit.ports.required.ReleaseSpotRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PermitCleanupTest {

    private PermitRepository mockRepository;
    private ReleaseSpotRequest mockReleaseSpotRequest;
    private PermitCleanup permitCleanup;

    @BeforeEach
    void setUp() {
        mockRepository = mock(PermitRepository.class);
        mockReleaseSpotRequest = mock(ReleaseSpotRequest.class);
        permitCleanup = new PermitCleanup(mockRepository, mockReleaseSpotRequest);
    }

    @Test
    void removeExpiredPermits_shouldProcessExpiredPermitAndReleaseSpot() {
        LocalDateTime now = LocalDateTime.now();

        PermitData expiredPermit = new PermitData();
        expiredPermit.setPlateNumber("PLATE123");
        expiredPermit.setExpirationTime(now.minusDays(1));

        when(mockRepository.findAllByExpirationTimeBefore(any())).thenReturn(List.of(expiredPermit));

        permitCleanup.removeExpiredPermits();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockReleaseSpotRequest).sendReleaseSpotRequest(captor.capture());
        assertEquals("PLATE123", captor.getValue());
    }

}
