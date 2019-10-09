package com.oymj.restaurantorderapp.database_foodmenuandcart;

/**
 * Created by OwYeong on 9/9/2017.
 */

public class foodinformation
{
    private String foodname;
    private String fooddesc;
    private double foodprice;
    private int foodspicy;
    private int containmeat;
    private int foodsalty;
    private int foodsweetness;
    private int foodsour;
    private int chefsuggest;

    private int preparetime;
    private char availableornot;

    public foodinformation(String foodname,String fooddesc,double foodprice,int foodspicy,int containmeat,int foodsalty,int foodsweetness,int foodsour,int chefsuggest,int preparetime,char availableornot)
    {
        this.foodname=foodname;
        this.fooddesc=fooddesc;
        this.foodprice=foodprice;
        this.foodspicy=foodspicy;
        this.containmeat=containmeat;
        this.foodsalty=foodsalty;
        this.foodsweetness=foodsweetness;
        this.chefsuggest=chefsuggest;
        this.foodsour=foodsour;
        this.preparetime=preparetime;
        this.availableornot=availableornot;
    }


    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFooddesc() {
        return fooddesc;
    }

    public void setFooddesc(String fooddesc) {
        this.fooddesc = fooddesc;
    }

    public double getFoodprice() {
        return foodprice;
    }

    public void setFoodprice(double foodprice) {
        this.foodprice = foodprice;
    }

    public int getFoodspicy() {
        return foodspicy;
    }



    public void setFoodspicy(int foodspicy) {
        this.foodspicy = foodspicy;
    }

    public int getFoodsour() {
        return foodsour;
    }

    public void setFoodsour(int foodsour) {
        this.foodsour = foodsour;
    }

    public int getContainmeat() {
        return containmeat;
    }

    public void setContainmeat(int containmeat) {
        this.containmeat = containmeat;
    }

    public int getFoodsalty() {
        return foodsalty;
    }

    public void setFoodsalty(int foodsalty) {
        this.foodsalty = foodsalty;
    }

    public int getFoodsweetness() {
        return foodsweetness;
    }

    public void setFoodsweetness(int foodsweetness) {
        this.foodsweetness = foodsweetness;
    }

    public int getChefsuggest() {
        return chefsuggest;
    }

    public void setChefsuggest(int chefsuggest) {
        this.chefsuggest = chefsuggest;
    }

    public int getPreparetime() {
        return preparetime;
    }

    public void setPreparetime(int preparetime) {
        this.preparetime = preparetime;
    }

    public char getAvailableornot() {
        return availableornot;
    }

    public void setAvailableornot(char availableornot) {
        this.availableornot = availableornot;
    }
}
