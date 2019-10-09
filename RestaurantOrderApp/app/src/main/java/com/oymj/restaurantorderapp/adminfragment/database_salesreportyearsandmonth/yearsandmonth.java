package com.oymj.restaurantorderapp.adminfragment.database_salesreportyearsandmonth;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/21/2017.
 */

public class yearsandmonth
{
    private int year;
    ArrayList<String> month=new ArrayList<String>();

    public yearsandmonth(int year)
    {
        this.year=year;
    }

    public void addmonth(int monthint)
    {
        month.add(Integer.toString(monthint));
    }

    public ArrayList<String> getMonth()
    {
        return month;
    }

    public int getYear()
    {
        return year;
    }


}
