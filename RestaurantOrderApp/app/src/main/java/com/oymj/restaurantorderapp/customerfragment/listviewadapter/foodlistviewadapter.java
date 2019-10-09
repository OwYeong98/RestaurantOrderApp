package com.oymj.restaurantorderapp.customerfragment.listviewadapter;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customerfoodmenu;
import com.oymj.restaurantorderapp.customerfragment.customerfooddetail;
import com.oymj.restaurantorderapp.customerfragment.orderfood;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodinformation;

import java.util.ArrayList;

public class foodlistviewadapter extends BaseAdapter implements View.OnClickListener{
    Context context;
    ArrayList<foodinformation> food;
    customerfoodmenu custfoodmenu;


    private static LayoutInflater inflater=null;

    public foodlistviewadapter(customerfoodmenu customerfoodmenu, ArrayList<foodinformation> arrayfood) {
        // TODO Auto-generated constructor stub
        context=customerfoodmenu.getActivity();
        custfoodmenu=customerfoodmenu;
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

    TextView foodnametv;
    TextView fooddesctv;
    ImageButton order;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View rowView;
        rowView = inflater.inflate(R.layout.customer_foodlistview, null);


        //set food background
        String foodname=food.get(position).getFoodname().toLowerCase();
        foodname=foodname.replace(" ","");
        int imageid = custfoodmenu.getActivity().getResources().getIdentifier("small"+foodname, "drawable", custfoodmenu.getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            rowView.setBackgroundResource(R.drawable.smallnopic);
        }else
        {
            rowView.setBackgroundResource(imageid);
        }






        foodnametv=(TextView) rowView.findViewById(R.id.foodname);
        fooddesctv=(TextView) rowView.findViewById(R.id.fooddesc);

        fooddesctv.setText(food.get(position).getFooddesc());


        foodnametv.setText(food.get(position).getFoodname());



        Typeface insanibu =Typeface.createFromAsset(custfoodmenu.getActivity().getAssets(),"font/insanibu.ttf");
        Typeface immonoltregular =Typeface.createFromAsset(custfoodmenu.getActivity().getAssets(),"font/immonoltregular.otf");
        foodnametv.setTypeface(insanibu);
        fooddesctv.setTypeface(immonoltregular);


        order=(ImageButton) rowView.findViewById(R.id.orderbutton);

        ImageView stamp=(ImageView) rowView.findViewById(R.id.stampnotavailable);

        //if food available only make it clickable if not available stamp a not available on top
        if(food.get(position).getAvailableornot()=='Y')
        {
            stamp.setImageResource(android.R.color.transparent);

            order.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FragmentTransaction FragTrans;
                    FragTrans = custfoodmenu.getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.customermain_frame, orderfood.newInstance(food.get(position).getFoodname()));
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();

                }
            });

            rowView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    FragmentTransaction FragTrans;
                    FragTrans = custfoodmenu.getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.customermain_frame, customerfooddetail.newInstance(food.get(position).getFoodname()));
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }
            });
        }else
        {
            stamp.setImageResource(R.drawable.stamp_notavailable);
        }





        return rowView;
    }

    @Override
    public void onClick(View v) {

    }

}


