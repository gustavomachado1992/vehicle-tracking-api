package com.example.vehicletrackingapi.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {

    private String placa;
    private String poi;
    private int tempo;

}
