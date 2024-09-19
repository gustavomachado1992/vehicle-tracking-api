package com.example.vehicletrackingapi.service;

import com.example.vehicletrackingapi.client.ExternalApiClient;
import com.example.vehicletrackingapi.model.PointOfInterest;
import com.example.vehicletrackingapi.model.Position;
import com.example.vehicletrackingapi.model.Response;
import lombok.RequiredArgsConstructor;
import org.geotools.referencing.GeodeticCalculator;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountTimePosition {

    private final ExternalApiClient externalApiClient;
    private List<PointOfInterest> pointOfInterest = new ArrayList<>();

    public List<Response> getTimesByPOI(String placa, String data) {
        return externalApiClient.getPositionByPlateAndDate(placa, data).stream()
                .collect(Collectors.groupingBy(Position::getPlaca))
                .values().stream()
                .map(positions -> getTimesByPOI(positions.get(0).getPlaca(), positions))
                .flatMap(List::stream)
                .toList();
    }

    private List<Response> getTimesByPOI(String placa, List<Position> positions) {
        pointOfInterest = pointOfInterest.isEmpty() ? externalApiClient.getPOI() : pointOfInterest;
        return pointOfInterest.stream()
                .map(poi ->  Response.builder()
                        .placa(placa)
                        .poi(poi.getNome())
                        .tempo(getTimesByPOI(positions, poi))
                        .build()
                )
                .filter(r -> r.getTempo() > 0)
                .toList();
    }

    private long getTimesByPOI(List<Position> positions, PointOfInterest poi) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        Duration totalDuration = Duration.ZERO;
        LocalDateTime previousDate = null;

        for (Position position : positions) {
            LocalDateTime currentDate = LocalDateTime.parse(position.getData(), formatter);
            boolean insidePOI = insidePOI(poi, position);
            if (previousDate != null && insidePOI) {
                Duration duration = Duration.between(previousDate, currentDate);
                totalDuration = totalDuration.plus(duration);
            }

            previousDate = insidePOI ? currentDate : null;
        }

        return totalDuration.toMinutes();
    }

    static boolean insidePOI(PointOfInterest poi, Position position) {
        GeodeticCalculator calculator = new GeodeticCalculator();
        calculator.setStartingGeographicPoint(poi.getLatitude(), poi.getLongitude());  // Definindo ponto central
        calculator.setDestinationGeographicPoint(position.getLatitude(), position.getLongitude()); // Definindo ponto a verificar

        double distance = calculator.getOrthodromicDistance();
        return distance <= poi.getRaio();
    }

}
