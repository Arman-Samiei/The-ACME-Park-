package ca.mcmaster.cas735.group2.voucher_service.business;

import ca.mcmaster.cas735.group2.voucher_service.business.entities.VoucherData;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.ReleaseSpotRequest;
import ca.mcmaster.cas735.group2.voucher_service.ports.required.VoucherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VoucherCleanupTest {

    @Mock
    private VoucherRepository mockDatabase;

    @Mock
    private ReleaseSpotRequest mockReleaseSpotRequest;

    private VoucherCleanup voucherCleanup;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        voucherCleanup = new VoucherCleanup(mockDatabase, mockReleaseSpotRequest);
    }

    @Test
    void removeExpiredVouchers_shouldProcessExpiredPermitAndReleaseSpot() {
        LocalDateTime now = LocalDateTime.now();
        VoucherData expiredVoucher = new VoucherData();
        expiredVoucher.setPlateNumber("PLATE123");
        expiredVoucher.setExpirationTime(now.minusDays(1));

        when(mockDatabase.findAllByExpirationTimeBefore(any())).thenReturn(List.of(expiredVoucher));

        voucherCleanup.removeExpiredVouchers();

        verify(mockReleaseSpotRequest).sendReleaseSpotRequest("PLATE123");
    }

    @Test
    void processExpiredPermit_shouldReleaseSpot() throws Exception {
        VoucherData expiredVoucher = new VoucherData();
        expiredVoucher.setPlateNumber("PLATE123");

        Method processExpiredPermitMethod = VoucherCleanup.class.getDeclaredMethod("processExpiredPermit", VoucherData.class);
        processExpiredPermitMethod.setAccessible(true);

        processExpiredPermitMethod.invoke(voucherCleanup, expiredVoucher);

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockReleaseSpotRequest).sendReleaseSpotRequest(captor.capture());
        assertEquals("PLATE123", captor.getValue());
    }
}
