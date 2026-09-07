package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.AdminDashboardResponse;
import com.godwintech.gttravels.enums.ERole;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.BusRepository;
import com.godwintech.gttravels.repository.RouteRepository;
import com.godwintech.gttravels.repository.ScheduleRepository;
import com.godwintech.gttravels.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;
    private final ScheduleRepository scheduleRepository;
    private final BookingRepository bookingRepository;

    public AdminDashboardService(
            UserRepository userRepository,
            BusRepository busRepository,
            RouteRepository routeRepository,
            ScheduleRepository scheduleRepository,
            BookingRepository bookingRepository) {

        this.userRepository = userRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
        this.scheduleRepository = scheduleRepository;
        this.bookingRepository = bookingRepository;
    }

    public AdminDashboardResponse getDashboard() {

        AdminDashboardResponse response = new AdminDashboardResponse();

        // Users
        response.setTotalUsers(userRepository.count());

        response.setTotalCustomers(
                userRepository.countByRoles_Name(ERole.ROLE_CUSTOMER));

        response.setTotalOperators(
                userRepository.countByRoles_Name(ERole.ROLE_OPERATOR));

        // Bus
        response.setTotalBuses(busRepository.count());

        // Route
        response.setTotalRoutes(routeRepository.count());

        // Schedule
        response.setTotalSchedules(scheduleRepository.count());

        // Booking
        response.setTotalBookings(bookingRepository.count());

        // Revenue
        Double revenue = bookingRepository.getTotalRevenue();
        response.setTotalRevenue(revenue == null ? 0.0 : revenue);

        return response;
    }
}