package com.oymj.restaurantorderapp.adminfragment;

import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
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
 * Created by OwYeong on 11/5/2017.
 */

public class adminreportselect extends Fragment implements View.OnClickListener {

    public static adminreportselect newInstance() {
        adminreportselect fragment = new adminreportselect();

        //update current displaying fragment
        Currentfragment.currentfragment = fragment;
        Currentfragment.currentfragmentclassname = fragment.getClass().getSimpleName();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_reportselect, container, false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        ImageButton salesreportbutton=(ImageButton) rootView.findViewById(R.id.salesreportbutton);
        ImageButton foodsalesreportbutton=(ImageButton) rootView.findViewById(R.id.foodsalereportbutton);

        salesreportbutton.setOnClickListener(this);
        foodsalesreportbutton.setOnClickListener(this);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.salesreportbutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.adminmain_frame, adminsalesreport.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
            case R.id.foodsalereportbutton:

                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.adminmain_frame, adminfoodsalesreport.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
        }
    }
}
