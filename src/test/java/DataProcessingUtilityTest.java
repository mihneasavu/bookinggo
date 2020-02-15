import builder.OptionBuilder;
import builder.ResponseBuilder;
import com.bookinggo.bookinggo.representation.Car;
import com.bookinggo.bookinggo.representation.Response;
import com.bookinggo.bookinggo.utilities.DataProcessingUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import org.springframework.test.context.junit4.SpringRunner;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
public class DataProcessingUtilityTest {

    private static final String localhostUrl = "http://localhost:8080/";
    private static final String PICKUP = "3.410632,-2.157533";
    private static final String DROPOFF = "3.410632,-2.157533";
    private static final int PRICE = 100000;
    private static final String CAR_TYPE = "LUXURY";

    private List<Car> carList = new ArrayList<>();
    private Response singleProviderResponse;
    private WireMockServer wireMockServer;

    @Before
    public void setup() {
        singleProviderResponse = null;
        carList.clear();
        wireMockServer = new WireMockServer();
        wireMockServer.start();
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }


    @Test
    public void shouldReturnEmptyOptionalForInvalidProviderName() throws JsonProcessingException {
        givenAPIRespondsWitAValidResponseForValidProvider();
        whenIQueryForInvalidProvider();
        thenResponseIsEmpty();
        verify(getRequestedFor(anyUrl()));

    }

    @Test
    public void shouldReturnValidResponseForSingleProviderQuery() throws JsonProcessingException {
        givenAPIRespondsWitAValidResponseForValidProvider();
        whenIQueryForProvider("dave");
        thenResponseIsAsExpected("dave");
    }

    @Test
    public void shouldReturnCheapestOptionForCarType() throws JsonProcessingException {
        givenAPIRespondsWitAValidResponseForMultipleProviders();
        whenIQueryForAllProviders();
        thenCheapestOptionIsReturnedForCAR_TYPE();
    }

    @Test
    public void shouldHandleErrorFromProvider() throws JsonProcessingException {
        givenAPIRespondsWithErrorForAProvider();
        whenIQueryForAllProviders();
        thenAnOptionIsStillReturnedFromTheOtherProvider();
    }

    private void thenCheapestOptionIsReturnedForCAR_TYPE() {
        assertEquals(carList.get(0).getPrice().intValue(), 100000);
    }

    private void thenAnOptionIsStillReturnedFromTheOtherProvider() {
        assertEquals(carList.get(0).getProvider(), "dave");
    }


    private void thenResponseIsEmpty() {
        assertNull(singleProviderResponse);
    }

    private void thenResponseIsAsExpected(String provider) {
        assertEquals(PICKUP,singleProviderResponse.getPickup());
        assertEquals(DROPOFF,singleProviderResponse.getDropoff());
        assertEquals(provider, singleProviderResponse.getSupplier_id());
        assertEquals(PRICE, singleProviderResponse.getOptions().get(0).getPrice().intValue());
    }

    private void whenIQueryForInvalidProvider() {
        Optional<Response> response =new DataProcessingUtility(localhostUrl).queryProvider(PICKUP, DROPOFF, 2, "invalidProvider");
        response.ifPresent(value -> singleProviderResponse = value);
    }

    private void whenIQueryForProvider(String provider) {
        Optional<Response> response = new DataProcessingUtility(localhostUrl).queryProvider(PICKUP, DROPOFF, 2, provider);
        response.ifPresent(value -> singleProviderResponse = value);
    }

    private void whenIQueryForAllProviders () {
        carList = new DataProcessingUtility(localhostUrl).queryAllProviders(PICKUP, DROPOFF, 2);
    }

    private void givenAPIRespondsWitAValidResponseForValidProvider() throws JsonProcessingException {
        wireMockServer.stubFor(get(urlPathEqualTo("/dave"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(
                                new ResponseBuilder()
                                        .withSupplier_id("dave")
                                        .withPickup(PICKUP)
                                        .withDropoff(DROPOFF)
                                        .withOption(new OptionBuilder()
                                                .withPrice(100000)
                                                .withCarType(CAR_TYPE).build())
                                        .build()))));

    }

    private void givenAPIRespondsWitAValidResponseForMultipleProviders() throws JsonProcessingException {
        wireMockServer.stubFor(get(urlPathEqualTo("/dave"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(
                                new ResponseBuilder()
                                        .withSupplier_id("dave")
                                        .withPickup(PICKUP)
                                        .withDropoff(DROPOFF)
                                        .withOption(new OptionBuilder()
                                                .withPrice(100000)
                                                .withCarType(CAR_TYPE).build())
                                        .build()))));

        wireMockServer.stubFor(get(urlPathEqualTo("/eric"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(
                                new ResponseBuilder()
                                        .withSupplier_id("eric")
                                        .withPickup(PICKUP)
                                        .withDropoff(DROPOFF)
                                        .withOption(new OptionBuilder()
                                                .withPrice(200000)
                                                .withCarType(CAR_TYPE).build())
                                        .build()))));

    }

    private void givenAPIRespondsWithErrorForAProvider() throws JsonProcessingException {
        wireMockServer.stubFor(get(urlPathEqualTo("/dave"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(new ObjectMapper().writeValueAsString(
                                new ResponseBuilder()
                                        .withSupplier_id("dave")
                                        .withPickup(PICKUP)
                                        .withDropoff(DROPOFF)
                                        .withOption(new OptionBuilder()
                                                .withPrice(100000)
                                                .withCarType(CAR_TYPE).build())
                                        .build()))));

        wireMockServer.stubFor(get(urlPathEqualTo("/eric"))
                .willReturn(aResponse()
                        .withStatus(500)));
    }
}