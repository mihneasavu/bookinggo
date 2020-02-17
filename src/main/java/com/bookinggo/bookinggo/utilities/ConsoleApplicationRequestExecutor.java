package com.bookinggo.bookinggo.utilities;

import com.bookinggo.bookinggo.representation.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.util.Collections;

public class ConsoleApplicationRequestExecutor {

    private String baseUrl;

    public ConsoleApplicationRequestExecutor(String url){
        this.baseUrl = url;
    }

    public Response queryProvider(String pickup, String dropoff, String provider) throws RestClientResponseException, ResourceAccessException {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<Response> entity = new HttpEntity<>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(provider)
                .queryParam("pickup", pickup)
                .queryParam("dropoff", dropoff);

        RestTemplate restTemplate = new RestTemplateBuilder().setReadTimeout(Duration.ofSeconds(2)).build();

        ResponseEntity<Response> responseObj = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Response.class);
        return responseObj.getBody();
    }
}
