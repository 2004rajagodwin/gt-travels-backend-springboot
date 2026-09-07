package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.SavedPassenger;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.SavedPassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavedPassengerServiceSecurityTest {

    @Mock
    private SavedPassengerRepository savedPassengerRepository;

    @InjectMocks
    private SavedPassengerService savedPassengerService;

    @Test
    void getOwned_rejectsOtherUsersPassenger() {
        User customerA = new User();
        customerA.setId(1L);

        when(savedPassengerRepository.findByIdAndUserId(99L, 1L))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> savedPassengerService.getOwned(99L, customerA));
    }

    @Test
    void getOwned_allowsOwner() {
        User owner = new User();
        owner.setId(2L);

        SavedPassenger passenger = new SavedPassenger();
        passenger.setId(5L);
        passenger.setUser(owner);

        when(savedPassengerRepository.findByIdAndUserId(5L, 2L))
                .thenReturn(Optional.of(passenger));

        savedPassengerService.getOwned(5L, owner);
    }
}
