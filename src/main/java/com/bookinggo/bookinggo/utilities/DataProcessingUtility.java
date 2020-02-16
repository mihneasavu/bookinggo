package com.bookinggo.bookinggo.utilities;

import com.bookinggo.bookinggo.representation.Car;
import com.bookinggo.bookinggo.representation.Option;
import com.bookinggo.bookinggo.representation.Response;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;

import java.util.*;
import java.util.stream.Collectors;

public class DataProcessingUtility {

    private ConsoleApplicationRequestExecutor executor;

    public DataProcessingUtility(String url) {
        this.executor = new ConsoleApplicationRequestExecutor(url);
    }

    private final Map<String, Integer> carCapacityMap = new HashMap<String, Integer>() {{
        put("STANDARD", 4);
        put("EXECUTIVE", 4);
        put("LUXURY", 4);
        put("PEOPLE_CARRIER", 6);
        put("LUXURY_PEOPLE_CARRIER", 6);
        put("MINIBUS", 16);
    }};
    private final List<String> providers = new ArrayList<String>() {{
        add("dave");
        add("eric");
        add("jeff");
    }};

    public int getMaxPassengers(){
        return Collections.max(this.carCapacityMap.values());
    }

    public Boolean providerValid(String provider){
        return provider.contains(provider);
    }

    public List<Car> queryAllProviders(String pickup, String dropoff, int numberOfPassengers) {
        List<Response> responseList = new ArrayList<>();

        for (String provider : providers) {
            try {
                responseList.add(executor.queryProvider(pickup, dropoff, provider));
            } catch (RestClientResponseException e) {
                System.out.println("An error occurred contacting provider " + provider + ": " + e.getRawStatusCode());
            } catch (ResourceAccessException e) {
                System.out.println("provider " + provider + " timed out: " + e.getMessage());
            }
        }

        return processedReturnedOptions(responseList, numberOfPassengers);

    }

    public Optional<Response> queryProvider(String pickup, String dropoff, int numberOfPassengers, String provider) {
        try {
            Response response = executor.queryProvider(pickup, dropoff, provider);
            response.setOptions(response.getOptions().stream()
                    .filter(o -> carCapacityMap.get(o.getCar_type()) >= numberOfPassengers)
                    .collect(Collectors.toList()));
            return Optional.of(response);
        } catch (RestClientResponseException e) {
            System.out.println("An error occurred contacting provider " + provider + ": " + e.getRawStatusCode());
        } catch (ResourceAccessException e) {
            System.out.println("provider " + provider + " timed out: " + e);
        }
            return Optional.empty();
    }

    private List<Car> processedReturnedOptions(List<Response> responseList, int passengers) {

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
