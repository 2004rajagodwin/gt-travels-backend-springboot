package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.SavedPassengerRequest;
import com.godwintech.gttravels.dto.SavedPassengerResponse;
import com.godwintech.gttravels.entity.SavedPassenger;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.exception.ForbiddenException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.SavedPassengerRepository;
import com.godwintech.gttravels.util.PassengerValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SavedPassengerService {

    private final SavedPassengerRepository savedPassengerRepository;

    public SavedPassengerService(SavedPassengerRepository savedPassengerRepository) {
        this.savedPassengerRepository = savedPassengerRepository;
    }

    public List<SavedPassengerResponse> listForUser(User user) {
        return savedPassengerRepository.findByUserIdOrderByNameAsc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public SavedPassengerResponse create(User user, SavedPassengerRequest request) {
        validateRequest(request);
        SavedPassenger passenger = new SavedPassenger();
        passenger.setUser(user);
        apply(passenger, request);
        return toResponse(savedPassengerRepository.save(passenger));
    }

    public SavedPassengerResponse update(User user, Long id, SavedPassengerRequest request) {
        validateRequest(request);
        SavedPassenger passenger = getOwned(id, user);
        apply(passenger, request);
        return toResponse(savedPassengerRepository.save(passenger));
    }

    public void delete(User user, Long id) {
        SavedPassenger passenger = getOwned(id, user);
        savedPassengerRepository.delete(passenger);
    }

    public SavedPassenger getOwned(Long id, User user) {
        return savedPassengerRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Saved passenger not found"));
    }

    private void validateRequest(SavedPassengerRequest request) {
        PassengerValidator.validateName(request.getName());
        PassengerValidator.validateAge(request.getAge());
        PassengerValidator.validateMobile(request.getMobile());
        PassengerValidator.validateEmail(request.getEmail());
    }

    private void apply(SavedPassenger passenger, SavedPassengerRequest request) {
        passenger.setName(request.getName().trim());
        passenger.setAge(request.getAge());
        passenger.setGender(request.getGender());
        passenger.setMobile(PassengerValidator.normalizeMobile(request.getMobile()));
        passenger.setEmail(request.getEmail() != null ? request.getEmail().trim() : null);
        passenger.setIdType(request.getIdType());
        passenger.setIdNumber(request.getIdNumber());
    }

    private SavedPassengerResponse toResponse(SavedPassenger passenger) {
        SavedPassengerResponse response = new SavedPassengerResponse();
        response.setId(passenger.getId());
        response.setName(passenger.getName());
        response.setAge(passenger.getAge());
        response.setGender(passenger.getGender());
        response.setMobile(passenger.getMobile());
        response.setEmail(passenger.getEmail());
        response.setIdType(passenger.getIdType());
        response.setIdNumber(passenger.getIdNumber());
        return response;
    }
}
