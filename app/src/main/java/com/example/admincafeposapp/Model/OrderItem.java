package com.example.admincafeposapp.Model;

import androidx.annotation.Nullable;

public class OrderItem extends MenuItem {
    private int item_quantity;

    public OrderItem() {
    }

    public OrderItem(String item_name, double item_price, int item_quantity) {
        super(item_name, item_price);
        this.item_quantity = item_quantity;
    }

    public int getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(int item_quantity) {
        this.item_quantity = item_quantity;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return (this.getItem_name().equals(((OrderItem) obj).getItem_name()));
    }
}
