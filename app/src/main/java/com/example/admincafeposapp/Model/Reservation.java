package com.example.admincafeposapp.Model;

public class Reservation {
    private String id, customer_surname, table_no, date, start_time, end_time;
    private int noOfPeople;

    public Reservation() {
    }

    public Reservation(String id, String customer_surname, String table_no, String date, String start_time, String end_time, int noOfPeople) {
        this.id = id;
        this.customer_surname = customer_surname;
        this.table_no = table_no;
        this.date = date;
        this.start_time = start_time;
        this.end_time = end_time;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(int noOfPeople) {
        this.noOfPeople = noOfPeople;
    }
}
