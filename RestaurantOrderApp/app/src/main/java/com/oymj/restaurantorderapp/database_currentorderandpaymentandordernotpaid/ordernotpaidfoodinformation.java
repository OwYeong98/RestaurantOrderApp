package com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class ordernotpaidfoodinformation
{
    private String foodname;
    private int quantity;
    private double totalprice;

    public ordernotpaidfoodinformation(String foodname,int quantity,double totalprice)
    {
        this.foodname=foodname;
        this.quantity=quantity;
        this.totalprice=totalprice;
    }

    public String getFoodname() {
        return foodname;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalprice() {
        return totalprice;
    }
}
