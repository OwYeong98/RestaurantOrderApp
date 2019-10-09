package com.oymj.restaurantorderapp.employeefragment.listviewadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodinformation;
import com.oymj.restaurantorderapp.employeefragment.employeefoodavailabilitycontrol;

import java.util.ArrayList;

import communicateserverthread.employeecommunicate.setfoodavailability;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class foodavailabilitycontrollistviewadapter extends BaseAdapter
{
    Context context;
    com.oymj.restaurantorderapp.employeefragment.employeefoodavailabilitycontrol employeefoodavailabilitycontrol;
    ArrayList<foodinformation> foodlist;


    private static LayoutInflater inflater=null;

    public foodavailabilitycontrollistviewadapter(employeefoodavailabilitycontrol employeefoodavailabilitycontrol, ArrayList<foodinformation> foodlist) {
        // TODO Auto-generated constructor stub
        context=employeefoodavailabilitycontrol.getActivity();
        this.employeefoodavailabilitycontrol=employeefoodavailabilitycontrol;
        this.foodlist=foodlist;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return foodlist.size();
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

    private TextView foodnametv;
    private ImageView availableornotiv;


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.employee_foodavailabilitycontrollistview, null);
        foodnametv=(TextView) rowView.findViewById(R.id.foodavailabilitycontrolfoodname);
        availableornotiv=(ImageView) rowView.findViewById(R.id.foodavailabilitycontrolavailableornot);

        Typeface insanibu =Typeface.createFromAsset(employeefoodavailabilitycontrol.getActivity().getAssets(),"font/insanibu.ttf");
        foodnametv.setTypeface(insanibu);

        foodnametv.setText(foodlist.get(position).getFoodname());

        if(Character.toString(foodlist.get(position).getAvailableornot()).equals("Y"))
        {
            availableornotiv.setImageResource(R.drawable.ic_availablenow);
        }else
        {
            availableornotiv.setImageResource(R.drawable.ic_notavailable);
        }

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                boolean foodavailabilityboolean=false;
                if(Character.toString(foodlist.get(position).getAvailableornot()).equals("Y"))
                {
                    foodavailabilityboolean=false;
                }else
                {
                    foodavailabilityboolean=true;
                }

                setfoodavailability setfoodavailability=new setfoodavailability(employeefoodavailabilitycontrol,foodlist.get(position).getFoodname(),foodavailabilityboolean);

                Thread thread=new Thread(setfoodavailability);
                thread.start();

            }
        });


        return rowView;
    }
}
