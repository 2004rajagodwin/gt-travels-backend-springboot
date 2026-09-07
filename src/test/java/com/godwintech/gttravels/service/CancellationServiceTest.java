package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.*;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.enums.PolicyScopeType;
import com.godwintech.gttravels.enums.TripStatus;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.repository.CancellationPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancellationServiceTest {

    @Mock private CancellationPolicyRepository policyRepository;
    private CancellationService cancellationService;

    @BeforeEach
    void setUp() {
        cancellationService = new CancellationService(policyRepository);
    }

    @Test
    void calculate_appliesConfiguredRefundPercentage() {
        Booking booking = confirmedBooking(LocalDateTime.now().plusHours(50), 1000.0);
        CancellationPolicy policy = policyWithRules(
                new Rule(48, 90.0), new Rule(24, 75.0), new Rule(12, 50.0), new Rule(0, 0.0));
        when(policyRepository.findActiveByScope(PolicyScopeType.SCHEDULE, 1L)).thenReturn(Optional.empty());
        when(policyRepository.findActiveByScope(PolicyScopeType.BUS, 10L)).thenReturn(Optional.empty());
        when(policyRepository.findActiveByScope(PolicyScopeType.ROUTE, 5L)).thenReturn(Optional.empty());
        when(policyRepository.findActiveByScope(PolicyScopeType.OPERATOR, 2L)).thenReturn(Optional.empty());
        when(policyRepository.findActiveByScope(PolicyScopeType.GLOBAL, null)).thenReturn(Optional.of(policy));

        CancellationService.CancellationCalculation calc = cancellationService.calculate(booking);
        assertEquals(900.0, calc.refundAmount());
        assertEquals(100.0, calc.cancellationCharge());
    }

    @Test
    void validate_rejectsAlreadyCancelled() {
        Booking booking = confirmedBooking(LocalDateTime.now().plusDays(1), 500);
        booking.setStatus(BookingStatus.CANCELLED);
        assertThrows(ConflictException.class, () -> cancellationService.validateCancellable(booking));
    }

    private Booking confirmedBooking(LocalDateTime departure, double amount) {
        User user = new User();
        user.setId(1L);

        User operator = new User();
        operator.setId(2L);

        Route route = new Route();
        route.setId(5L);

        Bus bus = new Bus();
        bus.setId(10L);
        bus.setOperator(operator);

        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setDepartureTime(departure);
        schedule.setRoute(route);
        schedule.setBus(bus);
        schedule.setTripStatus(TripStatus.SCHEDULED);

        Booking booking = new Booking();
        booking.setBookingId("b1");
        booking.setPnr("GTB-2026-TEST01");
        booking.setPassenger(user);
        booking.setBus(bus);
        booking.setSchedule(schedule);
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setTotalAmount(amount);
        return booking;
    }

    private CancellationPolicy policyWithRules(Rule... rules) {
        CancellationPolicy policy = new CancellationPolicy();
        policy.setName("Default");
        policy.setScopeType(PolicyScopeType.GLOBAL);
        List<CancellationPolicyRule> ruleEntities = new java.util.ArrayList<>();
        for (Rule rule : rules) {
            CancellationPolicyRule entity = new CancellationPolicyRule();
            entity.setMinHoursBeforeJourney(rule.minHours());
            entity.setRefundPercentage(rule.refundPercent());
            entity.setPolicy(policy);
            ruleEntities.add(entity);
        }
        policy.setRules(ruleEntities);
        return policy;
    }

    private record Rule(int minHours, double refundPercent) {}
}
