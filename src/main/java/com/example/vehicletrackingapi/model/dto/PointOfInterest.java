package com.example.vehicletrackingapi.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointOfInterest {

    private String nome;
    private int raio;
    private double latitude;
    private double longitude;

}
