package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.BoardingPointResponse;
import com.godwintech.gttravels.dto.DroppingPointResponse;
import com.godwintech.gttravels.dto.OperatorBookingResponse;
import com.godwintech.gttravels.service.OperatorBoardingPointService;
import com.godwintech.gttravels.service.OperatorDroppingPointService;
import com.godwintech.gttravels.service.OperatorService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operator")
@PreAuthorize("hasRole('OPERATOR')")
public class OperatorController {

    private final OperatorService operatorService;
    private final OperatorBoardingPointService operatorBoardingPointService;
    private final OperatorDroppingPointService operatorDroppingPointService;

    public OperatorController(
            OperatorService operatorService,
            OperatorBoardingPointService operatorBoardingPointService,
            OperatorDroppingPointService operatorDroppingPointService) {

        this.operatorService = operatorService;
        this.operatorBoardingPointService = operatorBoardingPointService;
        this.operatorDroppingPointService = operatorDroppingPointService;
    }

    // ==========================
    // Passenger Bookings
    // ==========================

    @GetMapping("/bookings")
    public List<OperatorBookingResponse> getBookings() {

        return operatorService.getBookings();
    }

    // ==========================
    // Boarding Points
    // ==========================

    @GetMapping("/boarding-points")
    public List<BoardingPointResponse> getBoardingPoints() {

        return operatorBoardingPointService.getMyBoardingPoints();
    }

    // ==========================
    // Dropping Points
    // ==========================

    @GetMapping("/dropping-points")
    public List<DroppingPointResponse> getDroppingPoints() {

        return operatorDroppingPointService.getMyDroppingPoints();
    }
}