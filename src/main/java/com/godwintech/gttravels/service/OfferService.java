package com.godwintech.gttravels.service;

import com.godwintech.gttravels.entity.Bus;
import com.godwintech.gttravels.entity.PromotionalOffer;
import com.godwintech.gttravels.entity.Schedule;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.PromotionalOfferRepository;
import com.godwintech.gttravels.util.DiscountCalculator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OfferService {

    private final PromotionalOfferRepository offerRepository;
    private final BookingRepository bookingRepository;

    public OfferService(PromotionalOfferRepository offerRepository, BookingRepository bookingRepository) {
        this.offerRepository = offerRepository;
        this.bookingRepository = bookingRepository;
    }

    public PromotionalOffer findBestOffer(User user, Schedule schedule, Bus bus, double subtotalAfterCoupon) {
        if (subtotalAfterCoupon <= 0) {
            return null;
        }

        Long operatorId = bus.getOperator() != null ? bus.getOperator().getId() : null;
        Long routeId = schedule.getRoute() != null ? schedule.getRoute().getId() : null;
        Long busId = bus.getId();
        Long scheduleId = schedule.getId();

        List<PromotionalOffer> offers = offerRepository.findApplicableOffers(
                LocalDate.now(), operatorId, routeId, busId, scheduleId);

        return offers.stream()
                .filter(o -> subtotalAfterCoupon >= o.getMinimumBookingAmount())
                .filter(o -> !o.isFirstBookingOnly() || isFirstBooking(user))
                .max(Comparator.comparingDouble(o -> DiscountCalculator.calculate(
                        o.getDiscountType(), o.getDiscountValue(), subtotalAfterCoupon, o.getMaximumDiscount())))
                .orElse(null);
    }

    private boolean isFirstBooking(User user) {
        return bookingRepository.findByPassenger_EmailOrderByCreatedAtDesc(user.getEmail()).stream()
                .noneMatch(b -> b.getStatus() == BookingStatus.CONFIRMED);
    }
}
