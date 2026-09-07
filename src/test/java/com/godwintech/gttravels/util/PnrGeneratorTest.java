package com.godwintech.gttravels.util;

import com.godwintech.gttravels.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PnrGeneratorTest {

    @Mock
    private BookingRepository bookingRepository;

    @Test
    void generate_producesUniqueFormat() {
        when(bookingRepository.existsByPnr(anyString())).thenReturn(false);
        PnrGenerator generator = new PnrGenerator(bookingRepository);

        String pnr = generator.generate();
        assertTrue(pnr.startsWith("GTB-"));
        assertTrue(pnr.matches("GTB-\\d{4}-[A-Z0-9]{6}"));
    }

    @Test
    void generate_retriesOnCollision() {
        when(bookingRepository.existsByPnr(anyString()))
                .thenReturn(true)
                .thenReturn(false);
        PnrGenerator generator = new PnrGenerator(bookingRepository);
        assertDoesNotThrow(generator::generate);
    }
}
