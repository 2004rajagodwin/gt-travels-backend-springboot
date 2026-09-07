package com.godwintech.gttravels.controller;

import com.godwintech.gttravels.dto.OperatorBusResponse;
import com.godwintech.gttravels.service.OperatorBusService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operator")
@PreAuthorize("hasRole('OPERATOR')")
public class OperatorBusController {

    private final OperatorBusService operatorBusService;

    public OperatorBusController(OperatorBusService operatorBusService) {
        this.operatorBusService = operatorBusService;
    }

    @GetMapping("/my-buses")
    public ResponseEntity<List<OperatorBusResponse>> getMyBuses() {

        return ResponseEntity.ok(
                operatorBusService.getMyBuses()
        );
    }
    
    
    
}