package com.bookinggo.bookinggo;


import com.bookinggo.bookinggo.representation.Car;
import com.bookinggo.bookinggo.utilities.DataProcessingUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BookingGoConsoleApplication {

    public static final String USAGE = "To run this Application using the console, please provide the following arguments: pickup location, dropoff location and number of passengers." +
            "\n The response will contain the cheapest option for each available vehicle type, all sorted in descending order of price.";

    public static void main(String[] args) {


        if (args.length == 0) {
            SpringApplication.run(BookingGoConsoleApplication.class, args);
        } else {
            if (args.length != 3) {
                System.out.println(USAGE);
            } else {
                final DataProcessingUtility dataProcessingUtility = new DataProcessingUtility("https://techtest.rideways.com");

                String pickup = args[0];
                String dropoff = args[1];
                int numberOfPassengers = Integer.parseInt(args[2]);
                int maxPassengers = dataProcessingUtility.getMaxPassengers();
                if (numberOfPassengers > maxPassengers) {
                    System.out.println("The largest currently available vehicle can only accommodate " + maxPassengers + " passengers.");
                } else {
                    List<Car> processedOptions = dataProcessingUtility.queryAllProviders(pickup, dropoff, numberOfPassengers);
                    if (processedOptions.isEmpty()) {
                        System.out.println("No provider is available");
                    } else {
                        for (Car o : processedOptions) {
                            System.out.println(o.getCar_type() + " - " + o.getProvider() + " - " + o.getPrice());
                        }
                    }
                }
            }

        }
    }

}



