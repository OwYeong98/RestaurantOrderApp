package com.oymj.restaurantorderapp.customerfragment;

import android.content.Intent;
import android.graphics.Typeface;
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
import android.widget.Toast;

import com.oymj.restaurantorderapp.LoginScreen;
import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.employeefragment.employeeordernotpaid;

import org.w3c.dom.Text;

import communicateserverthread.Currentfragment;
import communicateserverthread.getnamefromserver;
import communicateserverthread.logout;

public class customerhomepage extends Fragment implements View.OnClickListener{


    Spinner userspinner;
    TextView showusername;

    private String clientname;

    public static customerhomepage newInstance() {
        customerhomepage fragment = new customerhomepage();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.customer_homepage,container,false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        userspinner = (Spinner) rootView.findViewById(R.id.userspinner);
        ImageButton foodmenubutton= (ImageButton) rootView.findViewById(R.id.foodmenubutton);
        ImageButton myorderbutton=(ImageButton) rootView.findViewById(R.id.myorderbutton);

        TextView welcomeuser= (TextView) rootView.findViewById(R.id.welcomeuser);
        showusername= (TextView) rootView.findViewById(R.id.showusername);

        welcomeuser.setTypeface(dsfatty);
        showusername.setTypeface(dsfatty);



        foodmenubutton.setOnClickListener(this);
        myorderbutton.setOnClickListener(this);


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
            case R.id.foodmenubutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.customermain_frame, customerfoodmenu.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
            case R.id.myorderbutton:
                if(customerpayment.callbefore==true)
                {
                    FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.customermain_frame, customerpayment.newInstance());
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }else if (customercartconfirmed.callbefore==true)
                {
                    FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.customermain_frame, customercartconfirmed.newInstance());
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }else
                {
                    FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.customermain_frame, customercartnotconfirm.newInstance());
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }
                break;
        }

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
