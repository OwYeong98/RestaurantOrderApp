package com.oymj.restaurantorderapp.employeefragment;

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
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.tablefoodorderedlistview;

import communicateserverthread.Currentfragment;
import communicateserverthread.employeecommunicate.setorderdone;

/**
 * Created by OwYeong on 10/2/2017.
 */

public class employeetablefoodordered extends Fragment implements View.OnClickListener
{
    public static employeetablefoodordered newInstance(int tableno) {
        Bundle bundle = new Bundle();
        bundle.putString("tableno",Integer.toString(tableno));

        employeetablefoodordered fragment = new employeetablefoodordered();

        fragment.setArguments(bundle);

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.employee_tablefoodordered,container,false);

        ListView foodlist=(ListView) rootView.findViewById(R.id.tablefoodorderedlistview);

        foodlist.setAdapter(new tablefoodorderedlistview(this,Integer.parseInt(getArguments().getString("tableno")), database_currentrequest.currentorder));

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        Typeface immonolt =Typeface.createFromAsset(getActivity().getAssets(),"font/immonolt.otf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.employee_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        TextView currentorderlist=(TextView) rootView.findViewById(R.id.currentorderlistword);
        currentorderlist.setTypeface(immonolt);

        TextView tableno= (TextView) rootView.findViewById(R.id.tablefoodorderedtableno);
        tableno.setTypeface(insanibu);
        tableno.setText("Table "+getArguments().getString("tableno"));

        ImageButton orderdone= (ImageButton) rootView.findViewById(R.id.tabledonebutton);
        orderdone.setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.tabledonebutton:
                setorderdone setorderdone=new setorderdone(this,Integer.parseInt(getArguments().getString("tableno")));
                Thread thread=new Thread(setorderdone);
                thread.start();

                break;

        }

    }
}
