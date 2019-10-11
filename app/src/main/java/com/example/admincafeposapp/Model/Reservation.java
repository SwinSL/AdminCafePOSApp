package com.example.admincafeposapp.Model;

public class Reservation {
    private String id, customer_surname, table_no, date, time;
    private int noOfPeople;

    public Reservation() {
    }

    public Reservation(String id, String customer_surname, String table_no, String date, String time, int noOfPeople) {
        this.id = id;
        this.customer_surname = customer_surname;
        this.table_no = table_no;
        this.date = date;
        this.time = time;
        this.noOfPeople = noOfPeople;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_surname() {
        return customer_surname;
    }

    public void setCustomer_surname(String customer_surname) {
        this.customer_surname = customer_surname;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(int noOfPeople) {
        this.noOfPeople = noOfPeople;
    }
}
