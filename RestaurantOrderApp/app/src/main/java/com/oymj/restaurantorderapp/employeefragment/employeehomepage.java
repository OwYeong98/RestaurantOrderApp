package com.oymj.restaurantorderapp.employeefragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.oymj.restaurantorderapp.LoginScreen;
import com.oymj.restaurantorderapp.R;

import communicateserverthread.Currentfragment;
import communicateserverthread.getnamefromserver;
import communicateserverthread.logout;

public class employeehomepage extends Fragment implements View.OnClickListener {


    Spinner userspinner;
    TextView showusername;


    public static employeehomepage newInstance() {
        employeehomepage fragment = new employeehomepage();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.employee_homepage,container,false);


        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.employee_topeditpagetitle);

        topemployeeeditpagetitle.setTypeface(insanibu);


        userspinner = (Spinner) rootView.findViewById(R.id.userspinner);

        TextView welcomeuser= (TextView) rootView.findViewById(R.id.welcomeuser);
        showusername= (TextView) rootView.findViewById(R.id.showusername);

        welcomeuser.setTypeface(dsfatty);
        showusername.setTypeface(dsfatty);

        ImageButton orderlistbutton= (ImageButton) rootView.findViewById(R.id.orderlistbutton);
        ImageButton paymentrequestbutton= (ImageButton) rootView.findViewById(R.id.paymentrequestbutton);
        ImageButton setfoodavailabilitybutton= (ImageButton) rootView.findViewById(R.id.setfoodavailabilitybutton);
        ImageButton ordernotpaidlistbutton= (ImageButton) rootView.findViewById(R.id.ordernotpaidlistbutton);

        orderlistbutton.setOnClickListener(this);
        paymentrequestbutton.setOnClickListener(this);
        setfoodavailabilitybutton.setOnClickListener(this);
        ordernotpaidlistbutton.setOnClickListener(this);


        //get name of user to display
        getnamefromserver getnamefromserver= new getnamefromserver(this);
        Thread getname =new Thread(getnamefromserver);
        getname.start();




        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.orderlistbutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.employeemain_frame, employeeorderinqueue.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
            case R.id.paymentrequestbutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.employeemain_frame, employeecurrentpaymentrequest.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
            case R.id.setfoodavailabilitybutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.employeemain_frame, employeefoodavailabilitycontrol.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
            case R.id.ordernotpaidlistbutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.employeemain_frame, employeeordernotpaid.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
