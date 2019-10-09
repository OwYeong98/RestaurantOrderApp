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
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderinqueue;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.currentordertablelistview;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import communicateserverthread.Currentfragment;


/**
 * Created by OwYeong on 9/28/2017.
 */

public class employeeorderinqueue extends Fragment
{
    public static employeeorderinqueue newInstance() {
        employeeorderinqueue fragment = new employeeorderinqueue();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.employee_currentorderinqueue,container,false);

        ListView foodlist=(ListView) rootView.findViewById(R.id.orderinqueuelistview);

        foodlist.setAdapter(new currentordertablelistview(this, sortorderinqueue(database_currentrequest.currentorder)));

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        Typeface immonolt =Typeface.createFromAsset(getActivity().getAssets(),"font/immonolt.otf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.employee_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        TextView currentorderinqueue=(TextView) rootView.findViewById(R.id.currentorderinqueueword);
        currentorderinqueue.setTypeface(immonolt);

        TextView tablenoword=(TextView) rootView.findViewById(R.id.tablenoword);
        TextView statusword=(TextView) rootView.findViewById(R.id.statusword);

        tablenoword.setTypeface(insanibu);
        statusword.setTypeface(insanibu);




        return rootView;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ArrayList<currentorderinqueue> sortorderinqueue(ArrayList<currentorderinqueue> order)
    {
        int arraysize = order.size();
        for (int i = 0; i < arraysize-1; i++)
        {
            for (int j = 0; j < arraysize-i-1; j++)
            {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date dateordered1=null;
                Date dateordered2=null;
                try {
                    dateordered1=dateFormat.parse(order.get(j).getDateordered());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                try {
                    dateordered2=dateFormat.parse(order.get(j+1).getDateordered());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (dateordered2.before(dateordered1) )
                {
                    // swap temp and arr[i]
                    currentorderinqueue temp=order.get(i);
                    order.set(i,order.get(i+1));
                    order.set(i+1,temp);

                }
            }
        }

        return order;

    }

}
