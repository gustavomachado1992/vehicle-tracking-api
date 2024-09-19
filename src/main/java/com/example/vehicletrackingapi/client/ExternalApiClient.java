package com.example.vehicletrackingapi.client;

import com.example.vehicletrackingapi.model.PointOfInterest;
import com.example.vehicletrackingapi.model.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExternalApiClient {

    private final WebClient webClient;

    @Autowired
    public ExternalApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://challenge-backend.mobi7.io").build();
    }

    public List<Position> getPositionByPlateAndDate(String placa, String data) {
        return webClient.get()
                .uri(buildUri(placa, data))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Position>>() {})
                .block();
    }

    public List<PointOfInterest> getPOI() {
        return webClient.get()
                .uri("/pois")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PointOfInterest>>() {})
                .block();
    }

    String buildUri(String placa, String data) {
        StringBuilder uriBuilder = new StringBuilder("/posicao");

        if (placa != null || data != null) {
            uriBuilder.append("?");
            if (placa != null) {
                uriBuilder.append("placa=").append(placa);
            }
            if (data != null) {
                if (placa != null) {
                    uriBuilder.append("&");
                }
                String dataFormat = data.replaceAll("-",  "/") ;
                uriBuilder.append("data=").append(dataFormat);
            }
        }

        return uriBuilder.toString();
    }
}


