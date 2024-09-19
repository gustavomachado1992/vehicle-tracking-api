package com.example.vehicletrackingapi.client;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExternalApiClientTest {

    @InjectMocks
    private ExternalApiClient externalApiClient;

    @Test
    void testBuildUriWithPlacaAndData() {
        String uri = externalApiClient.buildUri("TESTE001", "2023-09-10");
        assertEquals("/posicao?placa=TESTE001&data=2023/09/10", uri);
    }

    @Test
    void testBuildUriWithOnlyPlaca() {
        String uri = externalApiClient.buildUri("TESTE001", null);
        assertEquals("/posicao?placa=TESTE001", uri);
    }

    @Test
    void testBuildUriWithOnlyData() {
        String uri = externalApiClient.buildUri(null, "2023-09-10");
        assertEquals("/posicao?data=2023/09/10", uri);
    }

    @Test
    void testBuildUriWithNoParams() {
        String uri = externalApiClient.buildUri(null, null);
        assertEquals("/posicao", uri);
    }

}
