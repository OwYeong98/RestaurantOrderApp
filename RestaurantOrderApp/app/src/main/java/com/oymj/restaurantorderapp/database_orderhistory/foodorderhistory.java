package com.oymj.restaurantorderapp.database_orderhistory;

/**
 * Created by OwYeong on 10/16/2017.
 */

public class foodorderhistory
{
    private String foodname;
    private int quantity;

    public foodorderhistory(String foodname,int quantity)
    {
        this.foodname=foodname;
        this.quantity=quantity;
    }

    public String getFoodname() {
        return foodname;
    }

    public int getQuantity() {
        return quantity;
    }
}
