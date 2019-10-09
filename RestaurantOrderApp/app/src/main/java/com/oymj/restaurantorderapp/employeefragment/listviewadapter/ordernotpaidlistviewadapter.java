package com.oymj.restaurantorderapp.employeefragment.listviewadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.ordernotpaid;
import com.oymj.restaurantorderapp.employeefragment.employeeordernotpaid;
import com.oymj.restaurantorderapp.employeefragment.employeeordernotpaidfulldetail;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/7/2017.
 */

public class ordernotpaidlistviewadapter extends BaseAdapter
{
    Context context;
    com.oymj.restaurantorderapp.employeefragment.employeeordernotpaid employeeordernotpaid;
    ArrayList<ordernotpaid> orderlist;


    private static LayoutInflater inflater=null;

    public ordernotpaidlistviewadapter(employeeordernotpaid employeeordernotpaid, ArrayList<ordernotpaid> orderlist) {
        // TODO Auto-generated constructor stub
        context=employeeordernotpaid.getActivity();
        this.employeeordernotpaid=employeeordernotpaid;
        this.orderlist=orderlist;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return orderlist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    private TextView datetv;
    private TextView usernametv;
    private TextView totalpaymenttv;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.employee_ordernotpaidlistview, null);
        datetv=(TextView) rowView.findViewById(R.id.ordernotpaiddate);
        totalpaymenttv = (TextView) rowView.findViewById(R.id.ordernotpaidtotalprice);
        usernametv = (TextView) rowView.findViewById(R.id.ordernotpaidusername);

        Typeface immonolt =Typeface.createFromAsset(employeeordernotpaid.getActivity().getAssets(),"font/immonolt.otf");

        datetv.setTypeface(immonolt);
        totalpaymenttv.setTypeface(immonolt);
        usernametv.setTypeface(immonolt);

        datetv.setText(orderlist.get(position).getDate());
        usernametv.setText(orderlist.get(position).getUsername());
        totalpaymenttv.setText(Double.toString(orderlist.get(position).getTotalpayment()));

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                FragmentTransaction FragTrans;
                FragTrans = employeeordernotpaid.getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.employeemain_frame, employeeordernotpaidfulldetail.newInstance(orderlist.get(position).getUsername(),orderlist.get(position).getDate()));
                FragTrans.addToBackStack(null);
                FragTrans.commit();
            }
        });


        return rowView;
    }

}
