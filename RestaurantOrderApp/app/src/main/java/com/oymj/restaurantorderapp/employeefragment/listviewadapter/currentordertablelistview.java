package com.oymj.restaurantorderapp.employeefragment.listviewadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderinqueue;
import com.oymj.restaurantorderapp.employeefragment.employeeorderinqueue;
import com.oymj.restaurantorderapp.employeefragment.employeetablefoodordered;
import com.oymj.restaurantorderapp.employeefragment.employeetablepayment;


import java.util.ArrayList;

import communicateserverthread.employeecommunicate.setordercooking;


/**
 * Created by OwYeong on 10/2/2017.
 */

public class currentordertablelistview extends BaseAdapter implements View.OnClickListener
{
    Context context;
    ArrayList<currentorderinqueue> orderlist;
    com.oymj.restaurantorderapp.employeefragment.employeeorderinqueue employeeorderinqueue;

    private static LayoutInflater inflater=null;

    public currentordertablelistview(employeeorderinqueue employeeorderinqueue, ArrayList<currentorderinqueue> orderlist) {
        // TODO Auto-generated constructor stub
        context=employeeorderinqueue.getActivity();
        this.employeeorderinqueue=employeeorderinqueue;
        this.orderlist=orderlist;

        inflater = (LayoutInflater)context.
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

    TextView tablenotv;
    ImageView cookconditioniv;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.employee_currentordertablelistview, null);
        tablenotv=(TextView) rowView.findViewById(R.id.currentordertableno);

        Typeface immonolt =Typeface.createFromAsset(employeeorderinqueue.getActivity().getAssets(),"font/immonolt.otf");
        tablenotv.setTypeface(immonolt);

        cookconditioniv=(ImageView) rowView.findViewById(R.id.currentordercondition);


        tablenotv.setText("Table No: "+Integer.toString(orderlist.get(position).getTableno()));

        if(orderlist.get(position).getCooking()==true)
        {
            cookconditioniv.setImageResource(R.drawable.ic_cooking);
        }else
        {
            cookconditioniv.setImageResource(R.drawable.ic_inqueue);
        }

        if (orderlist.get(position).getcookdone()==true)
        {
            cookconditioniv.setImageResource(R.drawable.ic_done);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(orderlist.get(position).getcookdone()==true)
                {
                    FragmentTransaction FragTrans;
                    FragTrans = employeeorderinqueue.getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.employeemain_frame, employeetablepayment.newInstance(orderlist.get(position).getTableno()));
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }else
                {
                    FragmentTransaction FragTrans;
                    FragTrans = employeeorderinqueue.getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.employeemain_frame, employeetablefoodordered.newInstance(orderlist.get(position).getTableno()));
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();

                    setordercooking setordercooking=new setordercooking(employeeorderinqueue,orderlist.get(position).getTableno());
                    Thread thread=new Thread(setordercooking);
                    thread.start();
                }

            }
        });

        return rowView;
    }

    @Override
    public void onClick(View v) {

    }
}
