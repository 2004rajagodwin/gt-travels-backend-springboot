package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.OperatorDashboardResponse;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.BusRepository;
import com.godwintech.gttravels.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

@Service
public class OperatorDashboardService {

    private final BusRepository busRepository;
    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;

    public OperatorDashboardService(
            BusRepository busRepository,
            ScheduleRepository scheduleRepository,
            BookingRepository bookingRepository,
            UserService userService) {

        this.busRepository = busRepository;
        this.scheduleRepository = scheduleRepository;
        this.bookingRepository = bookingRepository;
        this.userService = userService;
    }

    public OperatorDashboardResponse dashboard() {

        User operator = userService.getCurrentUser();
        Long operatorId = operator.getId();

        OperatorDashboardResponse response = new OperatorDashboardResponse();
        response.setTotalBuses(busRepository.countByOperatorId(operatorId));
        response.setTotalSchedules(scheduleRepository.countByBus_Operator_Id(operatorId));

        Long bookings = bookingRepository.countConfirmedByOperator(operatorId);
        response.setTotalBookings(bookings == null ? 0L : bookings);

        Double revenue = bookingRepository.getRevenueByOperator(operatorId);
        response.setTotalRevenue(revenue == null ? 0.0 : revenue);

        return response;
    }
}
