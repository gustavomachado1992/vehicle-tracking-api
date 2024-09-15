package com.example.vehicletrackingapi.client;

import com.example.vehicletrackingapi.model.dto.PointOfInterest;
import com.example.vehicletrackingapi.model.dto.Posicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ExternalApiClient {

    private final WebClient webClient;

    @Autowired
    public ExternalApiClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://challenge-backend.mobi7.io").build();
    }

    // lista de placas
    public List<String> getPlacas() {
        return webClient.get()
                .uri("/posicao/placas")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<String>>() {})
                .block();  // Executa a chamada de forma síncrona
    }

    // posição de todos os veículos
    public List<Posicao> getPosicaoVeiculos() {
        return webClient.get()
                .uri("/posicao")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Posicao>>() {})
                .block();
    }

    // posição de um veículo específico em uma data
    public List<Posicao> getPosicaoPorPlacaEData(String placa, String data) {
        return webClient.get()
                .uri(buildUri(placa, data))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Posicao>>() {})
                .block();
    }

    // lista de pontos de interesse (POIs)
    public List<PointOfInterest> getPontosDeInteresse() {
        return webClient.get()
                .uri("/pois")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PointOfInterest>>() {})
                .block();
    }

    // detalhes de um POI específico
    public PointOfInterest getPontoDeInteressePorId(String poi) {
        return webClient.get()
                .uri("/pois/{poi}", poi)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<PointOfInterest>() {})
                .block();
    }

    private String buildUri(String placa, String data) {
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
                uriBuilder.append("data=").append(data);
            }
        }

        return uriBuilder.toString();
    }
}


