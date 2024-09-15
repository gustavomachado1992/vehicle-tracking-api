package com.example.vehicletrackingapi.model.dto;

import lombok.Data;

@Data
public class PointOfInterest {

    private String nome;
    private int raio;
    private Long latitude;
    private Long longitude;

}
