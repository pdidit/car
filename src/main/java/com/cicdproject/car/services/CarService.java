package com.cicdproject.car.services;

import com.cicdproject.car.dao.CarRepo;
import com.cicdproject.car.dto.Car;
import com.cicdproject.car.dto.DTOCar;
import com.cicdproject.car.exception.CarNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service calls for the car
 */
@Service
public class CarService {

    @Autowired
    private CarRepo carRepo;

    /**
     * To create the car
     * @param car DTOCar car object to create a new car.
     * @return DTOCar returns the new car DTO car
     */
    public DTOCar createCar(DTOCar car){
        Car preCar = new Car();
        BeanUtils.copyProperties(car, preCar);
        Car savedCar = carRepo.save(preCar);
        DTOCar returnCar = new DTOCar();
        BeanUtils.copyProperties(savedCar, returnCar);
        return returnCar;
    }

    /**
     * Retrieves a single car that is requested
     * @param id Integer ID of the car that wants to retrieve.
     * @return DTOCar returns a Car object.
     */
    public DTOCar retrieveSingleCar(Integer id) {
        Optional<Car> car = carRepo.findById(id);
        if(car.isPresent()) {
            DTOCar newCar = new DTOCar();
            BeanUtils.copyProperties(car.get(), newCar);
            return newCar;
        } else {
            throw new CarNotFoundException("Unable to find the car with id " + id);
        }
    }

    /**
     * Returns a list of the cars
     * @return List<DTOCar> returns a list of the Cars.
     */
    public List<DTOCar> getAllCars(){
        //query the client objects and convert to DTOCar to return in the list
        List<DTOCar> returnCars = new ArrayList<>();
        carRepo.findAll().forEach(client -> {
            DTOCar trans = new DTOCar();
            BeanUtils.copyProperties(client, trans);
            returnCars.add(trans);
        });

        return returnCars;
    }

    /**
     * Update the car object.
     * @param id Integer ID of the car being updated.
     * @param updatedCar DTOCar Car Object being updated
     * @return DTOCar returns the updated car DTO car
     */
    public DTOCar updateCar(Integer id, DTOCar updatedCar){
        Car updateCar = carRepo.findById(id)
                .map(oldCar -> {
                    BeanUtils.copyProperties(updatedCar, oldCar);
                    oldCar.setId(id);
                    return carRepo.saveAndFlush(oldCar);
                })
                //if not exist create a new car object.
                .orElseThrow( () -> new CarNotFoundException("Unable to find the car with id " + id));
        //returns the new car that was created
        DTOCar returnCar = new DTOCar();
        BeanUtils.copyProperties(updateCar,returnCar);
        return returnCar;
    }

    /**
     * Delete the car from system.
     * @param id Integer ID to delete in car.
     */
    public void deleteCar(Integer id){
        Optional<Car> car = carRepo.findById(id);
        if(car.isPresent())
            carRepo.deleteById(id);
        else
            throw new CarNotFoundException("Unable to find the car with id " + id);
    }
}
