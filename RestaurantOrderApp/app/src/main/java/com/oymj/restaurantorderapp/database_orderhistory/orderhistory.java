package com.oymj.restaurantorderapp.database_orderhistory;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/16/2017.
 */

public class orderhistory
{
    private int orderid;
    private double totalpayment;
    private String date;
    private ArrayList<foodorderhistory> foodordered=new ArrayList<foodorderhistory>();

    public orderhistory(int orderid,double totalpayment,String date,ArrayList<foodorderhistory> foodordered)
    {
        this.orderid=orderid;
        this.totalpayment=totalpayment;
        this.date=date;
        this.foodordered=foodordered;
    }

    public int getOrderid() {
        return orderid;
    }

    public double getTotalpayment() {
        return totalpayment;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<foodorderhistory> getFoodordered() {
        return foodordered;
    }
}
