package com.example.admincafeposapp.Model;

public class MenuItem {
    private String item_name;
    private double item_price;

    public MenuItem() {
    }

    public MenuItem(String item_name, double item_price) {
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public double getItem_price() {
        return item_price;
    }

    public void setItem_price(double item_price) {
        this.item_price = item_price;
    }
}
