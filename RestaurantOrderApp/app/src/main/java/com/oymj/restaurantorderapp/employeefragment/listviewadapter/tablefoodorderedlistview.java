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
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderfoodinformation;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderinqueue;
import com.oymj.restaurantorderapp.employeefragment.employeetablefoodordered;


import java.util.ArrayList;

/**
 * Created by OwYeong on 10/4/2017.
 */

public class tablefoodorderedlistview extends BaseAdapter
{
    Context context;
    com.oymj.restaurantorderapp.employeefragment.employeetablefoodordered employeetablefoodordered;
    ArrayList<currentorderfoodinformation> tablefoodlist;


    private static LayoutInflater inflater=null;

    public tablefoodorderedlistview(employeetablefoodordered employeetablefoodordered, int tableno,ArrayList<currentorderinqueue> orderlist) {
        // TODO Auto-generated constructor stub
        context=employeetablefoodordered.getActivity();
        this.employeetablefoodordered=employeetablefoodordered;

        for(int loop=0;loop<orderlist.size();loop++)
        {
            if(tableno==orderlist.get(loop).getTableno())
            {
                tablefoodlist=orderlist.get(loop).getFoodorderlist();
            }
        }

        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return tablefoodlist.size();
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

    TextView foodnametv;
    TextView quantitytv;
    TextView remarktv;
    ImageView foodiconiv;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.employee_tablefoodorderedlistview, null);
        foodnametv=(TextView) rowView.findViewById(R.id.tablefoodorderedfoodname);
        quantitytv=(TextView) rowView.findViewById(R.id.tablefoodorderedquantity);
        remarktv=(TextView) rowView.findViewById(R.id.tablefoodorderedremark);
        foodiconiv=(ImageView) rowView.findViewById(R.id.tablefoodorderedfoodicon);

        Typeface insanibu =Typeface.createFromAsset(employeetablefoodordered.getActivity().getAssets(),"font/insanibu.ttf");
        foodnametv.setTypeface(insanibu);

        Typeface arial =Typeface.createFromAsset(employeetablefoodordered.getActivity().getAssets(),"font/arialnarrow.ttf");
        remarktv.setTypeface(arial);

        //set food image
        String foodname=tablefoodlist.get(position).getFoodname().toLowerCase();
        foodname=foodname.replace(" ","");
        int imageid = employeetablefoodordered.getActivity().getResources().getIdentifier("mini"+foodname, "drawable", employeetablefoodordered.getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            foodiconiv.setImageResource(employeetablefoodordered.getActivity().getResources().getIdentifier("mininopic", "drawable", employeetablefoodordered.getActivity().getPackageName()));
        }else
        {
            foodiconiv.setImageResource(imageid);
        }


        foodnametv.setText(tablefoodlist.get(position).getFoodname());
        quantitytv.setText(Integer.toString(tablefoodlist.get(position).getQuantity()));
        remarktv.setText(tablefoodlist.get(position).getRemark());


        return rowView;
    }
}
