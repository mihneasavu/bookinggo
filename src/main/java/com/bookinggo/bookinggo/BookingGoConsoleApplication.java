package com.bookinggo.bookinggo;


import com.bookinggo.bookinggo.representation.Car;
import com.bookinggo.bookinggo.utilities.DataProcessingUtility;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class BookingGoConsoleApplication {



    public static void main(String[] args) {

        if (args.length == 0) {
            SpringApplication.run(BookingGoConsoleApplication.class, args);
        } else {

            String pickup = args[0];
            String dropoff = args[1];
            int numberOfPassengers = Integer.parseInt(args[2]);

            List<Car> processedOptions = DataProcessingUtility.queryAllProviders(pickup, dropoff, numberOfPassengers);
            if (processedOptions.isEmpty()) {
                System.out.println("Could not contact any provider");
            } else {
                for (Car o : processedOptions) {
                    System.out.println(o.getCar_type() + " - " + o.getProvider() + " - " + o.getPrice());
                }
            }
        }
    }


}



