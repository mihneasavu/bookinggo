package com.bookinggo.bookinggo.utilities;

import com.bookinggo.bookinggo.representation.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Collections;

@Component
public class ConsoleApplicationRequestExecutor {

    private static String baseUrl = "https://techtest.rideways.com/";

    public Response queryProvider(String pickup, String dropoff, String provider) throws RestClientResponseException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Response> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(provider)
                .queryParam("pickup", pickup)
                .queryParam("dropoff", dropoff);

        RestTemplate restTemplate = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(2)).build();

        ResponseEntity<Response> responseObj = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Response.class);
        return responseObj.getBody();
    }
}
