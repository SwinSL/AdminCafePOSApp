package com.example.admincafeposapp.Model;

import java.util.List;

public class Transaction {
    String date;
    List<String> orderID, sales;

    public Transaction() {}

    public Transaction(String date, List<String> orderID, List<String> sales) {
        this.date = date;
        this.orderID = orderID;
        this.sales = sales;
    }

    public String getDate() {
        return date;
    }

    public List<String> getOrderID() {
        return orderID;
    }

    public List<String> getSales() {
        return sales;
    }
}
