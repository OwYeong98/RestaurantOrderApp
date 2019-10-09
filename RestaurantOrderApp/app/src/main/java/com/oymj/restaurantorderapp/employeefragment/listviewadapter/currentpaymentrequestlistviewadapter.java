package com.oymj.restaurantorderapp.employeefragment.listviewadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderfoodinformation;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.paymentrequestinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.employeecurrentpaymentrequest;
import com.oymj.restaurantorderapp.employeefragment.employeetablepayment;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/7/2017.
 */

public class currentpaymentrequestlistviewadapter extends BaseAdapter
{
    Context context;
    com.oymj.restaurantorderapp.employeefragment.employeecurrentpaymentrequest employeecurrentpaymentrequest;
    ArrayList<paymentrequestinformation> paymentrequestlist;


    private static LayoutInflater inflater=null;

    public currentpaymentrequestlistviewadapter(employeecurrentpaymentrequest employeecurrentpaymentrequest, ArrayList<paymentrequestinformation> paymentrequestlist) {
        // TODO Auto-generated constructor stub
        context=employeecurrentpaymentrequest.getActivity();
        this.employeecurrentpaymentrequest= employeecurrentpaymentrequest;
        this.paymentrequestlist=paymentrequestlist;

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return paymentrequestlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    TextView tablenotv;
    TextView usernametv;
    TextView totalpricetv;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.employee_currentpaymentrequestlistview, null);
        tablenotv=(TextView) rowView.findViewById(R.id.currentpaymentrequesttablenno);
        usernametv=(TextView) rowView.findViewById(R.id.currentpaymentrequestusername);
        totalpricetv=(TextView) rowView.findViewById(R.id.currentpaymentrequesttotalprice);

        Typeface immonolt =Typeface.createFromAsset(employeecurrentpaymentrequest.getActivity().getAssets(),"font/immonolt.otf");

        tablenotv.setTypeface(immonolt);
        usernametv.setTypeface(immonolt);
        totalpricetv.setTypeface(immonolt);


        tablenotv.setText("Table "+Integer.toString(paymentrequestlist.get(position).getTableno()));
        usernametv.setText(paymentrequestlist.get(position).getUsername());

        ArrayList<currentorderfoodinformation> foodlist=null;
        for (int loop = 0; loop< database_currentrequest.currentorder.size(); loop++)
        {
            if(paymentrequestlist.get(position).getTableno()== database_currentrequest.currentorder.get(loop).getTableno())
            {
                foodlist= database_currentrequest.currentorder.get(loop).getFoodorderlist();
            }
        }

        double totalfoodprice=0;
        for(int loop=0;loop<foodlist.size();loop++)
        {
            double foodprice=0;
            for (int findfoodpriceloop=0;findfoodpriceloop< foodmenu.foodlist.size();findfoodpriceloop++)
            {
                if(foodmenu.foodlist.get(findfoodpriceloop).getFoodname().equals(foodlist.get(loop).getFoodname()))
                {
                    foodprice=foodmenu.foodlist.get(findfoodpriceloop).getFoodprice()*foodlist.get(loop).getQuantity();
                }
            }
            totalfoodprice+=foodprice;
        }
        totalfoodprice+= totalfoodprice*10/100;//total price + 10% tax

        totalpricetv.setText(String.format("%,.2f", totalfoodprice));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FragmentTransaction FragTrans;
                FragTrans = employeecurrentpaymentrequest.getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.employeemain_frame, employeetablepayment.newInstance(paymentrequestlist.get(position).getTableno()));
                FragTrans.addToBackStack(null);
                FragTrans.commit();
            }
        });

        return rowView;
    }
}
