package com.oymj.restaurantorderapp.adminfragment;

import java.util.Random;

/**
 * Created by OwYeong on 10/24/2017.
 */

public class graphviewbarchartdatastore
{
    int storedat;
    String foodname;
    int totalsold;


    Random random = new Random();

    public graphviewbarchartdatastore(int storedat,String foodname)
    {
        this.storedat=storedat;
        this.foodname=foodname;
        this.totalsold=0;


    }

    public int getStoredat() {
        return storedat;
    }

    public String getFoodname() {
        return foodname;
    }

    public void addTotalsold(int value)
    {
        totalsold+=value;
    }


    public int getTotalsold(){ return totalsold; }


}
