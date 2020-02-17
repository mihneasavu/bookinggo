package com.bookinggo.bookinggo.controllers;

import com.bookinggo.bookinggo.representation.Car;
import com.bookinggo.bookinggo.representation.Response;
import com.bookinggo.bookinggo.utilities.DataProcessingUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.properties")
@RestController
public class BookingGoController {

    private final String baseUrl = "https://techtest.rideways.com/";

    private DataProcessingUtility dataProcessingUtility = new DataProcessingUtility(baseUrl);

    @GetMapping("/rideways")
    public ResponseEntity<String> queryAllProviders(@RequestParam(value = "pickup") String pickup, @RequestParam(value = "dropoff") String dropoff, @RequestParam(value = "passengers") String passengers) throws JsonProcessingException {
        int numberOfPassengers = Integer.parseInt(passengers);
        int maxPassengers = dataProcessingUtility.getMaxPassengers();
        if (numberOfPassengers > maxPassengers) {
            return new ResponseEntity<>("The largest currently available vehicle can only accommodate " + maxPassengers + " passengers.", HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            List<Car> processedOptions = dataProcessingUtility.queryAllProviders(pickup, dropoff, Integer.parseInt(passengers));
            return new ResponseEntity<>(new ObjectMapper().writeValueAsString(processedOptions), HttpStatus.OK);
        }
    }

    @GetMapping("/rideways/{provider}")
    public ResponseEntity<String> queryProvider(@PathVariable("provider") String provider,
                                               @RequestParam(value = "pickup") String pickup,
                                               @RequestParam(value = "dropoff") String dropoff,
                                               @RequestParam(value = "passengers") String passengers) throws JsonProcessingException {

        int numberOfPassengers = Integer.parseInt(passengers);
        int maxPassengers = dataProcessingUtility.getMaxPassengers();

        if(!dataProcessingUtility.providerValid(provider)){
            return new ResponseEntity<>("Invalid provider.",HttpStatus.NOT_FOUND);
        }else if (numberOfPassengers > maxPassengers) {
            return new ResponseEntity<>("The largest currently available vehicle can only accommodate " + maxPassengers + " passengers.", HttpStatus.UNPROCESSABLE_ENTITY);
        } else {
            Optional<Response> response = dataProcessingUtility.queryProvider(pickup, dropoff, Integer.parseInt(passengers), provider);
            if (response.isPresent()) {
                return new ResponseEntity<>(new ObjectMapper().writeValueAsString(response.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Could not contact provider.", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }
}