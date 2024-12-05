package ca.mcmaster.cas735.group2.voucher_service.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReleaseSpotRequestAdapterTest {

    private RabbitTemplate mockRabbitTemplate;
    private ReleaseSpotRequestAdapter adapter;

    @BeforeEach
    void setUp() {
        mockRabbitTemplate = mock(RabbitTemplate.class);
        adapter = new ReleaseSpotRequestAdapter(mockRabbitTemplate);

        try {
            var field = ReleaseSpotRequestAdapter.class.getDeclaredField("exchange");
            field.setAccessible(true);
            field.set(adapter, "test-exchange");
        } catch (Exception e) {
            throw new RuntimeException("Failed to set exchange field", e);
        }
    }

    @Test
    void sendReleaseSpotRequest_shouldSendCorrectMessage() {
        String plateNumber = "PLATE123";

        adapter.sendReleaseSpotRequest(plateNumber);

        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> messageCaptor = ArgumentCaptor.forClass(Object.class);

        verify(mockRabbitTemplate).convertAndSend(eq("test-exchange"), routingKeyCaptor.capture(), messageCaptor.capture());

        assertEquals("spot.release.request", routingKeyCaptor.getValue());
        assertEquals(plateNumber, messageCaptor.getValue());
    }

    @Test
    void outbound_shouldCreateTopicExchange() {
        TopicExchange exchange = adapter.outbound();

        assertEquals("test-exchange", exchange.getName());
        assertEquals("topic", exchange.getType());
    }
}
