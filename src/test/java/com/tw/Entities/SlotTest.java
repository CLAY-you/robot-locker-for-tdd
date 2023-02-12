package com.tw.Entities;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class SlotTest {
    @Test
    void should_return_generate_random_ticket_number_when_dispatch_available_slot() {
        Slot availableSlot = new Slot(1, 1, false);
        Slot spyAvailableSlot = spy(availableSlot);
        when(spyAvailableSlot.generateTicketNumber()).thenReturn("12345678");

        String ticketNumber = spyAvailableSlot.dispatchTicketNumber();

        assertThat(ticketNumber).isEqualTo("12345678");
    }

    @Test
    void should_generate_8_bit_ticket_number() {
        MockedStatic<RandomNumberGenerator> randomNoGenerator = mockStatic(RandomNumberGenerator.class);
        randomNoGenerator.when(RandomNumberGenerator::generate8BitRandomNumber).thenReturn("12345678");

        Slot availableSlot = new Slot(1, 1, false);
        String generatedTicketNumber = availableSlot.generateTicketNumber();

        assertThat(generatedTicketNumber).isEqualTo("12345678");
    }
}