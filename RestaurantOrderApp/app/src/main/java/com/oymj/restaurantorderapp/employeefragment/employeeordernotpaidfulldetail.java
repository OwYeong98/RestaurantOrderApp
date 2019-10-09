package com.oymj.restaurantorderapp.employeefragment;

import android.graphics.Typeface;
import android.os.Bundle;
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
import com.oymj.restaurantorderapp.database_orderhistory.orderhistoryrecord;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.employeeordernotpaidfulldetaillistviewadapter;

import communicateserverthread.Currentfragment;
import communicateserverthread.employeecommunicate.ordernotpaiddone;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class employeeordernotpaidfulldetail extends Fragment implements View.OnClickListener
{
    public static employeeordernotpaidfulldetail newInstance(String username,String date) {
        Bundle bundle=new Bundle();
        bundle.putString("username",username);
        bundle.putString("date",date);

        employeeordernotpaidfulldetail fragment = new employeeordernotpaidfulldetail();
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
        View rootView=inflater.inflate(R.layout.employee_ordernotpaidfulldetail,container,false);

        ListView orderlist=(ListView) rootView.findViewById(R.id.ordernotpaidfulldetaillistview);

        orderlist.setAdapter(new employeeordernotpaidfulldetaillistviewadapter(this,getArguments().getString("date"),getArguments().getString("username"), orderhistoryrecord.ordernotpaid));
        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.employee_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);


        TextView date=(TextView) rootView.findViewById(R.id.ordernotpaidfulldetaildate);
        TextView taxprice=(TextView) rootView.findViewById(R.id.ordernotpaidfulldetailtaxprice);
        TextView totalpayment=(TextView) rootView.findViewById(R.id.ordernotpaidfulldetailtotalprice);
        TextView gstprice=(TextView) rootView.findViewById(R.id.ordernotpaidfulldetailgstprice);


        date.setText(getArguments().getString("username")+": "+getArguments().getString("date").substring(0,10));
        for (int loop = 0; loop< orderhistoryrecord.ordernotpaid.size(); loop++)
        {
            if(orderhistoryrecord.ordernotpaid.get(loop).getUsername().equals(getArguments().getString("username")) && orderhistoryrecord.ordernotpaid.get(loop).getDate().equals(getArguments().getString("date")))
            {
                double originalprice= orderhistoryrecord.ordernotpaid.get(loop).getTotalpayment()/1.16;
                taxprice.setText("Tax(10%): RM"+String.format("%,.2f", originalprice*10/100));
                gstprice.setText("GST(6%): RM"+String.format("%,.2f", originalprice*6/100));
                totalpayment.setText("Total: RM"+String.format("%,.2f", orderhistoryrecord.ordernotpaid.get(loop).getTotalpayment()));
            }
        }

        ImageButton paymentdone= (ImageButton) rootView.findViewById(R.id.ordernotpaidfulldetailpaymentdone);
        paymentdone.setOnClickListener(this);



        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.ordernotpaidfulldetailpaymentdone:
                ordernotpaiddone ordernotpaiddone=new ordernotpaiddone(this,getArguments().getString("date"),getArguments().getString("username"));
                Thread thread=new Thread(ordernotpaiddone);
                thread.start();

                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
