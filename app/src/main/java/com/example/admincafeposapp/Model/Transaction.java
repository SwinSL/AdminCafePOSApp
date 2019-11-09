package com.example.admincafeposapp.Model;

import java.util.List;

public class Transaction {
    String date;
    List<String> orderID, sales;
    List<Boolean> member;

    public Transaction() {}

    public Transaction(String date, List<String> orderID, List<String> sales, List<Boolean> member) {
        this.date = date;
        this.orderID = orderID;
        this.sales = sales;
        this.member = member;
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

    public List<Boolean> getMember() {
        return member;
    }
}
