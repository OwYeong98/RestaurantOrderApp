package com.oymj.restaurantorderapp.customerfragment;

import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabHost;

import com.oymj.restaurantorderapp.R;

public class customermainscreen extends AppCompatActivity implements View.OnClickListener {

    Intent intent;
    Bundle extras;
    Spinner userspinner;
    private String clientname;
    BottomNavigationView btmnav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customer_mainscreen);

        ImageButton btmnavfoodmenu=(ImageButton) findViewById(R.id.bottomnav_foodmenu);

        ImageButton btmnavmyorder=(ImageButton) findViewById(R.id.bottomnav_myorder);

        ImageButton btmnavhome=(ImageButton) findViewById(R.id.bottomnav_homepage);

        btmnavfoodmenu.setOnClickListener(this);
        btmnavmyorder.setOnClickListener(this);
        btmnavhome.setOnClickListener(this);



        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.customermain_frame, customerhomepage.newInstance());
        transaction.commit();



    }


    @Override
    public void onClick(View v) {
        FragmentTransaction transaction;

        switch (v.getId())
        {
            case R.id.bottomnav_foodmenu:


                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.customermain_frame, customerfoodmenu.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();

                break;
            case R.id.bottomnav_homepage:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.customermain_frame, customerhomepage.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.bottomnav_myorder:
                if(customerpayment.callbefore==true)
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.customermain_frame, customerpayment.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else if (customercartconfirmed.callbefore==true)
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.customermain_frame, customercartconfirmed.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else
                {
                    transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.customermain_frame, customercartnotconfirm.newInstance());
                    transaction.addToBackStack(null);
                    transaction.commit();
                }

                break;


        }
    }




}
