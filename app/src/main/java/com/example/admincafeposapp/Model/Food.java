package com.example.admincafeposapp.Model;

public class Food {
    private String name;
    private double price;

    public Food()
    {}

    public Food(String name, double price)
    {
        this.name = name;
        this.price = price;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }
}
