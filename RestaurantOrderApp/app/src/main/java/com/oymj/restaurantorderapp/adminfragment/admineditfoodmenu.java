package com.oymj.restaurantorderapp.adminfragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.listviewadapter.editfoodlistviewadapter;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;

/**
 * Created by OwYeong on 10/13/2017.
 */

public class admineditfoodmenu extends Fragment implements View.OnClickListener
{
    public static admineditfoodmenu newInstance() {
        admineditfoodmenu fragment = new admineditfoodmenu();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.admin_editfoodmenu,container,false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView topemployeetitle=(TextView) rootView.findViewById(R.id.admin_toptitle);
        topemployeetitle.setTypeface(dsfatty);

        TextView topemployeeeditpagetitle=(TextView) rootView.findViewById(R.id.admin_toppagetitle);
        topemployeeeditpagetitle.setTypeface(insanibu);

        ListView editfoodlist=(ListView) rootView.findViewById(R.id.editfoodlistview);

        ImageButton addmenu=(ImageButton) rootView.findViewById(R.id.addmenubutton);
        addmenu.setOnClickListener(this);

        editfoodlistviewadapter editfoodlistviewadapter=new editfoodlistviewadapter(this, foodmenu.foodlist);
        editfoodlist.setAdapter(editfoodlistviewadapter);


        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.addmenubutton:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.adminmain_frame, adminaddfood.newInstance());
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
        }

    }

}
