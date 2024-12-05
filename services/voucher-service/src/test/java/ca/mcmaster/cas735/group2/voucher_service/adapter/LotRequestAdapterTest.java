package ca.mcmaster.cas735.group2.voucher_service.adapter;

import ca.mcmaster.cas735.group2.voucher_service.dto.VoucherLotRequestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LotRequestAdapterTest {

    private RabbitTemplate rabbitTemplate;
    private LotRequestAdapter adapter;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        adapter = new LotRequestAdapter(rabbitTemplate);

        try {
            var field = LotRequestAdapter.class.getDeclaredField("exchange");
            field.setAccessible(true);
            field.set(adapter, "test-exchange");
        } catch (Exception e) {
            throw new RuntimeException("Failed to set exchange field", e);
        }
    }

    @Test
    void requestSpot_shouldSendCorrectMessage() {
        String exchange = "test-exchange";
        VoucherLotRequestData voucherLotRequestData = new VoucherLotRequestData();
        voucherLotRequestData.setLotID("LOT42");
        voucherLotRequestData.setPlateNumber("PLATE123");

        try {
            var exchangeField = LotRequestAdapter.class.getDeclaredField("exchange");
            exchangeField.setAccessible(true);
            exchangeField.set(adapter, exchange);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        adapter.requestSpot(voucherLotRequestData);

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
        verify(rabbitTemplate).convertAndSend(eq(exchange), eq("spot.availability.request"), messageCaptor.capture());

        String actualMessage = messageCaptor.getValue();
        assertEquals("{\"lotID\":\"LOT42\",\"customerType\":\"visitor\",\"plateNumber\":\"PLATE123\"," +
                "\"accessPassProcessingStatus\":\"confirmed\",\"requestSender\":\"voucher\"}", actualMessage);
    }

    @Test
    void outbound_shouldCreateExchange() {
        TopicExchange topicExchange = adapter.outbound();
        assertEquals("test-exchange", topicExchange.getName());
    }
}
