package com.example.vehicletrackingapi.controller;

import com.example.vehicletrackingapi.model.Response;
import com.example.vehicletrackingapi.service.CountTimePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TrackingPosition {

    @Autowired
    private CountTimePosition countTimePosition;

    @GetMapping("/position")
    @CrossOrigin(origins = "http://localhost:4200")
    public List<Response> getPositionAll(
            @RequestParam(value = "placa", required = false) String placa,
            @RequestParam(value = "data", required = false) String data) {
        return countTimePosition.getTimesByPOI(placa, data);
    }
}
