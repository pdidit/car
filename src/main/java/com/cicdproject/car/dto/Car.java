package com.cicdproject.car.dto;

import javax.persistence.*;

@Entity
@Table(name = "cars")
public class Car
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String make;
    private String model;
    private int year;
    @Column(name="color")
    private String colour;
    private double litre;
    private int mileage;
    private double price;
    @Enumerated(EnumType.STRING)
    private Condition condition;
    private String seller;

    public Car(Integer id, String make, String model, int year, String colour, double litre, int mileage, double price, Condition condition, String seller) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.colour = colour;
        this.litre = litre;
        this.mileage = mileage;
        this.price = price;
        this.condition = condition;
        this.seller = seller;
    }

    public Car() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public double getLitre() {
        return litre;
    }

    public void setLitre(double litre) {
        this.litre = litre;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
