package com.example.vehicletrackingapi.service;

import com.example.vehicletrackingapi.client.ExternalApiClient;
import com.example.vehicletrackingapi.model.PointOfInterest;
import com.example.vehicletrackingapi.model.Position;
import com.example.vehicletrackingapi.model.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountTimePositionTest {

    @Mock
    private ExternalApiClient externalApiClient;

    @InjectMocks
    private CountTimePosition countTimePosition;

    @Test
    public void testGetTimesByPOI_SinglePosition() {
        String placa = "TESTE001";
        String data = "2023-09-10T10:00:00Z";

        List<Position> positions = Arrays.asList(
                buildPosition(placa, data, -25.5244493, -51.549662)
        );
        List<PointOfInterest> pois = Collections.singletonList(
                buildPointOfInterest()
        );

        when(externalApiClient.getPositionByPlateAndDate(placa, data)).thenReturn(positions);
        when(externalApiClient.getPOI()).thenReturn(pois);

        List<Response> responses = countTimePosition.getTimesByPOI(placa, data);

        List<Response> expected = new ArrayList<>();
        assertEquals(expected, responses);
    }

    @Test
    public void testGetTimesByPOI_MultiplePositions() {
        String placa = "TESTE002";
        String data = "2023-09-10T10:00:00Z";

        List<Position> positions = Arrays.asList(
                buildPosition(placa, "2023-09-10T10:00:00Z", -25.5244493, -51.549662),
                buildPosition(placa, "2023-09-10T10:05:00Z", -25.5244493, -51.549662)
        );

        PointOfInterest poi =  buildPointOfInterest();

        when(externalApiClient.getPositionByPlateAndDate(placa, data)).thenReturn(positions);
        when(externalApiClient.getPOI()).thenReturn(Collections.singletonList(poi));

        List<Response> responses = countTimePosition.getTimesByPOI(placa, data);

        List<Response> expected = List.of(Response.builder().poi("PONTO 24").tempo(5).placa(placa).build());
        assertEquals(expected, responses);
    }
    @Test
    public void testGetTimesByPOI_MultiplePositionsWithOnePoi() {
        String placa = null;
        String data = "2023-09-10T10:00:00Z";

        List<Position> positions = Arrays.asList(
            buildPosition("TESTE001", "2023-09-10T10:00:00Z", -25.5244493, -51.549662),
            buildPosition("TESTE001", "2023-09-10T10:15:00Z", -25.5244493, -51.549662),
            buildPosition("TESTE001", "2023-09-10T10:20:00Z", -25.0244493, -25.0244493),
            buildPosition("TESTE002", "2023-09-10T10:05:00Z", -25.0244493, -51.049662)
        );

        PointOfInterest poi =  buildPointOfInterest();

        when(externalApiClient.getPositionByPlateAndDate(placa, data)).thenReturn(positions);
        when(externalApiClient.getPOI()).thenReturn(Collections.singletonList(poi));

        List<Response> responses = countTimePosition.getTimesByPOI(null, data);

        List<Response> expected = List.of(Response.builder().poi("PONTO 24").tempo(15).placa("TESTE001").build());
        assertEquals(expected, responses);
    }
    @Test
    public void testInsidePOI() {
        PointOfInterest poi = buildPointOfInterest();
        Position position = buildPosition("TESTE001", "2023-09-10T10:00:00Z", -25.5244493, -51.549662);

        boolean result = CountTimePosition.insidePOI(poi, position);

        assertTrue(result);
    }

    @Test
    public void testNotInsidePOI() {
        PointOfInterest poi = buildPointOfInterest();
        Position position =  buildPosition("TESTE001", "2023-09-10T10:00:00Z", -25.5000000, -51.5000000);
        boolean result = CountTimePosition.insidePOI(poi, position);

        assertFalse(result);
    }

    private static Position buildPosition(String placa, String date, double lat, double lon) {
        return Position.builder()
                .placa(placa)
                .data(date)
                .latitude(lat)
                .longitude(lon)
                .ignicao(false)
                .velocidade(5)
                .build();
    }
    private static PointOfInterest buildPointOfInterest() {
        return PointOfInterest.builder()
                .nome("PONTO 24")
                .raio(350)
                .latitude(-25.5244493)
                .longitude(-51.549662)
                .build();
    }
}