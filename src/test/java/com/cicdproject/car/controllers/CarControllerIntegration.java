package com.cicdproject.car.controllers;

import com.cicdproject.car.dto.Condition;
import com.cicdproject.car.dto.DTOCar;
import com.cicdproject.car.dto.ServiceStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("integration test of the car application")
public class CarControllerIntegration {

    @LocalServerPort
    private Integer port;

    private String baseURL = "http://localhost";


    private static RestTemplate testRestTemplate = null;

    /**
     * initaling the Rest Template
     */
    @BeforeAll
    static void init() {
        testRestTemplate = new RestTemplate();
        testRestTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(HttpStatus statusCode) {
                return false;
            }
        });
    }

    /**
     * seting up the uri to be used.
     */
    @BeforeEach
    void setup(){

        baseURL = baseURL.concat(":").concat(port.toString()).concat("/cars");
    }

    /**
     * Test for getting all the list of cars
     */
    @Test
    @DisplayName("Integration test to get all the cars")
    void shouldReturnAListCars(){
        ResponseEntity<List> response = testRestTemplate.getForEntity(baseURL, List.class);
        List<DTOCar> listDTOCar = (List<DTOCar>) response.getBody();

        assertEquals(20, listDTOCar.size());
    }

    /**
     * test to get back a single car
     */
    @Test
    @DisplayName("Integration test for a single car")
    void shouldReturnAClient() {
        String url = baseURL.concat("/1");
        ResponseEntity response = testRestTemplate.getForEntity(url, DTOCar.class );
        DTOCar car = (DTOCar) response.getBody();

        //confirm the execpeted results.
        assertAll(
                () -> assertEquals(1.8, car.getLitre()),
                () -> assertEquals("Red", car.getColour()),
                () -> assertEquals(Condition.USED, car.getCondition()),
                () -> assertEquals(1, car.getId() ),
                () -> assertEquals("Audi", car.getMake()),
                () -> assertEquals("A4", car.getModel()),
                () -> assertEquals(91_300, car.getMileage()),
                () -> assertEquals(ServiceStatus.SERVICE_REQUIRED, car.getServiceStatus()),
                () -> assertEquals(3_000, car.getPrice()),
                () -> assertEquals(1999, car.getYear()),
                () -> assertEquals("sregan22", car.getSeller())
        );
    }

    /**
     * Integration test for the creation of a new car
     * @throws URISyntaxException
     */
    @Test
    @DisplayName("Integration test for the creation of car")
    public void testCreationOfCar() throws URISyntaxException {
        DTOCar dtoCar = new DTOCar();
        dtoCar.setLitre(2.0);
        dtoCar.setColour("Grey");
        dtoCar.setCondition(Condition.USED);
        dtoCar.setMake("Audi");
        dtoCar.setModel("A4");
        dtoCar.setMileage(125_000);
        dtoCar.setServiceStatus(ServiceStatus.SERVICED);
        dtoCar.setPrice(20_000);
        dtoCar.setYear(1999);
        dtoCar.setSeller("Padraic");
        ResponseEntity response = testRestTemplate.postForEntity(baseURL,dtoCar,DTOCar.class);

        //Confirm that new location is returned
        URI expectedResponse = new URI("http://localhost:"+port+"/cars/21");
        assertEquals(expectedResponse, response.getHeaders().getLocation());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    /**
     * Integrated test to update a car
     */
    @Test
    @DisplayName("Integration Test to update the car")
    public void testToUpdateCar() throws URISyntaxException {
        DTOCar dtoCar = new DTOCar();
        dtoCar.setLitre(1.8);
        dtoCar.setColour("Red");
        dtoCar.setCondition(Condition.USED);
        dtoCar.setMake("Audi");
        dtoCar.setModel("A4");
        dtoCar.setMileage(100_300);
        dtoCar.setServiceStatus(ServiceStatus.SERVICE_REQUIRED);
        dtoCar.setPrice(2_000);
        dtoCar.setYear(2014);
        dtoCar.setSeller("Padraic");

        //send request to update car and check
        HttpEntity<DTOCar> requestUpdate = new HttpEntity<>(dtoCar);
        String url = baseURL + "/1";
        ResponseEntity response = testRestTemplate.exchange(url,HttpMethod.PUT, requestUpdate, DTOCar.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Integrated Test for the deletion of car
     */
    @Test
    @DisplayName("Integration test to delete a car")
    public void testToDeleteACar(){
        String url = baseURL + "/2";
        ResponseEntity delResponse = testRestTemplate.exchange(url,HttpMethod.DELETE, null, DTOCar.class);

        assertEquals(HttpStatus.ACCEPTED, delResponse.getStatusCode());
    }
}
