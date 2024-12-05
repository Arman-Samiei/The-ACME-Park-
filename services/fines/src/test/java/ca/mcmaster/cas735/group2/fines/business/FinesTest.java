package ca.mcmaster.cas735.group2.fines.business;

import ca.mcmaster.cas735.group2.fines.business.entities.FinesData;
import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationRequestData;
import ca.mcmaster.cas735.group2.fines.dto.FinesCalculationResponseData;
import ca.mcmaster.cas735.group2.fines.dto.FinesDeletePaidRecordsData;
import ca.mcmaster.cas735.group2.fines.dto.FinesIssuanceData;
import ca.mcmaster.cas735.group2.fines.ports.required.FinesCalculationResponse;
import ca.mcmaster.cas735.group2.fines.ports.required.FinesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FinesTest {

    @Mock
    private FinesCalculationResponse finesCalculationResponse;

    @Mock
    private FinesRepository database;

    @InjectMocks
    private Fines finesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void calculateTotalFine_shouldSendResponseWithCorrectAmount() {
        String plateNumber = "ABC123";
        String id = "1";
        Double totalAmount = 150.0;

        when(database.findTotalAmountByPlateNumber(plateNumber)).thenReturn(totalAmount);

        FinesCalculationRequestData requestData = new FinesCalculationRequestData();
        requestData.setId(id);
        requestData.setPlateNumber(plateNumber);
        finesService.calculateTotalFine(requestData);

        ArgumentCaptor<FinesCalculationResponseData> captor = ArgumentCaptor.forClass(FinesCalculationResponseData.class);
        verify(finesCalculationResponse).sendResponse(captor.capture());

        FinesCalculationResponseData responseData = captor.getValue();
        assertEquals(id, responseData.getId());
        assertEquals(plateNumber, responseData.getPlateNumber());
        assertEquals(totalAmount, responseData.getFineAmount(), 0.0001);
    }

    @Test
    void issueFine_shouldSaveFineData() {
        FinesIssuanceData issuanceData = new FinesIssuanceData();
        issuanceData.setAmount(150.0);
        issuanceData.setPlateNumber("ABC123");

        FinesData finesData = new FinesData();
        finesData.setFinesID(1L);
        finesData.setAmount(150.0);
        finesData.setPlateNumber("ABC123");

        when(database.saveAndFlush(any(FinesData.class))).thenReturn(finesData);

        finesService.issueFine(issuanceData);

        ArgumentCaptor<FinesData> captor = ArgumentCaptor.forClass(FinesData.class);
        verify(database).saveAndFlush(captor.capture());

        FinesData savedData = captor.getValue();
        assertEquals("ABC123", savedData.getPlateNumber());
        assertEquals(150.0, savedData.getAmount(), 0.0001);
    }

    @Test
    void deleteRecords_shouldDeleteRecordsWhenPaid() {
        String plateNumber = "ABC123";
        FinesDeletePaidRecordsData deleteData = new FinesDeletePaidRecordsData();
        deleteData.setIsPaid(true);
        deleteData.setPlateNumber(plateNumber);

        finesService.deleteRecords(deleteData);

        verify(database).deleteByPlateNumber(plateNumber);
    }

    @Test
    void deleteRecords_shouldNotDeleteRecordsWhenNotPaid() {
        String plateNumber = "ABC123";
        FinesDeletePaidRecordsData deleteData = new FinesDeletePaidRecordsData();
        deleteData.setPlateNumber(plateNumber);
        deleteData.setIsPaid(false);

        finesService.deleteRecords(deleteData);

        verify(database, never()).deleteByPlateNumber(anyString());
    }
}
