package com.cicdproject.car.controllers;

import com.cicdproject.car.dao.CarRepo;
import com.cicdproject.car.dto.Car;
import com.cicdproject.car.exception.CarNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class CarController {

    @Autowired
    private CarRepo carRepo;

    @GetMapping("cars")
    public List<Car> getCars() {
        return carRepo.findAll();
    }

    @GetMapping("cars/{id}")
    public Optional<Car> getCar(@PathVariable Integer id){
        Optional<Car> car = carRepo.findById(id);
        if(car.isPresent())
            return car;
        else
            throw new CarNotFoundException("Unable to find the car with id " + id);
    }

    @DeleteMapping("cars/{id}")
    public void deleteCar(@PathVariable Integer id){
        Optional<Car> car = carRepo.findById(id);
        if(car.isPresent())
            carRepo.deleteById(id);
        else
            throw new CarNotFoundException("Unable to find the car with id " + id);
    }

    @PostMapping("cars")
    public ResponseEntity createCar(@RequestBody Car car){
        Car SavedCar = carRepo.save(car);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(car.getId()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("cars/{id}")
    public ResponseEntity updateCar(@RequestBody Car car, @PathVariable Integer id) {
        Optional<Car> oldCar = carRepo.findById(id);
        if(oldCar.isPresent()) {
            car.setId(id);
            carRepo.save(car);
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            car.setId(id);
            Car SavedCar = carRepo.save(car);

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest().path("{id}")
                    .buildAndExpand(car.getId()).toUri();
            return ResponseEntity.created(location).build();
        }
    }

    @GetMapping("cars/srcyear")
    public List<Car> getCarsFromYear(@RequestParam int year){
        return carRepo.findByYear(year);
    }

    @GetMapping("cars/srcmile")
    public List<Car> getCarsByMileage(@RequestParam int mileage){
        return carRepo.findByMileageLessThan(mileage);
    }

    @GetMapping("cars/srcmakemodel")
    public List<Car> getCarsByMakeAndModel(@RequestParam String make, @RequestParam String model){
        return carRepo.findByMakeAndModel(make,model);
    }

}
