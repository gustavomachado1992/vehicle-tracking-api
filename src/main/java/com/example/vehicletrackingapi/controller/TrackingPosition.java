package com.example.vehicletrackingapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrackingPosition {
    @GetMapping("/position")
    public ResponseEntity<String> getPosition() {
       return ResponseEntity.status(HttpStatus.OK).body("hello word");
    }
}
