package com.cicdproject.car.services;

import com.cicdproject.car.dao.CarRepo;
import com.cicdproject.car.dto.Car;
import com.cicdproject.car.dto.Condition;
import com.cicdproject.car.dto.DTOCar;
import com.cicdproject.car.dto.ServiceStatus;
import com.cicdproject.car.exception.CarNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing for the CarService
 */
@DisplayName("Unit test for the car service class")
class CarServiceTest {

    @InjectMocks
    private CarService carService;

    @Mock
    private CarRepo carRepo;

    private Car testCar;

    /**
     * intialize the mock and create a Car object to use in testing.
     * Also mock that is used throughout the testing class.
     */
    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        testCar = new Car();
        testCar.setLitre(2.0);
        testCar.setColour("Grey");
        testCar.setCondition(Condition.USED);
        testCar.setId(1);
        testCar.setMake("Audi");
        testCar.setModel("A4");
        testCar.setMileage(125_000);
        testCar.setServiceStatus(ServiceStatus.SERVICED);
        testCar.setPrice(20_000);
        testCar.setYear(2014);
        testCar.setSeller("Padraic");
        when(carRepo.findById(testCar.getId())).thenReturn(Optional.of(testCar));
    }

    /**
     * Test for the creation of the car
     */
    @Test
    @DisplayName("Testing the creation of a Car")
    void createCarTest(){
        DTOCar newSentCar = new DTOCar();
        newSentCar.setLitre(2.0);
        newSentCar.setColour("Grey");
        newSentCar.setCondition(Condition.USED);
        newSentCar.setMake("Audi");
        newSentCar.setModel("A4");
        newSentCar.setMileage(125_000);
        newSentCar.setServiceStatus(ServiceStatus.SERVICED);
        newSentCar.setPrice(20_000);
        newSentCar.setYear(2014);
        newSentCar.setSeller("Padraic");

        //mock the repository for the creation.
        when(carRepo.save(ArgumentMatchers.any(Car.class))).thenReturn(testCar);

        //confirm that the car service returns correctly.
        DTOCar SavedCar = carService.createCar(newSentCar);
        assertAll(
                () -> assertEquals(2.0, SavedCar.getLitre()),
                () -> assertEquals("Grey", SavedCar.getColour()),
                () -> assertEquals(Condition.USED, SavedCar.getCondition()),
                () -> assertEquals(1, SavedCar.getId() ),
                () -> assertEquals("Audi", SavedCar.getMake()),
                () -> assertEquals("A4", SavedCar.getModel()),
                () -> assertEquals(125_000, SavedCar.getMileage()),
                () -> assertEquals(ServiceStatus.SERVICED, SavedCar.getServiceStatus()),
                () -> assertEquals(20_000, SavedCar.getPrice()),
                () -> assertEquals(2014, SavedCar.getYear()),
                () -> assertEquals("Padraic", SavedCar.getSeller())
        );

    }

    /**
     * test to retrieve a car
     */
    @Test
    @DisplayName("Test the retrieval of one car")
    void retrieveSingleCarTest() {
        //executes the method and testing result.
        DTOCar returnedCar = carService.retrieveSingleCar(1);
        assertAll(
                () -> assertEquals(2.0, returnedCar.getLitre()),
                () -> assertEquals("Grey", returnedCar.getColour()),
                () -> assertEquals(Condition.USED, returnedCar.getCondition()),
                () -> assertEquals(1, returnedCar.getId() ),
                () -> assertEquals("Audi", returnedCar.getMake()),
                () -> assertEquals("A4", returnedCar.getModel()),
                () -> assertEquals(125_000, returnedCar.getMileage()),
                () -> assertEquals(ServiceStatus.SERVICED, returnedCar.getServiceStatus()),
                () -> assertEquals(20_000, returnedCar.getPrice()),
                () -> assertEquals(2014, returnedCar.getYear()),
                () -> assertEquals("Padraic", returnedCar.getSeller())
        );
    }

    /**
     * testing when the car is not existing on retrieval of a single car.
     */
    @Test
    @DisplayName("Test no car exist")
    void getExceptionWhenNoCarExistTest() {
        //test the exception that should be expected.
        Exception exception = assertThrows(CarNotFoundException.class, () -> carService.retrieveSingleCar(100));
        //testing the message that should be returned.
        assertEquals("Unable to find the car with id 100", exception.getMessage());
    }

    /**
     * Test to retrieve all the cars.
     */
    @Test
    @DisplayName("Test to get all the cars")
    void getAllCarsTest() {
        Car testCar2 = new Car();
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

        List<Car> allCars = new ArrayList<>();
        allCars.add(testCar);
        allCars.add(testCar2);

        // mock the retrieval of all the cars.
        when(carRepo.findAll()).thenReturn(allCars);

        // execute of the service method
        List<DTOCar> returnedCar = carService.getAllCars();

        // check the expected size.
        assertEquals(2, returnedCar.size());
    }

    /**
     * testing the updating of the car
     */
    @Test
    @DisplayName("Test updating a car")
    void updateCarTest() {
        //Create a DTOCar object to be sent in
        DTOCar successfulUpdate = new DTOCar();
        successfulUpdate.setLitre(2.0);
        successfulUpdate.setColour("Grey");
        successfulUpdate.setCondition(Condition.USED);
        successfulUpdate.setMake("Audi");
        successfulUpdate.setModel("A4");
        successfulUpdate.setMileage(150_000);
        successfulUpdate.setServiceStatus(ServiceStatus.SERVICED);
        successfulUpdate.setPrice(18_000);
        successfulUpdate.setYear(2014);
        successfulUpdate.setId(1);
        successfulUpdate.setSeller("Padraic");

        // update Car object to be returned in mock
        Car updatedCar = new Car();
        updatedCar.setLitre(2.0);
        updatedCar.setColour("Grey");
        updatedCar.setCondition(Condition.USED);
        updatedCar.setMake("Audi");
        updatedCar.setModel("A4");
        updatedCar.setMileage(150_000);
        updatedCar.setServiceStatus(ServiceStatus.SERVICED);
        updatedCar.setPrice(18_000);
        updatedCar.setYear(2014);
        updatedCar.setId(1);
        updatedCar.setSeller("Padraic");

        // mocking the saving of the updated car.
        when(carRepo.saveAndFlush(ArgumentMatchers.any(Car.class))).thenReturn(updatedCar);

        //execute the update service and confirm the expected results.
        DTOCar updatedDTOCar = carService.updateCar(1, successfulUpdate);
        assertAll(
                () -> assertEquals(2.0, updatedDTOCar.getLitre()),
                () -> assertEquals("Grey", updatedDTOCar.getColour()),
                () -> assertEquals(Condition.USED, updatedDTOCar.getCondition()),
                () -> assertEquals(1, updatedDTOCar.getId() ),
                () -> assertEquals("Audi", updatedDTOCar.getMake()),
                () -> assertEquals("A4", updatedDTOCar.getModel()),
                () -> assertEquals(150_000, updatedDTOCar.getMileage()),
                () -> assertEquals(ServiceStatus.SERVICED, updatedDTOCar.getServiceStatus()),
                () -> assertEquals(18_000, updatedDTOCar.getPrice()),
                () -> assertEquals(2014, updatedDTOCar.getYear()),
                () -> assertEquals("Padraic", updatedDTOCar.getSeller())
        );

    }

    /**
     * Test the update where car doesn't exist
     */
    @Test
    @DisplayName("Test updating a car with non existing Car")
    void updateCarWithNonExistingCarTest() {
        //DTOCar to be updated.
        DTOCar unsuccessfulUpdate = new DTOCar();
        unsuccessfulUpdate.setLitre(2.0);
        unsuccessfulUpdate.setColour("Grey");
        unsuccessfulUpdate.setCondition(Condition.USED);
        unsuccessfulUpdate.setMake("Audi");
        unsuccessfulUpdate.setModel("A4");
        unsuccessfulUpdate.setMileage(150_000);
        unsuccessfulUpdate.setServiceStatus(ServiceStatus.SERVICED);
        unsuccessfulUpdate.setPrice(18_000);
        unsuccessfulUpdate.setYear(2014);
        unsuccessfulUpdate.setSeller("Padraic");

        // test for the expected exception
        Exception exception = assertThrows(CarNotFoundException.class, () -> carService.updateCar(100, unsuccessfulUpdate ));

        //confirm the message to be returned.
        assertEquals("Unable to find the car with id 100", exception.getMessage());
    }

    /**
     * Test of the deletion of the car
     */
    @Test
    @DisplayName("Test deletion of the car")
    void deleteCarTest() {
        carService.deleteCar(1);
        verify(carRepo).deleteById(1);
    }

    /**
     * testing when the car is not existing on deletion of a single car.
     */
    @Test
    @DisplayName("Test deletion of the non existing car")
    void deleteCarThatDoesntExist() {
        //test the exception that should be expected.
        Exception exception = assertThrows(CarNotFoundException.class, () -> carService.deleteCar(100));
        //testing the message that should be returned.
        assertEquals("Unable to find the car with id 100", exception.getMessage());
    }
}