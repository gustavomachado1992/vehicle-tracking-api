package com.example.vehicletrackingapi.model.dto;

import lombok.Data;

@Data
public class Posicao {

    private String placa;
    private String dataPosicao;
    private int velocidade;
    private Long latitude;
    private Long longitude;
    private boolean ignicao;

}
