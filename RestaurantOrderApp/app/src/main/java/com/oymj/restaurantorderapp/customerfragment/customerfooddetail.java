package com.oymj.restaurantorderapp.customerfragment;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;

public class customerfooddetail extends Fragment implements View.OnClickListener{

    private String foodname;
    private int spicycount;
    private int saltycount;
    private int sweetcount;
    private int sourcount;
    private int chefcount;
    private int vegetableornot;

    public static customerfooddetail newInstance(String foodname) {

        Bundle bundle = new Bundle();
        bundle.putString("foodname",foodname);

        customerfooddetail fragment = new customerfooddetail();

        fragment.setArguments(bundle);

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.customer_fooddetail,container,false);

        foodname=getArguments().getString("foodname");




        ImageView foodimage= (ImageView) rootView.findViewById(R.id.foodimage);
        TextView foodtitle = (TextView) rootView.findViewById(R.id.foodtitle);
        TextView fooddesc = (TextView) rootView.findViewById(R.id.fooddescription);
        TextView chefrate=(TextView) rootView.findViewById(R.id.chefrate);

        //set order button onclick listener
        ImageButton order=(ImageButton) rootView.findViewById(R.id.ordernow);
        order.setOnClickListener(this);

        //set font family
        Typeface immonolt =Typeface.createFromAsset(getActivity().getAssets(),"font/immonoltregular.otf");
        chefrate.setTypeface(immonolt);

        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        foodtitle.setTypeface(insanibu);

        Typeface arialnarrow =Typeface.createFromAsset(getActivity().getAssets(),"font/arialnarrow.ttf");
        fooddesc.setTypeface(arialnarrow);


        String nospacefoodname=foodname.replace(" ","");
        String imagefoodname=nospacefoodname.toLowerCase();

        //check if image exist
        int imageid = getActivity().getResources().getIdentifier("big"+imagefoodname, "drawable", getActivity().getPackageName());

        //if image not exist will return 0
        if(imageid==0)
        {
            foodimage.setImageResource(getActivity().getResources().getIdentifier("bignopic", "drawable", getActivity().getPackageName()));
        }else
        {
            foodimage.setImageResource(getActivity().getResources().getIdentifier("big"+imagefoodname, "drawable", getActivity().getPackageName()));
        }


        //set food name
        foodtitle.setText(foodname);

        //search customerfooddetail from foodmenu that store all food detail
        for(int loop=0;loop<foodmenu.foodlist.size();loop++)
        {
            if(foodname.equals(foodmenu.foodlist.get(loop).getFoodname()))
            {
                //set desc get from foodmenu class that store all food detail
                fooddesc.setText(foodmenu.foodlist.get(loop).getFooddesc());
                spicycount=foodmenu.foodlist.get(loop).getFoodspicy();
                saltycount=foodmenu.foodlist.get(loop).getFoodsalty();
                sweetcount=foodmenu.foodlist.get(loop).getFoodsweetness();
                sourcount =foodmenu.foodlist.get(loop).getFoodsour();
                chefcount =foodmenu.foodlist.get(loop).getChefsuggest();
                vegetableornot=foodmenu.foodlist.get(loop).getContainmeat();
            }
        }

        //set the chef star according to food detail
        TableRow chef = (TableRow) rootView.findViewById(R.id.chefrow);

        for (int chefimage=1; chefimage<=chefcount; chefimage++){
            ImageView im = new ImageView (getActivity());
            im.setImageDrawable(getResources().getDrawable(R.drawable.ic_chef));
            im.setPadding(50, 0, 0, 0); //padding in each image if needed
            //add here on click event etc for each image...
            //...
            chef.addView(im, 150,160);
        }


        TableRow spicy = (TableRow) rootView.findViewById(R.id.spicyrow);
        TextView spicytext=(TextView) rootView.findViewById(R.id.spicy);
        spicytext.setTypeface(immonolt);

        for (int chiliimage=1; chiliimage<=spicycount; chiliimage++){
            ImageView im = new ImageView (getActivity());
            im.setImageDrawable(getResources().getDrawable(R.drawable.ic_spicy));
            im.setPadding(0, 0, 0, 0); //padding in each image if needed
            //add here on click event etc for each image...
            //...
            spicy.addView(im, 58,58);
        }

        TableRow salty = (TableRow) rootView.findViewById(R.id.saltyrow);
        TextView saltytext=(TextView) rootView.findViewById(R.id.salty);
        saltytext.setTypeface(immonolt);

        for (int saltimage=1; saltimage<=saltycount; saltimage++){
            ImageView im = new ImageView (getActivity());
            im.setImageDrawable(getResources().getDrawable(R.drawable.ic_salt));
            im.setPadding(10, 0, 0, 0); //padding in each image if needed
            //add here on click event etc for each image...
            //...
            salty.addView(im, 36,66);
        }

        TableRow sweet = (TableRow) rootView.findViewById(R.id.sweetrow);
        TextView sweettext=(TextView) rootView.findViewById(R.id.sweet);
        sweettext.setTypeface(immonolt);

        for (int sweetimage=1; sweetimage<=sweetcount; sweetimage++){
            ImageView im = new ImageView (getActivity());
            im.setImageDrawable(getResources().getDrawable(R.drawable.ic_sweet));
            im.setPadding(0, 0, 0, 0); //padding in each image if needed
            //add here on click event etc for each image...
            //...
            sweet.addView(im, 53,53);
        }

        TableRow sour = (TableRow) rootView.findViewById(R.id.sourrow);
        TextView sourtext=(TextView) rootView.findViewById(R.id.sour);
        sourtext.setTypeface(immonolt);

        for (int sourimage=1; sourimage<=sourcount; sourimage++){
            ImageView im = new ImageView (getActivity());
            im.setImageDrawable(getResources().getDrawable(R.drawable.ic_sour));
            im.setPadding(10, 0, 0, 0); //padding in each image if needed
            //add here on click event etc for each image...
            //...
            sour.addView(im, 44,39);
        }



        TableRow vegetarian = (TableRow) rootView.findViewById(R.id.vegetarianrow);
        TextView vegeteriantext=(TextView) rootView.findViewById(R.id.meatorvegetarian);
        vegeteriantext.setTypeface(immonolt);

        for (int vegetarianimage=1; vegetarianimage<=1; vegetarianimage++){
            Drawable image;

            if (vegetableornot==0)
            {
                image = getResources().getDrawable(R.drawable.ic_vegetable);
                ImageView im = new ImageView (getActivity());
                im.setImageDrawable(image);
                im.setPadding(50, 0, 0, 0); //padding in each image if needed
                //add here on click event etc for each image...
                //...
                vegetarian.addView(im, 90,90);
            }else
            {
                image =getResources().getDrawable(R.drawable.ic_drumstick);
                ImageView im = new ImageView (getActivity());
                im.setImageDrawable(image);
                im.setPadding(50, 0, 0, 0); //padding in each image if needed
                //add here on click event etc for each image...
                //...
                vegetarian.addView(im, 86,87);
            }



        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.ordernow:
                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                FragTrans.replace(R.id.customermain_frame, orderfood.newInstance(foodname));
                FragTrans.addToBackStack(null);
                FragTrans.commit();

                break;
        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
