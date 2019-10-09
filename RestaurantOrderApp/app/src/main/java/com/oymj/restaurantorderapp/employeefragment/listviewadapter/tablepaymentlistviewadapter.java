package com.oymj.restaurantorderapp.employeefragment.listviewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderfoodinformation;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderinqueue;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.employeetablepayment;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/7/2017.
 */

public class tablepaymentlistviewadapter extends BaseAdapter
{
    Context context;
    com.oymj.restaurantorderapp.employeefragment.employeetablepayment employeetablepayment;
    ArrayList<currentorderinqueue> orderlist;
    ArrayList<currentorderfoodinformation> foodordered;
    int tableno;

    private TextView foodnametv;
    private TextView foodquantitytv;
    private TextView foodtotalprice;
    private ImageView foodicon;



    private static LayoutInflater inflater=null;

    public tablepaymentlistviewadapter(employeetablepayment employeetablepayment, ArrayList<currentorderinqueue> orderlist,int tableno) {
        // TODO Auto-generated constructor stub
        context=employeetablepayment.getActivity();
        this.employeetablepayment=employeetablepayment;
        this.orderlist=orderlist;
        this.tableno=tableno;

        for(int loop=0;loop<orderlist.size();loop++)
        {
            if(orderlist.get(loop).getTableno()==this.tableno)
            {
                foodordered=orderlist.get(loop).getFoodorderlist();
            }
        }



        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return foodordered.size();
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




    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.employee_paymentfoodlistview, null);
        foodnametv=(TextView) rowView.findViewById(R.id.paymentfoodname);
        foodquantitytv = (TextView) rowView.findViewById(R.id.paymentquantity);
        foodtotalprice = (TextView) rowView.findViewById(R.id.paymentfoodprice);
        foodicon= (ImageView) rowView.findViewById(R.id.paymentimage);

        String foodname=foodordered.get(position).getFoodname().toLowerCase();
        foodname=foodname.replace(" ","");
        int imageid = employeetablepayment.getActivity().getResources().getIdentifier("mini"+foodname, "drawable", employeetablepayment.getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            foodicon.setImageResource(employeetablepayment.getActivity().getResources().getIdentifier("mininopic", "drawable", employeetablepayment.getActivity().getPackageName()));
        }else
        {
            foodicon.setImageResource(imageid);
        }

        foodnametv.setText(foodordered.get(position).getFoodname());
        foodquantitytv.setText(Integer.toString(foodordered.get(position).getQuantity()));

        //find food price from food list which store all information of food
        for(int loop = 0; loop< foodmenu.foodlist.size(); loop++)
        {
            if(foodordered.get(position).getFoodname().equals(foodmenu.foodlist.get(loop).getFoodname()))
            {
                foodtotalprice.setText(String.format("%,.2f", foodordered.get(position).getQuantity()*foodmenu.foodlist.get(loop).getFoodprice()));
            }
        }


        return rowView;
    }

}
