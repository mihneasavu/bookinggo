package com.bookinggo.bookinggo.utilities;

import com.bookinggo.bookinggo.representation.Car;
import com.bookinggo.bookinggo.representation.Option;
import com.bookinggo.bookinggo.representation.Response;
import org.springframework.web.client.RestClientResponseException;

import java.util.*;
import java.util.stream.Collectors;

public class DataProcessingUtility {

    private static ConsoleApplicationRequestExecutor controller = new ConsoleApplicationRequestExecutor();


    private static Map<String, Integer> carCapacityMap = new HashMap<String, Integer>() {{
        put("STANDARD", 4);
        put("EXECUTIVE", 4);
        put("LUXURY", 4);
        put("PEOPLE_CARRIER", 6);
        put("LUXURY_PEOPLE_CARRIER", 6);
        put("MINIBUS", 16);
    }};
    private static List<String> providers = new ArrayList<String>() {{
        add("dave");
        add("eric");
        add("jeff");
    }};

    public static Boolean providerValid(String provider){
        return provider.contains(provider);
    }

    public static List<Car> queryAllProviders(String pickup, String dropoff, int numberOfPassengers) {
        List<Response> responseList = new ArrayList<>();

        for (String provider : providers) {
            try {
                responseList.add(controller.queryProvider(pickup, dropoff, provider));
            } catch (RestClientResponseException e) {
                System.out.println("An error occurred contacting provider " + provider + ": " + e.getRawStatusCode());
            }
        }

        return DataProcessingUtility.processedReturnedOptions(responseList, numberOfPassengers);

    }

    public static Optional<Response> queryProvider(String pickup, String dropoff, int numberOfPassengers, String provider) {
        try {
            Response response = controller.queryProvider(pickup, dropoff, provider);
            response.setOptions(response.getOptions().stream()
                    .filter(o -> carCapacityMap.get(o.getCar_type()) >= numberOfPassengers)
                    .collect(Collectors.toList()));
            return Optional.of(response);
        } catch (RestClientResponseException e) {
            System.out.println("An error occurred contacting provider " + provider + ": " + e.getRawStatusCode());
        }
            return Optional.empty();
    }



    private static List<Car> processedReturnedOptions(List<Response> responseList, int passengers) {

        Map<String, Car> carMap = new HashMap<>();

        for (Response r : responseList){
            for (Option o : r.getOptions()) {
                Car car = new Car(r.getSupplier_id(), o);
                if (carMap.containsKey(car.getCar_type())){
                    if (car.getPrice() < carMap.get(car.getCar_type()).getPrice()){
                        carMap.put(car.getCar_type(), car);
                    }
                } else {
                    carMap.put(car.getCar_type(), car);
                }
            }
        }
        return carMap.values().stream()
                .sorted(Comparator.comparing(Option::getPrice).reversed())
                .filter(option -> carCapacityMap.get(option.getCar_type()) >= passengers)
                .collect(Collectors.toList());

    }
}
