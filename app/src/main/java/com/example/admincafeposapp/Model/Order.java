package com.example.admincafeposapp.Model;

import java.util.ArrayList;

public class Order {
    private String order_id, table_no;
    private double order_total;
    private ArrayList<OrderItem> orderItemArrayList;
    private String order_date, order_status;
    private Boolean isMember;

    public Order() {
    }

    public Order(String order_id, String table_no, double order_total, ArrayList<OrderItem> orderItems, String date) {
        this.order_id = order_id;
        this.table_no = table_no;
        this.order_total = order_total;
        this.orderItemArrayList = orderItems;
        this.order_date = date;
        this.order_status = "Not Paid";
        this.isMember = false;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public Boolean getIsMember() {
        return isMember;
    }

    public void setIsMember(Boolean member) {
        isMember = member;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }

    public ArrayList<OrderItem> getOrderItemArrayList() {
        return orderItemArrayList;
    }

    public void setOrderItemArrayList(ArrayList<OrderItem> orderItemArrayList) {
        this.orderItemArrayList = orderItemArrayList;
    }
}