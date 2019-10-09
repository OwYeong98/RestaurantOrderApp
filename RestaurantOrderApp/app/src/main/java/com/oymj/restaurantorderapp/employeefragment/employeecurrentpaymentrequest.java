package com.oymj.restaurantorderapp.employeefragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.currentpaymentrequestlistviewadapter;

import communicateserverthread.Currentfragment;

/**
 * Created by OwYeong on 10/7/2017.
 */

public class employeecurrentpaymentrequest extends Fragment
{
    public static employeecurrentpaymentrequest newInstance() {
        employeecurrentpaymentrequest fragment = new employeecurrentpaymentrequest();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.employee_currentpaymentrequest,container,false);

        ListView paymentlist=(ListView) rootView.findViewById(R.id.currentpaymentlistview);

        paymentlist.setAdapter(new currentpaymentrequestlistviewadapter(this, database_currentrequest.currentpaymentrequest));

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.employee_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        TextView currentpaymentrequestword=(TextView) rootView.findViewById(R.id.currentpaymentrequestword);
        currentpaymentrequestword.setTypeface(insanibu);


        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
