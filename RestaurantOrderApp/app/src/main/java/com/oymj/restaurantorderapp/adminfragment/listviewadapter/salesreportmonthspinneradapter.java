package com.oymj.restaurantorderapp.adminfragment.listviewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.adminfoodsalesreport;
import com.oymj.restaurantorderapp.adminfragment.adminsalesreport;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by OwYeong on 10/22/2017.
 */

public class salesreportmonthspinneradapter extends BaseAdapter
{
    Context context;
    LayoutInflater inflater;
    com.oymj.restaurantorderapp.adminfragment.adminsalesreport adminsalesreport;
    com.oymj.restaurantorderapp.adminfragment.adminfoodsalesreport adminfoodsalesreport;
    int year;
    ArrayList<String > monthlist=new ArrayList<String>();

    //all month for this constructer
    public salesreportmonthspinneradapter(adminsalesreport adminsalesreport,ArrayList<String> monthlist)
    {
        this.context = adminsalesreport.getActivity();
        this.adminsalesreport=adminsalesreport;
        this.monthlist=monthlist;

        inflater = (LayoutInflater.from(context));
    }

    public salesreportmonthspinneradapter(adminfoodsalesreport adminfoodsalesreport,ArrayList<String> monthlist)
    {
        this.context = adminfoodsalesreport.getActivity();
        this.adminfoodsalesreport=adminfoodsalesreport;
        this.monthlist=monthlist;

        inflater = (LayoutInflater.from(context));
    }


    @Override
    public int getCount() {
        return monthlist.size();
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

        view = inflater.inflate(R.layout.admin_salesreportspinner, null);

        TextView spinnertitle=(TextView) view.findViewById(R.id.spinnertitle);

        spinnertitle.setText(monthlist.get(position));




        return view;
    }



}
