package com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid;

/**
 * Created by OwYeong on 10/5/2017.
 */

public class paymentrequestinformation
{
    private String username;
    private int tableno;

    public paymentrequestinformation(String username,int tableno)
    {
        this.username=username;
        this.tableno=tableno;
    }

    public String getUsername() {
        return username;
    }

    public int getTableno() {
        return tableno;
    }
}
