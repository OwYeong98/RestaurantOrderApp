package com.oymj.restaurantorderapp.adminfragment.listviewadapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.admineditfooddetail;
import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodinformation;

import java.util.ArrayList;

import communicateserverthread.admincommunicate.deletefoodmenu;

/**
 * Created by OwYeong on 10/13/2017.
 */

public class editfoodlistviewadapter extends BaseAdapter implements View.OnClickListener
{
    private static LayoutInflater inflater=null;
    Context context;
    ArrayList<foodinformation> food;
    com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu admineditfoodmenu;

    public editfoodlistviewadapter(admineditfoodmenu admineditfoodmenu, ArrayList<foodinformation> arrayfood) {
        // TODO Auto-generated constructor stub
        context=admineditfoodmenu.getActivity();
        this.admineditfoodmenu=admineditfoodmenu;
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
        rowView = inflater.inflate(R.layout.admin_editfoodlistview, null);

        TextView foodname=(TextView) rowView.findViewById(R.id.editfoodmenufoodname);
        ImageButton remove=(ImageButton) rowView.findViewById(R.id.editfoodmenuremove);
        ImageButton edit=(ImageButton) rowView.findViewById(R.id.editfoodmenuedit);

        Typeface immonolt =Typeface.createFromAsset(admineditfoodmenu.getActivity().getAssets(),"font/immonolt.otf");


        foodname.setTypeface(immonolt);
        foodname.setText(food.get(position).getFoodname());


        remove.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                deletefoodmenu deletefoodmenu= new deletefoodmenu(admineditfoodmenu,food.get(position).getFoodname());

                Thread deletethread=new Thread(deletefoodmenu);

                deletethread.start();

            }
        });

        edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction FragTrans;
                FragTrans = admineditfoodmenu.getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.adminmain_frame, admineditfooddetail.newInstance(food.get(position).getFoodname()));
                FragTrans.addToBackStack(null);
                FragTrans.commit();

            }
        });


        return rowView;
    }

    @Override
    public void onClick(View v) {

    }

}
