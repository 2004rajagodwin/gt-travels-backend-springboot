package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.BusImageResponse;
import com.godwintech.gttravels.dto.BusRequest;
import com.godwintech.gttravels.dto.BusResponse;
import com.godwintech.gttravels.entity.Amenity;
import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.BusImage;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.BusStatus;
import com.godwintech.gttravels.enums.SeatConfigurationType;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.AmenityRepository;
import com.godwintech.gttravels.repository.BusImageRepository;
import com.godwintech.gttravels.repository.BusRepository;
import com.godwintech.gttravels.repository.UserRepository;
import com.godwintech.gttravels.util.RegistrationNumberValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AdminBusService {

    private final BusRepository busRepository;
    private final UserRepository userRepository;
    private final AmenityRepository amenityRepository;
    private final BusImageRepository busImageRepository;

    public AdminBusService(
            BusRepository busRepository,
            UserRepository userRepository,
            AmenityRepository amenityRepository,
            BusImageRepository busImageRepository) {

        this.busRepository = busRepository;
        this.userRepository = userRepository;
        this.amenityRepository = amenityRepository;
        this.busImageRepository = busImageRepository;
    }

    public List<BusResponse> getAllBuses() {
        return busRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public BusResponse getBus(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        return mapToResponse(bus);
    }

    public BusResponse createBus(BusRequest request) {
        validateRegistration(request.getRegistrationNumber(), null);

        if (busRepository.existsByBusNumber(request.getBusNumber())) {
            throw new BadRequestException("Bus Number already exists");
        }

        Bus bus = new Bus();
        applyRequest(bus, request, true);
        return mapToResponse(busRepository.save(bus));
    }

    public BusResponse updateBus(Long id, BusRequest request) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        validateRegistration(request.getRegistrationNumber(), id);

        if (!bus.getBusNumber().equals(request.getBusNumber())
                && busRepository.existsByBusNumber(request.getBusNumber())) {
            throw new BadRequestException("Bus Number already exists");
        }

        applyRequest(bus, request, false);
        return mapToResponse(busRepository.save(bus));
    }

    public BusResponse updateBusStatus(Long id, BusStatus status) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        bus.setStatus(status);
        return mapToResponse(busRepository.save(bus));
    }

    public void deleteBus(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        busRepository.delete(bus);
    }

    private void validateRegistration(String registrationNumber, Long busId) {
        if (registrationNumber == null || registrationNumber.isBlank()) {
            return;
        }
        if (!RegistrationNumberValidator.isValid(registrationNumber)) {
            throw new BadRequestException("Invalid Indian registration number format");
        }
        String normalized = RegistrationNumberValidator.normalize(registrationNumber);
        if (busId == null) {
            if (busRepository.existsByRegistrationNumber(normalized)) {
                throw new BadRequestException("Registration number already exists");
            }
        } else if (busRepository.existsByRegistrationNumberAndIdNot(normalized, busId)) {
            throw new BadRequestException("Registration number already exists");
        }
    }

    private void applyRequest(Bus bus, BusRequest request, boolean isCreate) {
        bus.setBusNumber(request.getBusNumber());
        bus.setBusName(request.getBusName());
        bus.setBusType(request.getBusType());
        bus.setBusCategory(request.getBusCategory());
        bus.setAcType(request.getAcType());
        bus.setDescription(request.getDescription());

        if (request.getRegistrationNumber() != null && !request.getRegistrationNumber().isBlank()) {
            bus.setRegistrationNumber(RegistrationNumberValidator.normalize(request.getRegistrationNumber()));
        }

        if (request.getSeatConfiguration() != null) {
            bus.setSeatConfiguration(request.getSeatConfiguration());
        } else if (bus.getSeatConfiguration() == null) {
            bus.setSeatConfiguration(SeatConfigurationType.SEATER);
        }

        if (request.getStatus() != null) {
            bus.setStatus(request.getStatus());
        } else if (bus.getStatus() == null) {
            bus.setStatus(BusStatus.ACTIVE);
        }

        if (request.getTotalSeats() != null) {
            bus.setTotalSeats(request.getTotalSeats());
        }

        if (request.getOperatorId() != null) {
            User operator = userRepository.findById(request.getOperatorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Operator not found"));
            bus.setOperator(operator);
        } else if (!isCreate) {
            bus.setOperator(null);
        }

        if (request.getAmenityIds() != null) {
            Set<Amenity> amenities = new HashSet<>(amenityRepository.findByIdIn(new HashSet<>(request.getAmenityIds())));
            bus.setAmenities(amenities);
        }

        if (isCreate) {
            bus.initializeSeats();
        }

        if (request.getImages() != null) {
            bus.getImages().clear();
            for (var imgReq : request.getImages()) {
                BusImage image = new BusImage();
                image.setBus(bus);
                image.setImageUrl(imgReq.getImageUrl());
                image.setPrimaryImage(imgReq.isPrimaryImage());
                image.setActive(imgReq.isActive());
                bus.getImages().add(image);
            }
        }
    }

    private BusResponse mapToResponse(Bus bus) {
        BusResponse response = new BusResponse();
        response.setId(bus.getId());
        response.setBusNumber(bus.getBusNumber());
        response.setBusName(bus.getBusName());
        response.setRegistrationNumber(bus.getRegistrationNumber());
        response.setBusType(bus.getBusType());
        response.setBusCategory(bus.getBusCategory() != null ? bus.getBusCategory().name() : null);
        response.setAcType(bus.getAcType());
        response.setSeatConfiguration(
                bus.getSeatConfiguration() != null ? bus.getSeatConfiguration().name() : null);
        response.setStatus(bus.getStatus());
        response.setDescription(bus.getDescription());
        response.setTotalSeats(bus.getTotalSeats());
        response.setOperatorId(bus.getOperator() != null ? bus.getOperator().getId() : null);
        response.setOperatorName(bus.getOperator() != null ? bus.getOperator().getName() : "Not Assigned");
        response.setAmenities(bus.getAmenities().stream().map(Amenity::getName).sorted().toList());
        response.setImages(busImageRepository.findByBusIdAndActiveTrueOrderByPrimaryImageDesc(bus.getId()).stream()
                .map(img -> new BusImageResponse(img.getId(), img.getImageUrl(), img.isPrimaryImage()))
                .toList());
        return response;
    }
}
