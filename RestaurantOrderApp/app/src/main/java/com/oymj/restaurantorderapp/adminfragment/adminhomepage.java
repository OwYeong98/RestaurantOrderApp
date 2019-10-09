package com.oymj.restaurantorderapp.adminfragment;

import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;

import communicateserverthread.Currentfragment;
import communicateserverthread.getnamefromserver;

/**
 * Created by OwYeong on 10/13/2017.
 */

public class adminhomepage extends Fragment implements View.OnClickListener
{
    Spinner userspinner;
    TextView showusername;



    public static adminhomepage newInstance() {
        adminhomepage fragment = new adminhomepage();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.admin_homepage,container,false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        userspinner = (Spinner) rootView.findViewById(R.id.userspinner);

        TextView welcomeuser= (TextView) rootView.findViewById(R.id.welcomeuser);
        showusername= (TextView) rootView.findViewById(R.id.showusername);

        welcomeuser.setTypeface(dsfatty);
        showusername.setTypeface(dsfatty);

        ImageButton editfoodmenubutton=(ImageButton) rootView.findViewById(R.id.editfoodmenubutton);
        ImageButton checkreportbutton=(ImageButton) rootView.findViewById(R.id.checkreportbutton);
        ImageButton viewfeedbackbutton=(ImageButton) rootView.findViewById(R.id.viewfeedbackbutton);

        editfoodmenubutton.setOnClickListener(this);
        checkreportbutton.setOnClickListener(this);
        viewfeedbackbutton.setOnClickListener(this);



        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);




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
            case R.id.editfoodmenubutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.adminmain_frame, admineditfoodmenu.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
            case R.id.checkreportbutton:

                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.adminmain_frame, adminreportselect.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();


                break;
            case R.id.viewfeedbackbutton:

                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.adminmain_frame, adminviewfeedback.newInstance());
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
