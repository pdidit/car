package com.cicdproject.car;

import com.cicdproject.car.dto.Car;
import com.cicdproject.car.dto.ServiceStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CarApplicationTests {

    @LocalServerPort
    private Integer port;

    private String baseURL = "http://localhost";

    private static RestTemplate testRestTemplate = null;

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

    @BeforeEach
    void setup(){
        baseURL = baseURL.concat(":").concat(port.toString()).concat("/cars");
    }

    @Test
    void shouldReturnAListClients(){
        Car[] clients = testRestTemplate.getForObject(baseURL, Car[].class);
        assertAll(
                () -> assertNotNull(clients)
        );
    }

    @Test
    void shouldReturnAClient(){
        String url = baseURL.concat("/1");
        Car car = testRestTemplate.getForObject(url, Car.class);
        assertAll(
                () -> {
                    assert car != null;
                    assertEquals("Audi", car.getMake());
                }

        );
    }

    @Test
    void serviceCarSuccess(){
        String url = baseURL.concat("/service/1/100300");
        ResponseEntity<Car> responseEntity = testRestTemplate.getForEntity(url, Car.class);
        assertAll(
                () -> {
                    assertEquals(200, responseEntity.getStatusCodeValue());
                }

        );
    }
    @Test
    void serviceCarWrongCar(){
        String url = baseURL.concat("/service/100/100300");
        ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(url, String.class);
        assertAll(
                () -> {
                    assertEquals(404, responseEntity.getStatusCodeValue());
                    assertEquals(true, responseEntity.getBody().contains("Car was not found"));
                }

        );
    }
}
