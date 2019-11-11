package com.example.admincafeposapp.Model;

import java.util.List;

public class Transaction {
    String date;
    List<String> orderID, sales,orderStatus;
    List<Boolean> member;

    public Transaction() {}

    public Transaction(String date, List<String> orderID, List<String> sales, List<Boolean> member,List<String> orderStatus) {
        this.date = date;
        this.orderID = orderID;
        this.sales = sales;
        this.orderStatus = orderStatus;
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

    public List<String> getOrderStatus() {
        return orderStatus;
    }

    public List<Boolean> getMember() {
        return member;
    }

    public void reset() {
        orderID.clear();
        sales.clear();
        orderStatus.clear();
        member.clear();
    }
}
