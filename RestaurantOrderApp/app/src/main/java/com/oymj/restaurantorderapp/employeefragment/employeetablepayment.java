package com.oymj.restaurantorderapp.employeefragment;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderfoodinformation;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.tablepaymentlistviewadapter;

import java.util.ArrayList;

import communicateserverthread.Currentfragment;
import communicateserverthread.employeecommunicate.paymentdone;
import communicateserverthread.employeecommunicate.paymentnotdone;

/**
 * Created by OwYeong on 10/7/2017.
 */

public class employeetablepayment extends Fragment implements View.OnClickListener
{
    int tableno;
    ArrayList<currentorderfoodinformation> foodlist;
    public static employeetablepayment newInstance(int tableno) {

        Bundle bundle=new Bundle();
        bundle.putString("tableno",Integer.toString(tableno));

        employeetablepayment fragment = new employeetablepayment();
        fragment.setArguments(bundle);

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.employee_tablepayment,container,false);

        ImageButton paymentdoneimgbutton=(ImageButton) rootView.findViewById(R.id.paymentdone);
        ImageButton paymentnotdoneimgbutton=(ImageButton) rootView.findViewById(R.id.paymentnotdone);

        paymentdoneimgbutton.setOnClickListener(this);
        paymentnotdoneimgbutton.setOnClickListener(this);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        Typeface immonolt =Typeface.createFromAsset(getActivity().getAssets(),"font/immonolt.otf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.employee_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        tableno= Integer.parseInt(getArguments().getString("tableno"));


        TextView tablenotv=(TextView) rootView.findViewById(R.id.orderfulldetailtableno);
        tablenotv.setTypeface(immonolt);
        tablenotv.setText("Table: "+tableno);

        TextView taxprice= (TextView) rootView.findViewById(R.id.orderfulldetailtaxprice);
        TextView gstprice= (TextView) rootView.findViewById(R.id.orderfulldetailgstprice);
        TextView totalprice=(TextView) rootView.findViewById(R.id.orderfulldetailtotalprice);

        taxprice.setTypeface(immonolt);
        gstprice.setTypeface(immonolt);
        totalprice.setTypeface(immonolt);

        for (int loop = 0; loop< database_currentrequest.currentorder.size(); loop++)
        {
            if(tableno== database_currentrequest.currentorder.get(loop).getTableno())
            {
                foodlist= database_currentrequest.currentorder.get(loop).getFoodorderlist();
            }
        }

        double totalfoodprice=0;
        //find total price
        for(int loop=0;loop<foodlist.size();loop++)
        {
            double foodprice=0;
            for (int findfoodpriceloop = 0; findfoodpriceloop< foodmenu.foodlist.size(); findfoodpriceloop++)
            {
                if(foodmenu.foodlist.get(findfoodpriceloop).getFoodname().equals(foodlist.get(loop).getFoodname()))
                {
                    foodprice=foodmenu.foodlist.get(findfoodpriceloop).getFoodprice()*foodlist.get(loop).getQuantity();
                }
            }
            totalfoodprice+=foodprice;
        }

        taxprice.setText("Tax(10%): RM"+String.format("%,.2f", totalfoodprice*10/100));
        gstprice.setText("GST(6%): RM"+String.format("%,.2f", totalfoodprice*6/100));

        totalfoodprice+=totalfoodprice*16/100;

        totalprice.setText("Total: RM"+String.format("%,.2f", totalfoodprice));


        ListView paymentlist=(ListView) rootView.findViewById(R.id.tablepaymentlistview);

        paymentlist.setAdapter(new tablepaymentlistviewadapter(this, database_currentrequest.currentorder,tableno));


        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.paymentdone:
                paymentdone paymentdonethread=new paymentdone(this,Integer.parseInt(getArguments().getString("tableno")));
                Thread PDthread=new Thread(paymentdonethread);

                PDthread.start();

                break;
            case R.id.paymentnotdone:
                paymentnotdone paymentnotdonethread=new paymentnotdone(this,Integer.parseInt(getArguments().getString("tableno")));
                Thread PNthread=new Thread(paymentnotdonethread);

                PNthread.start();

                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
