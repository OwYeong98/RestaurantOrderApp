package com.oymj.restaurantorderapp.database_foodmenuandcart;

import java.util.ArrayList;

/**
 * Created by OwYeong on 9/9/2017.
 */

//this class store all menu information including the foodmenu and cart
public class foodmenu
{
    //Arraylist that store all the food item with their information
    public static ArrayList<foodinformation> foodlist = new ArrayList<foodinformation>();

    //all food item is store to this arraylist when customer order
    public static ArrayList<foodcartinformation> foodcart = new ArrayList<foodcartinformation>();

    //store foodcart that are confirmed and send to server
    public static ArrayList<foodcartconfirmed> foodcartconfirmed= new ArrayList<foodcartconfirmed>();


    public static void addfooditem(String foodname,String fooddesc,double foodprice,int foodspicy,int containmeat,int foodsalty,int foodsweetness,int foodsour,int chefsuggest,int preparetime,char availableornot)
    {
        foodlist.add(new foodinformation(foodname,fooddesc,foodprice,foodspicy,containmeat,foodsalty,foodsweetness,foodsour,chefsuggest,preparetime,availableornot));
    }

    public static void addfoodcart(String foodname,int quantity, String remark)
    {
        foodcart.add(new foodcartinformation(foodname,quantity,remark));
    }

    public static void addfoodcartconfirmed(int tableno,ArrayList<foodcartinformation> foodordered,double totalpayment,String datenow,boolean cookcondition)
    {
        foodcartconfirmed.add(new foodcartconfirmed(tableno,foodordered,totalpayment,datenow,cookcondition));
    }




}
