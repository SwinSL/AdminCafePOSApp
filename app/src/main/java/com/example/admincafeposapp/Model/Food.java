package com.example.admincafeposapp.Model;

public class Food {
    private String item_name;
    private double item_price;

    public Food()
    {}

    public Food(String name, double price)
    {
        this.item_name = name;
        this.item_price = price;
    }

    public void setItem_name(String item_name)
    {
        this.item_name = item_name;
    }

    public void setPrice(double price)
    {
        this.item_price = price;
    }

    public String getItem_name()
    {
        return item_name;
    }

    public double getPrice()
    {
        return item_price;
    }
}
