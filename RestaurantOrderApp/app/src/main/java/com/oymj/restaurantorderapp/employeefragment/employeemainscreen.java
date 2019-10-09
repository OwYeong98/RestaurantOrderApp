package com.oymj.restaurantorderapp.employeefragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm;
import com.oymj.restaurantorderapp.customerfragment.customerfoodmenu;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.loginfailed;
import com.oymj.restaurantorderapp.serverconnectionerror;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import communicateserverthread.startacceptpacket;

public class employeemainscreen extends AppCompatActivity implements View.OnClickListener {


    BottomNavigationView btmnav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.employee_mainscreen);

        ImageButton homebutton=(ImageButton) findViewById(R.id.bottomnav_homepage);
        ImageButton paymentrequestbutton=(ImageButton) findViewById(R.id.bottomnav_paymentrequest);
        ImageButton orderlistbutton=(ImageButton) findViewById(R.id.bottomnav_orderlist);

        homebutton.setOnClickListener(this);
        paymentrequestbutton.setOnClickListener(this);
        orderlistbutton.setOnClickListener(this);


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.employeemain_frame, employeehomepage.newInstance());
        transaction.commit();




    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction;
        switch (v.getId())
        {
            case R.id.bottomnav_paymentrequest:

                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.employeemain_frame, employeecurrentpaymentrequest.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case R.id.bottomnav_homepage:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.employeemain_frame, employeehomepage.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.bottomnav_orderlist:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.employeemain_frame, employeeorderinqueue.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();

                break;


        }
    }


}
