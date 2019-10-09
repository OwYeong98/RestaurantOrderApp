package com.oymj.restaurantorderapp.customerfragment.listviewadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm;
import com.oymj.restaurantorderapp.customerfragment.orderfood;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodcartinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;


import java.util.ArrayList;

public class ordernotconfirmlistviewadapter extends BaseAdapter implements View.OnClickListener{

    Context context;
    ArrayList<foodcartinformation> food;
    com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm customercartnotconfirm;

    private TextView foodnametv;
    private TextView foodquantitytv;
    private ImageView foodiconiv;
    private ImageButton cancelorder;


    private static LayoutInflater inflater=null;

    public ordernotconfirmlistviewadapter(customercartnotconfirm customercartnotconfirm, ArrayList<foodcartinformation> arrayfood) {
        // TODO Auto-generated constructor stub
        context=customercartnotconfirm.getActivity();
        this.customercartnotconfirm=customercartnotconfirm;
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
        rowView = inflater.inflate(R.layout.customer_cartlistview, null);
        foodnametv=(TextView) rowView.findViewById(R.id.foodnamecartnotconfirm);
        foodiconiv=(ImageView) rowView.findViewById(R.id.foodiconcartnotconfirm);
        foodquantitytv = (TextView) rowView.findViewById(R.id.orderquantitycartnotconfirm);
        cancelorder = (ImageButton) rowView.findViewById(R.id.cancelcartnotconfirm);

        Typeface insanibu =Typeface.createFromAsset(customercartnotconfirm.getActivity().getAssets(),"font/insanibu.ttf");
        foodnametv.setTypeface(insanibu);
        foodquantitytv.setTypeface(insanibu);



        foodnametv.setText(food.get(position).getFoodname());
        foodquantitytv.setText(Integer.toString(food.get(position).getQuantity()));


        //set food image
        String foodname=food.get(position).getFoodname().toLowerCase();
        foodname=foodname.replace(" ","");
        int imageid = customercartnotconfirm.getActivity().getResources().getIdentifier("mini"+foodname, "drawable", customercartnotconfirm.getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            foodiconiv.setImageResource(customercartnotconfirm.getActivity().getResources().getIdentifier("mininopic", "drawable", customercartnotconfirm.getActivity().getPackageName()));
        }else
        {
            foodiconiv.setImageResource(imageid);
        }

        cancelorder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for (int loop=0;loop< foodmenu.foodcart.size();loop++)
                {
                    //find food in list with same name and remove it
                    if(food.get(position).getFoodname().equals(foodmenu.foodcart.get(loop).getFoodname()))
                    {
                        foodmenu.foodcart.remove(loop);
                        FragmentTransaction FragTrans;
                        FragTrans = customercartnotconfirm.getActivity().getSupportFragmentManager().beginTransaction();
                        FragTrans.replace(R.id.customermain_frame, com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm.newInstance());
                        FragTrans.commit();
                    }
                }

            }
        });


        return rowView;
    }

    @Override
    public void onClick(View v) {

    }
}
