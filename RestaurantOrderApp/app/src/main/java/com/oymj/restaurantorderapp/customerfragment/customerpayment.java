package com.oymj.restaurantorderapp.customerfragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.listviewadapter.paymentfoodlistviewadapter;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;

public class customerpayment extends Fragment{

    public ListView paymentfoodlist;
    public static boolean callbefore=false;

    private double counttotalprice;


    public static customerpayment newInstance() {
        callbefore=true;
        customerpayment fragment = new customerpayment();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.customer_payment,container,false);

        TextView taxprice= (TextView) rootView.findViewById(R.id.orderfulldetailtaxprice);
        TextView totalprice=(TextView) rootView.findViewById(R.id.orderfulldetailtotalprice);
        TextView gstprice=(TextView) rootView.findViewById(R.id.orderfulldetailgstprice);


        paymentfoodlist=(ListView) rootView.findViewById(R.id.paymentfoodlist);

        paymentfoodlist.setAdapter(new paymentfoodlistviewadapter(this, foodmenu.foodcartconfirmed.get(0).getFoodordered()));

        counttotalprice=0;

        //loop foodcart which store all cart information to calculate total price
        for (int loop=0;loop<foodmenu.foodcartconfirmed.get(0).getFoodordered().size();loop++)
        {
            //find food price from food list which store all information of food
            for(int innerloop=0;innerloop<foodmenu.foodlist.size();innerloop++)
            {
                if(foodmenu.foodcartconfirmed.get(0).getFoodordered().get(loop).getFoodname().equals(foodmenu.foodlist.get(innerloop).getFoodname()))
                {
                    double pricemulquantity=foodmenu.foodcartconfirmed.get(0).getFoodordered().get(loop).getQuantity()*foodmenu.foodlist.get(innerloop).getFoodprice();
                    counttotalprice+=pricemulquantity;
                }
            }
        }

        double counttaxprice=counttotalprice * 10/100;//10% tax
        double countgstprice=counttotalprice * 6/100;//6%tax

        taxprice.setText("Tax(10%): RM"+String.format("%,.2f", counttotalprice*10/100));
        gstprice.setText("GST(6%): RM"+String.format("%,.2f", counttotalprice*6/100));

        counttotalprice+=counttotalprice*16/100;

        totalprice.setText("Total: RM"+String.format("%,.2f", counttotalprice));



        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
