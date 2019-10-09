package com.oymj.restaurantorderapp.database_foodmenuandcart;

/**
 * Created by OwYeong on 9/9/2017.
 */

public class foodcartinformation
{
    private String foodname;
    private int quantity;
    private String remark;

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }




    public foodcartinformation(String foodname,int quantity, String remark)
    {
        this.foodname=foodname;
        this.quantity=quantity;
        this.remark=remark;

    }
}
