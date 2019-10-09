package com.oymj.restaurantorderapp;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.adminfragment.adminhomepage;
import com.oymj.restaurantorderapp.employeefragment.employeehomepage;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import java.util.ArrayList;

import communicateserverthread.Currentfragment;
import communicateserverthread.logout;

/**
 * Created by OwYeong on 10/11/2017.
 */

public class userspinneradapter extends BaseAdapter {
    Context context;
    String[] spinner = {"username", "Profile", "Log out"};
    LayoutInflater inflater;
    Activity activity;
    adminhomepage adminhomepage;
    employeehomepage employeehomepage;
    customerhomepage customerhomepage;

    public userspinneradapter(Context applicationContext,Activity activity, String name,adminhomepage adminhomepage) {
        this.context = applicationContext;
        this.activity=activity;
        this.adminhomepage=adminhomepage;

        inflater = (LayoutInflater.from(applicationContext));
    }
    public userspinneradapter(Context applicationContext,Activity activity, String name,employeehomepage employeehomepage) {
        this.context = applicationContext;
        this.activity=activity;
        this.employeehomepage=employeehomepage;

        inflater = (LayoutInflater.from(applicationContext));
    }
    public userspinneradapter(Context applicationContext,Activity activity, String name,customerhomepage customerhomepage) {
        this.context = applicationContext;
        this.activity=activity;
        this.customerhomepage=customerhomepage;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return spinner.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        if (spinner[position].equals("username"))
        {
            view = inflater.inflate(R.layout.userspinnerusericon, null);

        }else
        {
            view = inflater.inflate(R.layout.userspinneradapter, null);

            TextView spinnername = (TextView) view.findViewById(R.id.spinnertitle);
            spinnername.setText(spinner[position]);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(position==1)
                    {
                        if(Currentfragment.currentfragmentclassname.contains("admin"))
                        {
                            FragmentTransaction FragTrans;
                            FragTrans = adminhomepage.getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.adminmain_frame, allprofile.newInstance(adminhomepage));
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();
                        }else if (Currentfragment.currentfragmentclassname.contains("employee"))
                        {
                            FragmentTransaction FragTrans;
                            FragTrans = employeehomepage.getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.employeemain_frame, allprofile.newInstance(employeehomepage));
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();
                        }else
                        {
                            FragmentTransaction FragTrans;
                            FragTrans = customerhomepage.getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.customermain_frame, allprofile.newInstance(customerhomepage));
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();
                        }

                    }else if(position==2)
                    {
                        logout logout=new logout(activity);
                        Thread logoutthread=new Thread(logout);

                        logoutthread.start();
                    }
                }
            });
        }


        return view;
    }

    public String getcurrentselected(int position)
    {
        return spinner[position];
    }



}
