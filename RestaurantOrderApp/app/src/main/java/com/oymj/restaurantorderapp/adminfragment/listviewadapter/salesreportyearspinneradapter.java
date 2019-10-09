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

/**
 * Created by OwYeong on 10/16/2017.
 */

public class salesreportyearspinneradapter extends BaseAdapter
{
    Context context;
    LayoutInflater inflater;
    com.oymj.restaurantorderapp.adminfragment.adminsalesreport adminsalesreport;
    com.oymj.restaurantorderapp.adminfragment.adminfoodsalesreport adminfoodsalesreport;
    int firstyear;
    ArrayList<String> year=new ArrayList<String>();

    public salesreportyearspinneradapter(adminsalesreport adminsalesreport, ArrayList<String> year) {
        this.context = adminsalesreport.getActivity();
        this.adminsalesreport=adminsalesreport;
        this.year=year;

        inflater = (LayoutInflater.from(context));
    }

    public salesreportyearspinneradapter(adminfoodsalesreport adminfoodsalesreport, ArrayList<String> year) {
        this.context = adminfoodsalesreport.getActivity();
        this.adminfoodsalesreport=adminfoodsalesreport;
        this.year=year;

        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return year.size();
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

        spinnertitle.setText(year.get(position));




        return view;
    }





}
