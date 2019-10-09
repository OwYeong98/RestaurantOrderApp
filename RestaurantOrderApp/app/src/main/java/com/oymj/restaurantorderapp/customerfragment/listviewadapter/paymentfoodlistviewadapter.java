package com.oymj.restaurantorderapp.customerfragment.listviewadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodcartinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import java.util.ArrayList;

/**
 * Created by OwYeong on 9/25/2017.
 */

public class paymentfoodlistviewadapter extends BaseAdapter implements View.OnClickListener
{
    Context context;
    ArrayList<foodcartinformation> food;
    customerpayment customerpayment;

    private TextView foodnametv;
    private TextView foodquantitytv;
    private TextView foodtotalprice;
    private ImageView foodicon;



    private static LayoutInflater inflater=null;

    public paymentfoodlistviewadapter(customerpayment customerpayment, ArrayList<foodcartinformation> arrayfood) {
        // TODO Auto-generated constructor stub
        context=customerpayment.getActivity();
        this.customerpayment=customerpayment;
        food=arrayfood;

        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return food.size();
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
        foodicon = (ImageView) rowView.findViewById(R.id.paymentimage);

        String foodname=food.get(position).getFoodname().toLowerCase();
        foodname=foodname.replace(" ","");
        int imageid = customerpayment.getActivity().getResources().getIdentifier("mini"+foodname, "drawable", customerpayment.getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            foodicon.setImageResource(customerpayment.getActivity().getResources().getIdentifier("mininopic", "drawable", customerpayment.getActivity().getPackageName()));
        }else
        {
            foodicon.setImageResource(imageid);
        }

        foodnametv.setText(food.get(position).getFoodname());
        foodquantitytv.setText(Integer.toString(food.get(position).getQuantity()));

        //find food price from food list which store all information of food
        for(int loop=0;loop<foodmenu.foodlist.size();loop++)
        {
            if(food.get(position).getFoodname().equals(foodmenu.foodlist.get(loop).getFoodname()))
            {
                foodtotalprice.setText(String.format("%,.2f", food.get(position).getQuantity()*foodmenu.foodlist.get(loop).getFoodprice()));
            }
        }


        return rowView;
    }

    @Override
    public void onClick(View v) {

    }
}
