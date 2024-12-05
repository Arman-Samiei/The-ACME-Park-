package ca.mcmaster.cas735.group2.voucher_service.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VoucherIssuanceResponseAdapterTest {

    @Mock
    private RabbitTemplate mockRabbitTemplate;

    private VoucherIssuanceResponseAdapter adapter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        adapter = new VoucherIssuanceResponseAdapter(mockRabbitTemplate);

        Field exchangeField = VoucherIssuanceResponseAdapter.class.getDeclaredField("exchange");
        exchangeField.setAccessible(true);
        exchangeField.set(adapter, "test-exchange");
    }

    @Test
    void sendVoucherIssuanceResponse_shouldSendCorrectMessage() {
        String response = "Voucher issued successfully.";

        adapter.sendVoucherIssuanceResponse(response);

        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        verify(mockRabbitTemplate).convertAndSend(eq("test-exchange"), routingKeyCaptor.capture(), messageCaptor.capture());

        assertEquals("voucher.issue.response", routingKeyCaptor.getValue());
        assertEquals(response, messageCaptor.getValue());
    }

    @Test
    void outbound_shouldReturnTopicExchange() {
        TopicExchange exchange = adapter.outbound();

        assertEquals("test-exchange", exchange.getName());
        assertEquals("topic", exchange.getType());
    }
}
