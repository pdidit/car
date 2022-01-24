package com.cicdproject.car.controllers;

import com.cicdproject.car.dto.DTOCar;
import com.cicdproject.car.services.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * This is car controller.
 */
@RestController
public class CarController {

    @Autowired
    private CarService carService;

    /**
     * Returns a list of the cars in the system
     * @return ResponseEntity
     */
    @GetMapping("/cars")
    public ResponseEntity getCars() {
        List<DTOCar> allCars = carService.getAllCars();
        return ResponseEntity.ok(allCars);
    }

    /**
     * Returns a car that is request
     * @param id Integer ID of the car
     * @return ResponseEntity contains status code and car object
     */
    @GetMapping("/cars/{id}")
    public ResponseEntity getCar(@PathVariable Integer id){
        DTOCar car = carService.retrieveSingleCar(id);
        return ResponseEntity.ok(car);
    }

    /**
     * Deletes the car
     * @param id Integer ID of the car being deleted.
     * @return ResponseEntity
     */
    @DeleteMapping("/cars/{id}")
    public ResponseEntity deleteCar(@PathVariable Integer id){
        carService.deleteCar(id);
        return ResponseEntity.accepted().build();
    }

    /**
     * Update the car
     * @param car DTOCar Car object being updated.
     * @param id ID of the car that is getting updated.
     * @return ResponseEntity
     */
    @PutMapping("/cars/{id}")
    public ResponseEntity updateCar(@RequestBody DTOCar car, @PathVariable Integer id) {

        carService.updateCar(id, car);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Creates a new car
     * @param car DTOCar Car object to create
     * @return ResponseEntity Response of the creation.
     */
    @PostMapping("/cars")
    public ResponseEntity createCar(@RequestBody DTOCar car){
        DTOCar newCar = carService.createCar(car);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(newCar.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
}