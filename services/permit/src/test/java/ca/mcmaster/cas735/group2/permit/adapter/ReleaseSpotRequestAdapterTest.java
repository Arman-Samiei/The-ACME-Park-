package ca.mcmaster.cas735.group2.permit.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReleaseSpotRequestAdapterTest {

    private RabbitTemplate mockRabbitTemplate;
    private ReleaseSpotRequestAdapter adapter;

    @BeforeEach
    void setUp() {
        mockRabbitTemplate = mock(RabbitTemplate.class);
        adapter = new ReleaseSpotRequestAdapter(mockRabbitTemplate);

        try {
            var exchangeField = ReleaseSpotRequestAdapter.class.getDeclaredField("exchange");
            exchangeField.setAccessible(true);
            exchangeField.set(adapter, "test-exchange");
        } catch (Exception e) {
            fail("Failed to set private field 'exchange': " + e.getMessage());
        }
    }

    @Test
    void sendReleaseSpotRequest_shouldSendCorrectMessage() {
        String plateNumber = "PLATE123";

        ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);

        adapter.sendReleaseSpotRequest(plateNumber);

        verify(mockRabbitTemplate).convertAndSend(eq("test-exchange"), eq("spot.release.request"), messageCaptor.capture());
        assertEquals(plateNumber, messageCaptor.getValue());
    }

    @Test
    void outbound_shouldReturnCorrectExchange() {
        TopicExchange topicExchange = adapter.outbound();
        assertEquals("test-exchange", topicExchange.getName());
    }
}
