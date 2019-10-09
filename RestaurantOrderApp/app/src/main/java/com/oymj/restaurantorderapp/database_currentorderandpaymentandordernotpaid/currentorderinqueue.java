package com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/2/2017.
 */

public class currentorderinqueue
{
    private int tableno;
    private boolean cooking;
    private boolean cookdone;
    private ArrayList<currentorderfoodinformation> foodorderlist;
    private String cookstarttime=null;
    private String dateordered;

    public currentorderinqueue(int tableno,boolean cooking,boolean cookdone,String dateordered,ArrayList<currentorderfoodinformation> foodorderlist)
    {
        this.tableno=tableno;
        this.foodorderlist=foodorderlist;
        this.cooking=cooking;
        this.cookdone=cookdone;
        this.dateordered=dateordered;
    }

    public int getTableno() {
        return tableno;
    }

    public ArrayList<currentorderfoodinformation> getFoodorderlist() {
        return foodorderlist;
    }

    public boolean getCooking()
    {
        return cooking;
    }

    public boolean getcookdone() {
        return cookdone;
    }

    public void setcookdone(boolean cookdone) {
        this.cookdone = cookdone;
    }

    public void setCookstarttime(String cookstarttime)
    {
        this.cookstarttime=cookstarttime;
    }


    public String getCookstarttime()
    {
        return cookstarttime;
    }

    public String getDateordered(){
        return dateordered;
    }

}
