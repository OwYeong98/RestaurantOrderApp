package com.oymj.restaurantorderapp.employeefragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.database_orderhistory.orderhistory;
import com.oymj.restaurantorderapp.database_orderhistory.orderhistoryrecord;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.ordernotpaidlistviewadapter;

import communicateserverthread.Currentfragment;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class employeeordernotpaid extends Fragment
{
    public static employeeordernotpaid newInstance() {
        employeeordernotpaid fragment = new employeeordernotpaid();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.employee_ordernotpaid,container,false);

        ListView orderlist=(ListView) rootView.findViewById(R.id.ordernotpaidlistview);

        orderlist.setAdapter(new ordernotpaidlistviewadapter(this, orderhistoryrecord.ordernotpaid));

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.employee_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);


        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
