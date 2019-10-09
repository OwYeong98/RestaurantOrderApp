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
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.ordernotpaid;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.ordernotpaidfoodinformation;
import com.oymj.restaurantorderapp.employeefragment.employeeordernotpaidfulldetail;

import java.util.ArrayList;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class employeeordernotpaidfulldetaillistviewadapter extends BaseAdapter
{
    Context context;
    com.oymj.restaurantorderapp.employeefragment.employeeordernotpaidfulldetail employeeordernotpaidfulldetail;
    ArrayList<ordernotpaidfoodinformation> foodordered;

    private TextView foodnametv;
    private TextView foodquantitytv;
    private TextView foodtotalprice;
    private ImageView foodicon;



    private static LayoutInflater inflater=null;

    public employeeordernotpaidfulldetaillistviewadapter(employeeordernotpaidfulldetail employeeordernotpaidfulldetail, String date,String username,ArrayList<ordernotpaid> orderlist) {
        // TODO Auto-generated constructor stub
        context=employeeordernotpaidfulldetail.getActivity();
        this.employeeordernotpaidfulldetail=employeeordernotpaidfulldetail;

        for(int loop=0;loop<orderlist.size();loop++)
        {
            if(orderlist.get(loop).getUsername().equals(username) && orderlist.get(loop).getDate().equals(date))
            {
                foodordered=orderlist.get(loop).getFoodordered();
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
        int imageid = employeeordernotpaidfulldetail.getActivity().getResources().getIdentifier("mini"+foodname, "drawable", employeeordernotpaidfulldetail.getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            foodicon.setImageResource(imageid);
        }else
        {
            foodicon.setImageResource(imageid);
        }

        foodnametv.setText(foodordered.get(position).getFoodname());
        foodquantitytv.setText(Integer.toString(foodordered.get(position).getQuantity()));
        foodtotalprice.setText(Double.toString(foodordered.get(position).getTotalprice()));



        return rowView;
    }
}
