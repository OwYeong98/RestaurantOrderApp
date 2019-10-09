package com.oymj.restaurantorderapp.adminfragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;

/**
 * Created by OwYeong on 10/13/2017.
 */

public class adminmainscreen extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.admin_mainscreen);

        ImageButton btmnavreport=(ImageButton) findViewById(R.id.bottomnav_report);

        ImageButton btmnaveditfoodmenu=(ImageButton) findViewById(R.id.bottomnav_editfoodmenu);

        ImageButton btmnavhome=(ImageButton) findViewById(R.id.bottomnav_homepage);

        btmnaveditfoodmenu.setOnClickListener(this);
        btmnavreport.setOnClickListener(this);
        btmnavhome.setOnClickListener(this);




        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.adminmain_frame, adminhomepage.newInstance());
        transaction.commit();


    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction;
        switch (v.getId())
        {
            case R.id.bottomnav_editfoodmenu:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.adminmain_frame, admineditfoodmenu.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();


                break;
            case R.id.bottomnav_homepage:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.adminmain_frame, adminhomepage.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.bottomnav_report:
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.adminmain_frame, adminreportselect.newInstance());
                transaction.addToBackStack(null);
                transaction.commit();

                break;


        }
    }
}
