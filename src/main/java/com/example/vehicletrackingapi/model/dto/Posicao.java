package com.example.vehicletrackingapi.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Posicao {

    private String placa;
    private String data;
    private int velocidade;
    private double latitude;
    private double longitude;
    private boolean ignicao;

}
