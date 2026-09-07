package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.*;
import com.godwintech.gttravels.entity.*;
import com.godwintech.gttravels.enums.AuditActionType;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.CouponRepository;
import com.godwintech.gttravels.repository.CouponUsageRepository;
import com.godwintech.gttravels.util.DiscountCalculator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponUsageRepository couponUsageRepository;
    private final BookingValidationService bookingValidationService;
    private final FareCalculationService fareCalculationService;
    private final OfferService offerService;
    private final AuditLogService auditLogService;

    public CouponService(
            CouponRepository couponRepository,
            CouponUsageRepository couponUsageRepository,
            BookingValidationService bookingValidationService,
            FareCalculationService fareCalculationService,
            OfferService offerService,
            AuditLogService auditLogService) {
        this.couponRepository = couponRepository;
        this.couponUsageRepository = couponUsageRepository;
        this.bookingValidationService = bookingValidationService;
        this.fareCalculationService = fareCalculationService;
        this.offerService = offerService;
        this.auditLogService = auditLogService;
    }

    public CouponValidateResponse validateCoupon(CouponValidateRequest request, User user) {
        var ctx = bookingValidationService.validate(
                request.getScheduleId(),
                request.getSeatIds(),
                request.getBoardingPointId(),
                request.getDroppingPointId());

        Coupon coupon = validateCouponForUser(
                request.getCouponCode(),
                user,
                ctx.schedule(),
                ctx.boardingPoint(),
                request.getSeatIds().size(),
                false);

        FareDiscountContext discountContext = buildDiscountContext(
                coupon, user, ctx.schedule(), ctx.bus(), ctx.boardingPoint(), request.getSeatIds().size());

        FareBreakdownResponse fare = fareCalculationService.calculate(
                ctx.schedule(), request.getSeatIds().size(), ctx.boardingPoint(), discountContext);

        CouponValidateResponse response = new CouponValidateResponse();
        response.setCouponCode(coupon.getCode());
        response.setDiscountType(coupon.getDiscountType());
        response.setDiscountAmount(fare.getCouponDiscount());
        response.setSubtotalBeforeDiscount(fare.getBaseFare() + fare.getBoardingCharges() + fare.getConvenienceFee());
        response.setFinalAmount(fare.getTotalAmount());
        response.setFare(fare);
        return response;
    }

    public FareDiscountContext buildDiscountContext(
            String couponCode,
            User user,
            Schedule schedule,
            Bus bus,
            BoardingPoint boardingPoint,
            int seatCount) {

        FareDiscountContext context = new FareDiscountContext();
        if (couponCode != null && !couponCode.isBlank()) {
            Coupon coupon = validateCouponForUser(couponCode, user, schedule, boardingPoint, seatCount, false);
            context.setCoupon(coupon);
            double subtotal = fareCalculationService.calculateSubtotal(schedule, seatCount, boardingPoint);
            context.setCouponDiscount(DiscountCalculator.calculate(
                    coupon.getDiscountType(),
                    coupon.getDiscountValue(),
                    subtotal,
                    coupon.getMaximumDiscount()));
        }
        PromotionalOffer offer = offerService.findBestOffer(user, schedule, bus, subtotalForOffer(schedule, seatCount, boardingPoint, context));
        if (offer != null) {
            context.setOffer(offer);
            double afterCoupon = subtotalForOffer(schedule, seatCount, boardingPoint, context);
            context.setOfferDiscount(DiscountCalculator.calculate(
                    offer.getDiscountType(),
                    offer.getDiscountValue(),
                    afterCoupon,
                    offer.getMaximumDiscount()));
        }
        return context;
    }

    private double subtotalForOffer(Schedule schedule, int seatCount, BoardingPoint boardingPoint, FareDiscountContext ctx) {
        double subtotal = fareCalculationService.calculateSubtotal(schedule, seatCount, boardingPoint);
        return Math.max(0, subtotal - ctx.getCouponDiscount());
    }

    public FareDiscountContext buildDiscountContext(
            Coupon coupon,
            User user,
            Schedule schedule,
            Bus bus,
            BoardingPoint boardingPoint,
            int seatCount) {
        String code = coupon != null ? coupon.getCode() : null;
        return buildDiscountContext(code, user, schedule, bus, boardingPoint, seatCount);
    }

    public Coupon validateCouponForUser(
            String couponCode,
            User user,
            Schedule schedule,
            BoardingPoint boardingPoint,
            int seatCount,
            boolean unused) {

        if (couponCode == null || couponCode.isBlank()) {
            throw new BadRequestException("Coupon code is required");
        }

        Coupon coupon = couponRepository.findByCodeForUpdate(couponCode.trim())
                .orElseThrow(() -> new BadRequestException("Invalid coupon code"));

        LocalDate today = LocalDate.now();
        if (!coupon.isActive()) {
            throw new BadRequestException("Coupon is inactive");
        }
        if (today.isBefore(coupon.getStartDate())) {
            throw new BadRequestException("Coupon is not yet active");
        }
        if (today.isAfter(coupon.getExpiryDate())) {
            throw new BadRequestException("Coupon has expired");
        }

        double subtotal = fareCalculationService.calculateSubtotal(schedule, seatCount, boardingPoint);
        if (subtotal < coupon.getMinimumBookingAmount()) {
            throw new BadRequestException("Minimum booking amount not met for this coupon");
        }

        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new ConflictException("Coupon usage limit exceeded");
        }

        long userUsage = couponUsageRepository.countByCouponIdAndUserId(coupon.getId(), user.getId());
        if (userUsage >= coupon.getPerUserLimit()) {
            throw new ConflictException("You have already used this coupon");
        }

        return coupon;
    }

    @Transactional
    public void reserveCouponUsage(Coupon coupon, User user, Booking booking, double discountAmount) {
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        couponRepository.save(coupon);

        CouponUsage usage = new CouponUsage();
        usage.setCoupon(coupon);
        usage.setUser(user);
        usage.setBooking(booking);
        usage.setDiscountAmount(discountAmount);
        couponUsageRepository.save(usage);
    }

    public PageResponse<CouponResponse> search(String search, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1), Sort.by("createdAt").descending());
        Page<Coupon> result = (search == null || search.isBlank())
                ? couponRepository.findAll(pageable)
                : couponRepository.findByCodeContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        search, search, pageable);
        return new PageResponse<>(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(), result.getSize(), result.getTotalElements(), result.getTotalPages());
    }

    public CouponResponse create(CouponRequest request, User admin) {
        if (couponRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new ConflictException("Coupon code already exists");
        }
        validateDates(request.getStartDate(), request.getExpiryDate());
        Coupon coupon = new Coupon();
        applyRequest(coupon, request);
        Coupon saved = couponRepository.save(coupon);
        auditLogService.log(AuditActionType.COUPON_CREATED, "Coupon", String.valueOf(saved.getId()), admin,
                "Created coupon " + saved.getCode());
        return toResponse(saved);
    }

    public CouponResponse update(Long id, CouponRequest request, User admin) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        if (!coupon.getCode().equalsIgnoreCase(request.getCode())
                && couponRepository.existsByCodeIgnoreCase(request.getCode())) {
            throw new ConflictException("Coupon code already exists");
        }
        validateDates(request.getStartDate(), request.getExpiryDate());
        applyRequest(coupon, request);
        Coupon saved = couponRepository.save(coupon);
        auditLogService.log(AuditActionType.COUPON_UPDATED, "Coupon", String.valueOf(saved.getId()), admin,
                "Updated coupon " + saved.getCode());
        return toResponse(saved);
    }

    public CouponResponse updateStatus(Long id, boolean active, User admin) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));
        coupon.setActive(active);
        Coupon saved = couponRepository.save(coupon);
        auditLogService.log(active ? AuditActionType.COUPON_ACTIVATED : AuditActionType.COUPON_DEACTIVATED,
                "Coupon", String.valueOf(saved.getId()), admin, "Status changed for " + saved.getCode());
        return toResponse(saved);
    }

    public void delete(Long id) {
        couponRepository.deleteById(id);
    }

    private void validateDates(LocalDate start, LocalDate expiry) {
        if (expiry.isBefore(start)) {
            throw new BadRequestException("Expiry date must be on or after start date");
        }
    }

    private void applyRequest(Coupon coupon, CouponRequest request) {
        coupon.setCode(request.getCode());
        coupon.setDescription(request.getDescription());
        coupon.setDiscountType(request.getDiscountType());
        coupon.setDiscountValue(request.getDiscountValue());
        coupon.setMinimumBookingAmount(request.getMinimumBookingAmount() != null ? request.getMinimumBookingAmount() : 0);
        coupon.setMaximumDiscount(request.getMaximumDiscount());
        coupon.setStartDate(request.getStartDate());
        coupon.setExpiryDate(request.getExpiryDate());
        coupon.setUsageLimit(request.getUsageLimit());
        coupon.setPerUserLimit(request.getPerUserLimit() != null ? request.getPerUserLimit() : 1);
        coupon.setActive(request.isActive());
    }

    private CouponResponse toResponse(Coupon coupon) {
        CouponResponse r = new CouponResponse();
        r.setId(coupon.getId());
        r.setCode(coupon.getCode());
        r.setDescription(coupon.getDescription());
        r.setDiscountType(coupon.getDiscountType());
        r.setDiscountValue(coupon.getDiscountValue());
        r.setMinimumBookingAmount(coupon.getMinimumBookingAmount());
        r.setMaximumDiscount(coupon.getMaximumDiscount());
        r.setStartDate(coupon.getStartDate());
        r.setExpiryDate(coupon.getExpiryDate());
        r.setUsageLimit(coupon.getUsageLimit());
        r.setPerUserLimit(coupon.getPerUserLimit());
        r.setUsedCount(coupon.getUsedCount());
        r.setActive(coupon.isActive());
        r.setCreatedAt(coupon.getCreatedAt());
        return r;
    }
}
