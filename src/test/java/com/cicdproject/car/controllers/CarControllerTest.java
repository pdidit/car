package com.cicdproject.car.controllers;

import com.cicdproject.car.dto.Condition;
import com.cicdproject.car.dto.DTOCar;
import com.cicdproject.car.dto.ServiceStatus;
import com.cicdproject.car.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Testing of the Car controller")
class CarControllerTest {

    @InjectMocks
    private CarController carController;

    @Mock
    private CarService carService;

    @Mock
    HttpServletRequest request;

    private DTOCar dtoCar;

    /**
     * Setup for the testings.
     * Initalizing the mock and create a DTO for testing.
     * @throws Exception
     */
    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
        dtoCar = new DTOCar();
        dtoCar.setLitre(2.0);
        dtoCar.setColour("Grey");
        dtoCar.setCondition(Condition.USED);
        dtoCar.setId(1);
        dtoCar.setMake("Audi");
        dtoCar.setModel("A4");
        dtoCar.setMileage(125_000);
        dtoCar.setServiceStatus(ServiceStatus.SERVICED);
        dtoCar.setPrice(20_000);
        dtoCar.setYear(2014);
        dtoCar.setSeller("Padraic");

    }

    /**
     * Testing the retreival of all the cars
     */
    @Test
    @DisplayName("Test getting all the cars")
    void getCarsTest() {
        DTOCar testCar2 = new DTOCar();
        testCar2.setLitre(2.0);
        testCar2.setColour("Grey");
        testCar2.setCondition(Condition.USED);
        testCar2.setId(1);
        testCar2.setMake("Audi");
        testCar2.setModel("A4");
        testCar2.setMileage(125_000);
        testCar2.setServiceStatus(ServiceStatus.SERVICED);
        testCar2.setPrice(20_000);
        testCar2.setYear(2014);
        testCar2.setSeller("Padraic");

        List<DTOCar> allCars = new ArrayList<>();
        allCars.add(dtoCar);
        allCars.add(testCar2);

        when(carService.getAllCars()).thenReturn(allCars);
        ResponseEntity response = carController.getCars();
        List<DTOCar> returnedCar = (List<DTOCar>) response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, returnedCar.size());

    }

    /**
     * Retrieve a single car call
     */
    @Test
    @DisplayName("Test to retrieve a single car")
    void getCarTest() {
        when(carService.retrieveSingleCar(1)).thenReturn(dtoCar);
        ResponseEntity response = carController.getCar(1);
        DTOCar responseCar = ((DTOCar) response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertAll(
                () -> assertEquals(2.0, responseCar.getLitre()),
                () -> assertEquals("Grey", responseCar.getColour()),
                () -> assertEquals(Condition.USED, responseCar.getCondition()),
                () -> assertEquals(1, responseCar.getId() ),
                () -> assertEquals("Audi", responseCar.getMake()),
                () -> assertEquals("A4", responseCar.getModel()),
                () -> assertEquals(125_000, responseCar.getMileage()),
                () -> assertEquals(ServiceStatus.SERVICED, responseCar.getServiceStatus()),
                () -> assertEquals(20_000, responseCar.getPrice()),
                () -> assertEquals(2014, responseCar.getYear()),
                () -> assertEquals("Padraic", responseCar.getSeller())
        );

    }

    /**
     * call to delete the car
     */
    @Test
    @DisplayName("Test to delete Car from the controller")
    void deleteCarTest() {
        ResponseEntity response = carController.deleteCar(1);
        verify(carService).deleteCar(1);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    /**
     * test on the updating in the controller
     */
    @Test
    @DisplayName("Testing the updating Car from the controller")
    void updateCarTest() {
        when(carService.updateCar(1, dtoCar)).thenReturn(dtoCar);
        ResponseEntity response = carController.updateCar(dtoCar, 1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * Testing the creation of the car from the controller.
     * @throws URISyntaxException Throws in URI incorrrect.
     */
    @Test
    @DisplayName("Testing the creation of the Car from the controller.")
    void createCarTest() throws URISyntaxException {
        when(carService.createCar(ArgumentMatchers.any(DTOCar.class))).thenReturn(dtoCar);
        ResponseEntity response = carController.createCar(dtoCar);

        URI expectedResponse = new URI("/1");
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse, response.getHeaders().getLocation());
    }
}