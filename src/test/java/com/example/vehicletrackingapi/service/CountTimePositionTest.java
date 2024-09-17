package com.example.vehicletrackingapi.service;

import com.example.vehicletrackingapi.client.ExternalApiClient;
import com.example.vehicletrackingapi.model.dto.Posicao;
import java.util.ArrayList;
import java.util.List;

import com.example.vehicletrackingapi.model.dto.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountTimePositionTest {

    @Mock
    private ExternalApiClient externalApiClient;

    @InjectMocks
    private CountTimePosition countTimePosition;

    @Test
    public void getTimesByPOI() {
    }

    @Test
    public void testGetTimesByPOI() {
        String placa = "test1";
        String data = "";
        List<Posicao> posicoes = new ArrayList<>();
        posicoes.add(buildPosition(-25.3649196, -51.4699168));
        posicoes.add(buildPosition(-25.3649196, -51.46992));
        posicoes.add(buildPosition(0, 0));

        when(externalApiClient.getPosicaoPorPlacaEData(placa, data)).thenReturn(posicoes);

        List<Response> response = countTimePosition.getTimesByPOI(placa, data);

        List<Response> expected = List.of(Response.builder().poi("a").tempo(60).placa(placa).build());

        assertEquals(expected, response);
        verify(externalApiClient).getPosicaoPorPlacaEData(placa, data);
    }

    private static Posicao buildPosition(double lat, double lon) {
        return Posicao.builder()
                .latitude(lat)
                .longitude(lon)
                .build();
    }
}