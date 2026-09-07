package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.OperatorBusResponse;
import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.repository.BusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperatorBusService {

    private final BusRepository busRepository;
    private final UserService userService;

    public OperatorBusService(
            BusRepository busRepository,
            UserService userService) {

        this.busRepository = busRepository;
        this.userService = userService;
    }

    public List<OperatorBusResponse> getMyBuses() {

        User operator = userService.getCurrentUser();

        List<Bus> buses =
                busRepository.findByOperatorId(operator.getId());

        return buses.stream()
                .map(bus -> new OperatorBusResponse(

                        bus.getId(),

                        bus.getBusNumber(),

                        bus.getBusType(),

                        bus.getSeats() == null
                                ? 0
                                : bus.getSeats().size()

                ))
                .toList();
    }
}