package com.example.vehicletrackingapi.controller;

import com.example.vehicletrackingapi.model.dto.Response;
import com.example.vehicletrackingapi.service.CountTimePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrackingPosition {

    @Autowired
    private CountTimePosition countTimePosition;

    @GetMapping("/position/placa/{placa}/data/{data}")
    public List<Response> getPosition(@PathVariable String placa, @PathVariable String data) {
        return countTimePosition.getTimesByPOI(placa, data);
    }
}
