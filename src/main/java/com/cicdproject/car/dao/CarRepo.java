package com.cicdproject.car.dao;

import com.cicdproject.car.dto.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepo extends JpaRepository<Car, Integer> {

    List<Car> findByYear(int year);

    List<Car> findByMakeAndModel(String make, String model);

    List<Car> findByMileageLessThan(int mileage);
}
