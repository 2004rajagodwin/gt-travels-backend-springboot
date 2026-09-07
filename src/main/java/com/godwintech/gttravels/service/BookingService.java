package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.*;
import com.godwintech.gttravels.entity.*;
import com.godwintech.gttravels.enums.BookingStatus;
import com.godwintech.gttravels.enums.PaymentStatus;
import com.godwintech.gttravels.enums.SeatStatus;
import com.godwintech.gttravels.exception.BadRequestException;
import com.godwintech.gttravels.exception.ConflictException;
import com.godwintech.gttravels.exception.ForbiddenException;
import com.godwintech.gttravels.exception.ResourceNotFoundException;
import com.godwintech.gttravels.repository.BookingRepository;
import com.godwintech.gttravels.repository.SeatRepository;
import com.godwintech.gttravels.repository.UserRepository;
import com.godwintech.gttravels.util.BookingReferenceGenerator;
import com.godwintech.gttravels.util.PassengerValidator;
import com.godwintech.gttravels.util.PnrGenerator;
import com.razorpay.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);
    private static final List<BookingStatus> OPEN_STATUSES =
            List.of(BookingStatus.PENDING, BookingStatus.PAYMENT_PENDING);

    private final SeatRepository seatRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final PaymentService paymentService;
    private final MailService mailService;
    private final UserService userService;
    private final SeatAvailabilityService seatAvailabilityService;
    private final BookingValidationService bookingValidationService;
    private final FareCalculationService fareCalculationService;
    private final SavedPassengerService savedPassengerService;
    private final PnrGenerator pnrGenerator;
    private final BookingReferenceGenerator bookingReferenceGenerator;
    private final CouponService couponService;
    private final CancellationService cancellationService;
    private final RefundService refundService;
    private final AuditLogService auditLogService;

    public BookingService(
            SeatRepository seatRepository,
            BookingRepository bookingRepository,
            UserRepository userRepository,
            PaymentService paymentService,
            MailService mailService,
            UserService userService,
            SeatAvailabilityService seatAvailabilityService,
            BookingValidationService bookingValidationService,
            FareCalculationService fareCalculationService,
            SavedPassengerService savedPassengerService,
            PnrGenerator pnrGenerator,
            BookingReferenceGenerator bookingReferenceGenerator,
            CouponService couponService,
            CancellationService cancellationService,
            RefundService refundService,
            AuditLogService auditLogService) {

        this.seatRepository = seatRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.paymentService = paymentService;
        this.mailService = mailService;
        this.userService = userService;
        this.seatAvailabilityService = seatAvailabilityService;
        this.bookingValidationService = bookingValidationService;
        this.fareCalculationService = fareCalculationService;
        this.savedPassengerService = savedPassengerService;
        this.pnrGenerator = pnrGenerator;
        this.bookingReferenceGenerator = bookingReferenceGenerator;
        this.couponService = couponService;
        this.cancellationService = cancellationService;
        this.refundService = refundService;
        this.auditLogService = auditLogService;
    }

    public FareBreakdownResponse previewFare(FarePreviewRequest request, User user) {
        var ctx = bookingValidationService.validate(
                request.getScheduleId(),
                request.getSeatIds(),
                request.getBoardingPointId(),
                request.getDroppingPointId());
        FareDiscountContext discountContext = couponService.buildDiscountContext(
                request.getCouponCode(), user, ctx.schedule(), ctx.bus(), ctx.boardingPoint(),
                request.getSeatIds().size());
        return fareCalculationService.calculate(
                ctx.schedule(), request.getSeatIds().size(), ctx.boardingPoint(), discountContext);
    }

    public FareBreakdownResponse previewFare(FarePreviewRequest request) {
        User user = userService.getCurrentUser();
        return previewFare(request, user);
    }

    @Transactional
    public BookingResponse initiateBooking(CreateBookingRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        validatePassengers(request);

        var ctx = bookingValidationService.validate(
                request.getScheduleId(),
                request.getSeatIds(),
                request.getBoardingPointId(),
                request.getDroppingPointId());

        List<Seat> seats = seatRepository.findAllByIdForUpdate(request.getSeatIds());
        for (Seat seat : seats) {
            if (!seatAvailabilityService.isSeatAvailableForSchedule(seat, ctx.schedule().getId())) {
                throw new ConflictException(seat.getSeatNumber() + " is no longer available");
            }
        }

        validateGender(user, seats, ctx.bus());
        validatePassengerSeats(request, seats);

        FareDiscountContext discountContext = couponService.buildDiscountContext(
                request.getCouponCode(), user, ctx.schedule(), ctx.bus(), ctx.boardingPoint(), seats.size());
        FareBreakdownResponse fare = fareCalculationService.calculate(
                ctx.schedule(), seats.size(), ctx.boardingPoint(), discountContext);

        LocalDateTime now = LocalDateTime.now();
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.LOCKED);
            seat.setLockedBy(user.getId());
            seat.setLockedAt(now);
        }
        seatRepository.saveAll(seats);

        Order order = paymentService.createOrder(fare.getTotalAmount());
        String bookingId = UUID.randomUUID().toString();

        Booking booking = new Booking();
        booking.setBookingId(bookingId);
        booking.setBookingReference(bookingReferenceGenerator.generate());
        booking.setPassenger(user);
        booking.setBus(ctx.bus());
        booking.setSchedule(ctx.schedule());
        booking.setSeats(seats);
        booking.setBookingTime(now);
        booking.setCreatedAt(now);
        booking.setUpdatedAt(now);
        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        applyFare(booking, fare);
        booking.setRazorpayOrderId(order.get("id"));
        booking.setBoardingPoint(ctx.boardingPoint().getPointName());
        booking.setDropPoint(ctx.droppingPoint().getPointName());
        booking.setBoardingPointEntity(ctx.boardingPoint());
        booking.setDroppingPointEntity(ctx.droppingPoint());
        if (discountContext.getCoupon() != null) {
            booking.setCoupon(discountContext.getCoupon());
        }
        if (discountContext.getOffer() != null) {
            booking.setOffer(discountContext.getOffer());
        }
        booking.setPassengers(buildPassengers(booking, request.getPassengers(), seats, user));

        bookingRepository.save(booking);
        if (discountContext.getCoupon() != null && fare.getCouponDiscount() > 0) {
            couponService.reserveCouponUsage(
                    discountContext.getCoupon(), user, booking, fare.getCouponDiscount());
        }
        log.info("Booking initiated: ref={}, userId={}, seats={}",
                booking.getBookingReference(), user.getId(), seats.size());

        BookingResponse response = new BookingResponse();
        response.setBookingId(bookingId);
        response.setBookingReference(booking.getBookingReference());
        response.setRazorpayOrderId(order.get("id").toString());
        response.setAmount(fare.getTotalAmount());
        response.setKey(paymentService.getKeyId());
        response.setFare(fare);
        return response;
    }

    /** @deprecated use initiateBooking(CreateBookingRequest) */
    @Transactional
    public BookingResponse initiateBooking(SeatSelectionRequest request, String email) {
        CreateBookingRequest createRequest = new CreateBookingRequest();
        createRequest.setScheduleId(request.getScheduleId());
        createRequest.setSeatIds(request.getSeatIds());
        createRequest.setBoardingPointId(request.getBoardingPointId());
        createRequest.setDroppingPointId(request.getDroppingPointId());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<PassengerRequest> passengers = request.getSeatIds().stream().map(seatId -> {
            PassengerRequest p = new PassengerRequest();
            p.setSeatId(seatId);
            p.setName(user.getName());
            p.setAge(25);
            p.setGender(com.godwintech.gttravels.enums.PassengerGender.OTHER);
            p.setMobile(user.getPhone());
            p.setEmail(user.getEmail());
            return p;
        }).toList();
        createRequest.setPassengers(passengers);
        return initiateBooking(createRequest, email);
    }

    public Booking confirmBooking(PaymentOrderRequest request, User user) {
        Booking booking = confirmBookingTransaction(request, user);
        sendConfirmationEmailSafely(booking);
        return booking;
    }

    @Transactional
    protected Booking confirmBookingTransaction(PaymentOrderRequest request, User user) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        verifyCustomerOwnership(booking, user);

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            if (Objects.equals(booking.getRazorpayPaymentId(), request.getPaymentId())) {
                return booking;
            }
            throw new ConflictException("Booking already confirmed with a different payment");
        }

        if (!OPEN_STATUSES.contains(booking.getStatus())) {
            throw new ConflictException("Booking already processed");
        }

        if (!Objects.equals(booking.getRazorpayOrderId(), request.getOrderId())) {
            throw new BadRequestException("Payment order does not match booking");
        }

        boolean verified = paymentService.verifyPaymentSignature(
                request.getOrderId(), request.getPaymentId(), request.getSignature());
        if (!verified) {
            throw new BadRequestException("Payment verification failed");
        }

        List<Seat> seats = seatRepository.findAllByIdForUpdate(
                booking.getSeats().stream().map(Seat::getId).toList());

        for (Seat seat : seats) {
            if (!seatAvailabilityService.isSeatAvailableForSchedule(seat, booking.getSchedule().getId())
                    && seat.getStatus() != SeatStatus.LOCKED) {
                throw new ConflictException("Seat " + seat.getSeatNumber() + " is no longer available");
            }
            if (seat.getStatus() == SeatStatus.LOCKED
                    && seat.getLockedBy() != null
                    && !seat.getLockedBy().equals(user.getId())) {
                throw new ConflictException("Seat " + seat.getSeatNumber() + " is locked by another user");
            }
        }

        if (booking.getPnr() == null) {
            booking.setPnr(pnrGenerator.generate());
        }
        booking.setStatus(BookingStatus.CONFIRMED);
        booking.setPaymentStatus(PaymentStatus.PAID);
        booking.setRazorpayPaymentId(request.getPaymentId());
        booking.setUpdatedAt(LocalDateTime.now());

        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.BOOKED);
            seat.setLockedAt(null);
            seat.setLockedBy(null);
        }

        seatRepository.saveAll(seats);
        Booking saved = bookingRepository.save(booking);
        log.info("Booking confirmed: pnr={}, userId={}", saved.getPnr(), user.getId());
        return saved;
    }

    @Transactional
    public BookingResponse retryPayment(String bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        verifyCustomerOwnership(booking, user);

        if (!OPEN_STATUSES.contains(booking.getStatus())) {
            throw new BadRequestException("Booking is not eligible for payment retry");
        }

        for (Seat seat : booking.getSeats()) {
            if (!seatAvailabilityService.isSeatAvailableForSchedule(seat, booking.getSchedule().getId())
                    && !(seat.getStatus() == SeatStatus.LOCKED
                    && Objects.equals(seat.getLockedBy(), user.getId()))) {
                throw new ConflictException("Seat " + seat.getSeatNumber() + " is no longer available");
            }
        }

        FareDiscountContext discountContext = buildDiscountContextForBooking(booking, user);
        FareBreakdownResponse fare = fareCalculationService.calculate(
                booking.getSchedule(),
                booking.getSeats().size(),
                booking.getBoardingPointEntity(),
                discountContext);
        applyFare(booking, fare);

        Order order = paymentService.createOrder(fare.getTotalAmount());
        booking.setRazorpayOrderId(order.get("id"));
        booking.setStatus(BookingStatus.PAYMENT_PENDING);
        booking.setPaymentStatus(PaymentStatus.PENDING);
        bookingRepository.save(booking);

        BookingResponse response = new BookingResponse();
        response.setBookingId(booking.getBookingId());
        response.setBookingReference(booking.getBookingReference());
        response.setRazorpayOrderId(order.get("id").toString());
        response.setAmount(fare.getTotalAmount());
        response.setKey(paymentService.getKeyId());
        response.setFare(fare);
        return response;
    }

    @Transactional(readOnly = true)
    public CancellationPreviewResponse previewCancellation(String bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        verifyCustomerOwnership(booking, user);
        return cancellationService.preview(booking);
    }

    @Transactional
    public CancellationResultResponse cancelBooking(String bookingId, User user) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        verifyCustomerOwnership(booking, user);
        cancellationService.validateCancellable(booking);

        CancellationService.CancellationCalculation calc = cancellationService.calculate(booking);
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setCancelledAt(LocalDateTime.now());
        booking.setCancellationCharge(calc.cancellationCharge());
        booking.setRefundAmount(calc.refundAmount());
        booking.setUpdatedAt(LocalDateTime.now());
        releaseSeats(booking);
        bookingRepository.save(booking);

        auditLogService.log(com.godwintech.gttravels.enums.AuditActionType.CANCELLATION,
                "Booking", booking.getBookingId(), user,
                "Cancelled booking " + booking.getPnr());

        RefundResponse refundResponse = null;
        if (calc.refundAmount() > 0 && booking.getPaymentStatus() == PaymentStatus.PAID) {
            booking.setPaymentStatus(PaymentStatus.REFUND_PENDING);
            bookingRepository.save(booking);
            refundResponse = refundService.processCancellationRefund(booking, user, "Customer cancellation");
        }

        log.info("Booking cancelled: pnr={}, userId={}", booking.getPnr(), user.getId());
        CancellationResultResponse result = new CancellationResultResponse();
        result.setBookingId(booking.getBookingId());
        result.setPnr(booking.getPnr());
        result.setStatus(booking.getStatus().name());
        result.setPaymentStatus(booking.getPaymentStatus().name());
        result.setCancellationCharge(calc.cancellationCharge());
        result.setRefundAmount(calc.refundAmount());
        result.setRefund(refundResponse);
        return result;
    }

    private FareDiscountContext buildDiscountContextForBooking(Booking booking, User user) {
        String couponCode = booking.getCoupon() != null ? booking.getCoupon().getCode() : null;
        return couponService.buildDiscountContext(
                couponCode, user, booking.getSchedule(), booking.getBus(),
                booking.getBoardingPointEntity(), booking.getSeats().size());
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getMyBookings(String email, String tab) {
        return bookingRepository.findByPassenger_EmailOrderByCreatedAtDesc(email).stream()
                .filter(b -> matchesTab(b, tab))
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingSummaryResponse> searchAdminBookings(
            String pnr, String reference, String status, String date, int page, int size) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> result = bookingRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();
            if (pnr != null && !pnr.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("pnr")), "%" + pnr.toLowerCase() + "%"));
            }
            if (reference != null && !reference.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("bookingReference")),
                        "%" + reference.toLowerCase() + "%"));
            }
            if (status != null && !status.isBlank()) {
                predicates.add(cb.equal(root.get("status"), BookingStatus.valueOf(status)));
            }
            if (date != null && !date.isBlank()) {
                predicates.add(cb.equal(root.get("schedule").get("journeyDate"), LocalDate.parse(date)));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);

        List<BookingSummaryResponse> content = result.getContent().stream()
                .map(this::toSummaryResponse).toList();
        return new PageResponse<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    @Transactional(readOnly = true)
    public PageResponse<BookingSummaryResponse> searchOperatorBookings(
            String pnr, String date, Long scheduleId, int page, int size) {
        User operator = userService.getCurrentUser();
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Booking> result = bookingRepository.findAll((root, query, cb) -> {
            var predicates = new ArrayList<jakarta.persistence.criteria.Predicate>();
            predicates.add(cb.equal(root.get("bus").get("operator").get("id"), operator.getId()));
            if (pnr != null && !pnr.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("pnr")), "%" + pnr.toLowerCase() + "%"));
            }
            if (date != null && !date.isBlank()) {
                predicates.add(cb.equal(root.get("schedule").get("journeyDate"), LocalDate.parse(date)));
            }
            if (scheduleId != null) {
                predicates.add(cb.equal(root.get("schedule").get("id"), scheduleId));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);

        List<BookingSummaryResponse> content = result.getContent().stream()
                .map(this::toSummaryResponse).toList();
        return new PageResponse<>(content, result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Booking getBookingEntityForCustomer(String bookingId, User user) {
        Booking booking = getBookingEntity(bookingId);
        verifyCustomerOwnership(booking, user);
        return booking;
    }

    @Transactional(readOnly = true)
    public Booking getBookingEntity(String bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
    }

    @Transactional(readOnly = true)
    public BookingDetailsResponse getBookingById(String bookingId, User user) {
        Booking booking = getBookingEntity(bookingId);
        verifyCustomerOwnership(booking, user);
        return toDetailsResponse(booking);
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> getAllBookings() {
        return bookingRepository.findAll().stream().map(this::toSummaryResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> filterBookings(String date, String bus) {
        return bookingRepository.findAll().stream()
                .filter(b -> bus == null || bus.isBlank()
                        || b.getBus().getBusNumber().equalsIgnoreCase(bus))
                .filter(b -> date == null || date.isBlank()
                        || b.getSchedule().getJourneyDate().toString().equals(date))
                .map(this::toSummaryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BookingSummaryResponse> filterOperatorBookings(String date, String bus) {
        User operator = userService.getCurrentUser();
        return bookingRepository.findByBus_Operator_IdOrderByCreatedAtDesc(operator.getId()).stream()
                .filter(b -> bus == null || bus.isBlank()
                        || b.getBus().getBusNumber().equalsIgnoreCase(bus))
                .filter(b -> date == null || date.isBlank()
                        || b.getSchedule().getJourneyDate().toString().equals(date))
                .map(this::toSummaryResponse)
                .toList();
    }

    private boolean matchesTab(Booking booking, String tab) {
        if (tab == null || tab.isBlank() || "all".equalsIgnoreCase(tab)) {
            return true;
        }
        LocalDate journey = booking.getSchedule().getJourneyDate();
        LocalDate today = LocalDate.now();
        return switch (tab.toLowerCase()) {
            case "upcoming" -> journey != null && !journey.isBefore(today)
                    && booking.getStatus() != BookingStatus.CANCELLED;
            case "past" -> journey != null && journey.isBefore(today)
                    && booking.getStatus() != BookingStatus.CANCELLED;
            case "cancelled" -> booking.getStatus() == BookingStatus.CANCELLED;
            default -> true;
        };
    }

    private void validatePassengers(CreateBookingRequest request) {
        if (request.getPassengers() == null || request.getPassengers().isEmpty()) {
            throw new BadRequestException("Passenger details are required");
        }
        if (request.getPassengers().size() != request.getSeatIds().size()) {
            throw new BadRequestException("Passenger count must match selected seats");
        }
        Set<Long> seatIds = new HashSet<>(request.getSeatIds());
        for (PassengerRequest passenger : request.getPassengers()) {
            PassengerValidator.validateName(passenger.getName());
            PassengerValidator.validateAge(passenger.getAge());
            PassengerValidator.validateMobile(passenger.getMobile());
            PassengerValidator.validateEmail(passenger.getEmail());
            if (!seatIds.contains(passenger.getSeatId())) {
                throw new BadRequestException("Passenger seat does not match selected seats");
            }
        }
        if (request.getPassengers().stream().map(PassengerRequest::getSeatId).distinct().count()
                != request.getPassengers().size()) {
            throw new BadRequestException("Duplicate passenger seat assignment");
        }
    }

    private void validatePassengerSeats(CreateBookingRequest request, List<Seat> seats) {
        Set<Long> selected = seats.stream().map(Seat::getId).collect(Collectors.toSet());
        for (PassengerRequest passenger : request.getPassengers()) {
            if (!selected.contains(passenger.getSeatId())) {
                throw new BadRequestException("Invalid seat assignment for passenger");
            }
        }
    }

    private List<BookingPassenger> buildPassengers(
            Booking booking, List<PassengerRequest> requests, List<Seat> seats, User user) {
        Map<Long, Seat> seatMap = seats.stream().collect(Collectors.toMap(Seat::getId, s -> s));
        List<BookingPassenger> passengers = new ArrayList<>();
        for (PassengerRequest req : requests) {
            if (req.getSavedPassengerId() != null) {
                savedPassengerService.getOwned(req.getSavedPassengerId(), user);
            }
            Seat seat = seatMap.get(req.getSeatId());
            BookingPassenger bp = new BookingPassenger();
            bp.setBooking(booking);
            bp.setSeat(seat);
            bp.setName(req.getName().trim());
            bp.setAge(req.getAge());
            bp.setGender(req.getGender());
            bp.setMobile(PassengerValidator.normalizeMobile(req.getMobile()));
            bp.setEmail(req.getEmail() != null ? req.getEmail().trim() : null);
            bp.setIdType(req.getIdType());
            bp.setIdNumber(req.getIdNumber());
            passengers.add(bp);
        }
        return passengers;
    }

    private void applyFare(Booking booking, FareBreakdownResponse fare) {
        booking.setBaseFare(fare.getBaseFare());
        booking.setBoardingCharges(fare.getBoardingCharges());
        booking.setConvenienceFee(fare.getConvenienceFee());
        booking.setTax(fare.getTax());
        booking.setDiscount(fare.getDiscount());
        booking.setCouponDiscount(fare.getCouponDiscount());
        booking.setOfferDiscount(fare.getOfferDiscount());
        booking.setTotalAmount(fare.getTotalAmount());
    }

    private void releaseSeats(Booking booking) {
        for (Seat seat : booking.getSeats()) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockedAt(null);
            seat.setLockedBy(null);
        }
        seatRepository.saveAll(booking.getSeats());
    }

    private void sendConfirmationEmailSafely(Booking booking) {
        try {
            mailService.sendBookingTicket(booking);
        } catch (Exception e) {
            log.error("Failed to send confirmation email for booking {}", booking.getBookingId(), e);
        }
    }

    private void verifyCustomerOwnership(Booking booking, User user) {
        if (!booking.getPassenger().getId().equals(user.getId())) {
            throw new ForbiddenException("You do not have access to this booking");
        }
    }

    private void validateGender(User user, List<Seat> selectedSeats, Bus bus) {
        List<Seat> allSeats = seatRepository.findByBusId(bus.getId());

        for (Seat seat : selectedSeats) {
            String adjacent = getAdjacentSeat(seat.getSeatNumber());
            if (adjacent == null) {
                continue;
            }

            Seat adjacentSeat = allSeats.stream()
                    .filter(s -> s.getSeatNumber().equals(adjacent))
                    .findFirst()
                    .orElse(null);

            if (adjacentSeat == null || adjacentSeat.getStatus() != SeatStatus.BOOKED) {
                continue;
            }

            Booking booking = bookingRepository
                    .findBySeatAndStatus(adjacentSeat, BookingStatus.CONFIRMED)
                    .orElse(null);

            if (booking != null
                    && booking.getPassenger().getGender() != user.getGender()) {
                throw new BadRequestException("Adjacent seat occupied by opposite gender");
            }
        }
    }

    private String getAdjacentSeat(String seat) {
        if (seat.startsWith("L")) {
            int num = Integer.parseInt(seat.substring(1));
            return (num % 2 == 0) ? "L" + (num - 1) : "L" + (num + 1);
        }
        if (seat.startsWith("R")) {
            int num = Integer.parseInt(seat.substring(1));
            return (num % 2 == 0) ? "R" + (num - 1) : "R" + (num + 1);
        }
        return null;
    }

    private BookingSummaryResponse toSummaryResponse(Booking b) {
        BookingSummaryResponse r = new BookingSummaryResponse();
        r.setBookingId(b.getBookingId());
        r.setPnr(b.getPnr());
        r.setBookingReference(b.getBookingReference());
        r.setPassengerName(b.getPassenger().getName());
        r.setPhone(b.getPassenger().getPhone());
        r.setBusNumber(b.getBus().getBusNumber());
        r.setBusName(b.getBus().getBusName());
        r.setOperatorName(b.getBus().getOperator() != null ? b.getBus().getOperator().getName() : null);
        r.setSource(b.getSchedule().getRoute().getSource());
        r.setDestination(b.getSchedule().getRoute().getDestination());
        r.setDepartureTime(b.getSchedule().getDepartureTime());
        r.setArrivalTime(b.getSchedule().getArrivalTime());
        r.setJourneyDate(b.getSchedule().getJourneyDate());
        r.setSeats(b.getSeats().stream().map(Seat::getSeatNumber).toList());
        r.setPassengerCount(b.getPassengers() != null ? b.getPassengers().size() : b.getSeats().size());
        r.setTotalAmount(b.getTotalAmount());
        r.setStatus(b.getStatus().name());
        r.setPaymentStatus(b.getPaymentStatus() != null ? b.getPaymentStatus().name() : null);
        r.setBookingTime(b.getBookingTime());
        r.setRefundAmount(b.getRefundAmount());
        refundService.findLatestForBooking(b.getBookingId())
                .ifPresent(refund -> r.setRefundStatus(refund.getStatus().name()));
        return r;
    }

    private BookingDetailsResponse toDetailsResponse(Booking b) {
        BookingDetailsResponse r = new BookingDetailsResponse();
        r.setBookingId(b.getBookingId());
        r.setPnr(b.getPnr());
        r.setBookingReference(b.getBookingReference());
        r.setPassengerName(b.getPassenger().getName());
        r.setPassengerEmail(b.getPassenger().getEmail());
        r.setBusNumber(b.getBus().getBusNumber());
        r.setBusName(b.getBus().getBusName());
        r.setBusType(b.getBus().getBusType());
        r.setOperatorName(b.getBus().getOperator() != null ? b.getBus().getOperator().getName() : null);
        r.setSource(b.getSchedule().getRoute().getSource());
        r.setDestination(b.getSchedule().getRoute().getDestination());
        r.setDepartureTime(b.getSchedule().getDepartureTime());
        r.setArrivalTime(b.getSchedule().getArrivalTime());
        r.setJourneyDate(b.getSchedule().getJourneyDate());
        r.setSeats(b.getSeats().stream().map(Seat::getSeatNumber).toList());
        r.setPassengers(b.getPassengers().stream().map(this::toPassengerResponse).toList());
        r.setBaseFare(b.getBaseFare());
        r.setBoardingCharges(b.getBoardingCharges());
        r.setConvenienceFee(b.getConvenienceFee());
        r.setTax(b.getTax());
        r.setDiscount(b.getDiscount());
        r.setCouponCode(b.getCoupon() != null ? b.getCoupon().getCode() : null);
        r.setCouponDiscount(b.getCouponDiscount());
        r.setOfferDiscount(b.getOfferDiscount());
        r.setTotalAmount(b.getTotalAmount());
        r.setStatus(b.getStatus().name());
        r.setPaymentStatus(b.getPaymentStatus() != null ? b.getPaymentStatus().name() : null);
        r.setRazorpayOrderId(b.getRazorpayOrderId());
        r.setRazorpayPaymentId(b.getRazorpayPaymentId());
        r.setBoardingPoint(b.getBoardingPoint());
        r.setDropPoint(b.getDropPoint());
        r.setBookingTime(b.getBookingTime());
        r.setCancellationCharge(b.getCancellationCharge());
        r.setRefundAmount(b.getRefundAmount());
        refundService.findLatestForBooking(b.getBookingId()).ifPresent(r::setRefund);
        if (b.getStatus() == BookingStatus.CONFIRMED) {
            try {
                r.setCancellationPolicy(cancellationService.preview(b));
            } catch (Exception ignored) {
                // not cancellable preview
            }
        }
        return r;
    }

    private PassengerResponse toPassengerResponse(BookingPassenger p) {
        PassengerResponse r = new PassengerResponse();
        r.setId(p.getId());
        r.setSeatId(p.getSeat().getId());
        r.setSeatNumber(p.getSeat().getSeatNumber());
        r.setName(p.getName());
        r.setAge(p.getAge());
        r.setGender(p.getGender());
        r.setMobile(p.getMobile());
        r.setEmail(p.getEmail());
        r.setIdType(p.getIdType());
        r.setIdNumber(p.getIdNumber());
        return r;
    }
}