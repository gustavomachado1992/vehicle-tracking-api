package com.example.vehicletrackingapi.service;

import com.example.vehicletrackingapi.client.ExternalApiClient;
import com.example.vehicletrackingapi.model.dto.PointOfInterest;
import com.example.vehicletrackingapi.model.dto.Posicao;
import com.example.vehicletrackingapi.model.dto.Response;
import lombok.RequiredArgsConstructor;
import org.geotools.referencing.GeodeticCalculator;
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
    public List<Response> getTimesByPOI(String placa, String data) {
        var posicoesPorPlaca = externalApiClient.getPosicaoPorPlacaEData(placa, data).stream()
                .collect(Collectors.groupingBy(Posicao::getPlaca))
                .values().stream()
                .map(positions -> getTimesByPOI(positions.get(0).getPlaca(), positions))
                .flatMap(List::stream)
                .toList();

        return posicoesPorPlaca;
    }

    public List<Response> getTimesByPOI(String placa, List<Posicao> positions) {
        pointOfInterest = pointOfInterest.isEmpty() ? externalApiClient.getPontosDeInteresse() : pointOfInterest;
        return positions.stream()
                .map(this::findPoiName)
                .flatMap(List::stream)
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

    private List<String> findPoiName(Posicao posicao) {
        System.out.println(pointOfInterest);
        return pointOfInterest.stream()
                .filter(poi -> insidePOI(poi, posicao))
                .map(PointOfInterest::getNome)
                .toList();
    }


    public static boolean insidePOI(PointOfInterest poi, Posicao posicao) {
        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(poi.getLatitude(), poi.getLongitude());  // Definindo ponto central
        calculator.setDestinationGeographicPoint(posicao.getLatitude(), posicao.getLongitude()); // Definindo ponto a verificar

        double distance = calculator.getOrthodromicDistance(); // Distância em km
        System.out.println(poi.getNome());
        return distance <= poi.getRaio();
    }

}
