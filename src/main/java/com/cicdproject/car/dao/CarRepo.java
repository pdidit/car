package com.cicdproject.car.dao;

import com.cicdproject.car.dto.Car;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Car repository
 */
public interface CarRepo extends JpaRepository<Car, Integer> {
}
