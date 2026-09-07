package com.godwintech.gttravels.service;

import com.godwintech.gttravels.dto.DroppingPointResponse;
import com.godwintech.gttravels.entity.DroppingPoint;
import com.godwintech.gttravels.entity.User;
import com.godwintech.gttravels.repository.DroppingPointRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class OperatorDroppingPointService {

    private final DroppingPointRepository droppingPointRepository;
    private final UserService userService;

    public OperatorDroppingPointService(
            DroppingPointRepository droppingPointRepository,
            UserService userService) {

        this.droppingPointRepository = droppingPointRepository;
        this.userService = userService;
    }

    // ===================================
    // Get Dropping Points for routes served by the current operator
    // ===================================

    public List<DroppingPointResponse> getMyDroppingPoints() {

        User operator = userService.getCurrentUser();

        return droppingPointRepository.findByOperatorId(operator.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ===================================
    // Mapper
    // ===================================

    private DroppingPointResponse mapToResponse(
            DroppingPoint point) {

        return new DroppingPointResponse(

                point.getId(),

                point.getRoute().getId(),

                point.getRoute().getSource()
                        + " → "
                        + point.getRoute().getDestination(),

                point.getPointName(),

                point.getDropTime(),

                point.getSequenceNo()

        );
    }
}