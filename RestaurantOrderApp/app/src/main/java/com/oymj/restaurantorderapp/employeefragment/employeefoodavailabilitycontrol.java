package com.oymj.restaurantorderapp.employeefragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.foodavailabilitycontrollistviewadapter;

import communicateserverthread.Currentfragment;
import communicateserverthread.getnamefromserver;
import communicateserverthread.logout;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class employeefoodavailabilitycontrol extends Fragment
{
    public static employeefoodavailabilitycontrol newInstance() {
        employeefoodavailabilitycontrol fragment=new employeefoodavailabilitycontrol();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.employee_foodavailabilitycontrol,container,false);

        ListView foodavailabilitycontrollistview= (ListView) rootView.findViewById(R.id.foodavailabilitycontrollistview);

        foodavailabilitycontrollistviewadapter foodavailablecontroladapter=new foodavailabilitycontrollistviewadapter(this, foodmenu.foodlist);

        foodavailabilitycontrollistview.setAdapter(foodavailablecontroladapter);

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
