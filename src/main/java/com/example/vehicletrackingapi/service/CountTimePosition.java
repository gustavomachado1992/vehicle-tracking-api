package com.example.vehicletrackingapi.service;

import com.example.vehicletrackingapi.client.ExternalApiClient;
import com.example.vehicletrackingapi.model.dto.PointOfInterest;
import com.example.vehicletrackingapi.model.dto.Posicao;
import com.example.vehicletrackingapi.model.dto.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountTimePosition {

    private final ExternalApiClient externalApiClient;
    private List<PointOfInterest> pointOfInterest = new ArrayList<>();
    //TODO
    public List<Response> getTimesByPOI() {
        var posicoesPorPlaca = externalApiClient.getPosicaoVeiculos().stream()
                .collect(Collectors.groupingBy(Posicao::getPlaca));

        return List.of();
    }

    public List<Response> getTimesByPOI(String placa, String data) {
        pointOfInterest = pointOfInterest.isEmpty() ? externalApiClient.getPontosDeInteresse() : pointOfInterest;
        return externalApiClient.getPosicaoPorPlacaEData(placa, data).stream()
                .map(this::findPoiName)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> Response.builder()
                        .placa(placa)
                        .poi(entry.getKey())
                        .tempo(entry.getValue().intValue()*30)
                        .build()
                )
                .collect(Collectors.toList());
    }

    private String findPoiName(Posicao posicao) {
        return pointOfInterest.stream()
                .filter(poi -> insidePOI(poi, posicao))
                .findFirst()
                .map(PointOfInterest::getNome)
                .orElse(null);
    }

    private boolean insidePOI(PointOfInterest poi, Posicao posicao) {
        double dLat = posicao.getLatitude() - poi.getLatitude();
        double dLon = posicao.getLongitude() - poi.getLongitude();

        return Math.sqrt(dLat * dLat + dLon * dLon) <= poi.getRaio();
    }
}
