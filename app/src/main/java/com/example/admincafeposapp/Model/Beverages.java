package com.example.admincafeposapp.Model;

public class Beverages {

    private String name;
    private double price;

    public Beverages()
    {}

    public Beverages(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setPrice(double price)
    {
        this.name = name;
    }

    public double getPrice()
    {
        return price;
    }
}
