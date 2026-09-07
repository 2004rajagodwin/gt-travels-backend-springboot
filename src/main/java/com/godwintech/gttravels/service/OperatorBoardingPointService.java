package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.BoardingPointResponse;
import com.godwintech.gttravels.entity.BoardingPoint;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.repository.BoardingPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OperatorBoardingPointService {

    private final BoardingPointRepository boardingPointRepository;
    private final UserService userService;

    public OperatorBoardingPointService(
            BoardingPointRepository boardingPointRepository,
            UserService userService) {

        this.boardingPointRepository = boardingPointRepository;
        this.userService = userService;
    }

    // ===================================
    // Get Boarding Points for routes served by the current operator
    // ===================================

    public List<BoardingPointResponse> getMyBoardingPoints() {

        User operator = userService.getCurrentUser();

        return boardingPointRepository.findByOperatorId(operator.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===================================
    // Mapper
    // ===================================

    private BoardingPointResponse mapToResponse(
            BoardingPoint point) {

        return new BoardingPointResponse(

                point.getId(),

                point.getRoute().getId(),

                point.getRoute().getSource()
                        + " → "
                        + point.getRoute().getDestination(),

                point.getPointName(),

                point.getPickupTime(),

                point.getSequenceNo()

        );
    }
}