package com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid;

/**
 * Created by OwYeong on 10/2/2017.
 */

public class currentorderfoodinformation
{
    private String foodname;
    private String remark;
    private int quantity;

    public currentorderfoodinformation(String foodname,String remark,int quantity)
    {
        this.foodname=foodname;
        this.remark=remark;
        this.quantity=quantity;
    }

    public String getFoodname() {
        return foodname;
    }

    public String getRemark() {
        return remark;
    }

    public int getQuantity() {
        return quantity;
    }
}
