package com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class ordernotpaid
{
    private String username;
    private String date;
    private double totalpayment;
    ArrayList<ordernotpaidfoodinformation> foodordered;

    public ordernotpaid(String username,String date,double totalpayment,ArrayList<ordernotpaidfoodinformation> foodordered)
    {
        this.username=username;
        this.date=date;
        this.totalpayment=totalpayment;
        this.foodordered=foodordered;
    }

    public String getUsername() {
        return username;
    }

    public String getDate() {
        return date;
    }

    public double getTotalpayment() {
        return totalpayment;
    }

    public ArrayList<ordernotpaidfoodinformation> getFoodordered() {
        return foodordered;
    }
}
