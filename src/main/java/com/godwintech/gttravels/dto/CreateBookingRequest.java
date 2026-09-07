package com.godwintech.gttravels.dto;

import com.godwintech.gttravels.enums.IdDocumentType;
import com.godwintech.gttravels.enums.PassengerGender;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CreateBookingRequest extends SeatSelectionRequest {

    @NotEmpty
    @Valid
    private List<PassengerRequest> passengers;

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
        this.passengers = passengers;
    }
}
