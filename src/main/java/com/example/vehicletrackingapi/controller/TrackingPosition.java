package com.example.vehicletrackingapi.controller;

import com.example.vehicletrackingapi.client.ExternalApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TrackingPosition {

    @Autowired
    private ExternalApiClient externalApiClient;

    @GetMapping("/placas")
    public List<String> getPlacas() {
        return externalApiClient.getPlacas();
    }

    @GetMapping("/position")
    public ResponseEntity<String> getPosition() {
       return ResponseEntity.status(HttpStatus.OK).body("hello word");
    }
}
