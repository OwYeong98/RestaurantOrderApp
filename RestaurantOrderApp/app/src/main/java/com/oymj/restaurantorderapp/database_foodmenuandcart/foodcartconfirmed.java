package com.oymj.restaurantorderapp.database_foodmenuandcart;

import java.util.ArrayList;

/**
 * Created by OwYeong on 9/26/2017.
 */

public class foodcartconfirmed
{
    private int tableno;
    private ArrayList<foodcartinformation> foodordered = new ArrayList<foodcartinformation>();
    private double totalpayment;
    private String datenow;
    private boolean cookcondition;
    private String datestartcook=null;
    private boolean cookdone=false;
    private int estimatedtime;

    public foodcartconfirmed(int tableno,ArrayList<foodcartinformation> foodordered,double totalpayment,String datenow,boolean cookcondition)
    {
        this.tableno=tableno;
        this.foodordered=foodordered;
        this.totalpayment=totalpayment;
        this.datenow=datenow;
        this.cookcondition=cookcondition;
        countestimatedtime();
    }


    public void countestimatedtime(){
        estimatedtime=0;

        for(int loop=0;loop<foodordered.size();loop++)
        {
            String foodname=foodordered.get(loop).getFoodname();
            int foodpreparetime;
            int timeneed=0;

            //find food info
            for(int foodloop=0;foodloop<foodmenu.foodlist.size();foodloop++)
            {
                if (foodmenu.foodlist.get(foodloop).getFoodname().equals(foodname))
                {
                    foodpreparetime=foodmenu.foodlist.get(foodloop).getPreparetime();
                    timeneed=foodpreparetime * foodordered.get(loop).getQuantity();
                }
            }
            estimatedtime+=timeneed;
        }
    }

    public int getTableno() {
        return tableno;
    }

    public void setTableno(int tableno) {
        this.tableno = tableno;
    }

    public ArrayList<foodcartinformation> getFoodordered() {
        return foodordered;
    }

    public void setFoodordered(ArrayList<foodcartinformation> foodordered) {
        this.foodordered = foodordered;
    }

    public double getTotalpayment() {
        return totalpayment;
    }

    public void setTotalpayment(double totalpayment) {
        this.totalpayment = totalpayment;
    }

    public String getDatenow() {
        return datenow;
    }

    public void setDatenow(String datenow) {
        this.datenow = datenow;
    }

    public boolean isCookcondition() {
        return cookcondition;
    }

    public void setCookcondition(boolean cookcondition) {
        this.cookcondition = cookcondition;
    }

    public void setDatestartcook(String datestartcook)
    {
        this.datestartcook=datestartcook;
    }

    public String getDatestartcook(){
        return datestartcook;
    }

    public boolean isCookdone() {
        return cookdone;
    }

    public void setCookdone(boolean cookdone) {
        this.cookdone = cookdone;
    }

    public void setEstimatedtime(int estimatedtime) {
        this.estimatedtime = estimatedtime;
    }

    public int getEstimatedtime()
    {
        return estimatedtime;
    }

}
