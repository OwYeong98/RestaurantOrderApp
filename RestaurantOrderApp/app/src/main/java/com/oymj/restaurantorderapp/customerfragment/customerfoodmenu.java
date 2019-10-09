package com.oymj.restaurantorderapp.customerfragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.listviewadapter.foodlistviewadapter;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import java.util.ArrayList;

import communicateserverthread.Currentfragment;

public class customerfoodmenu extends Fragment{
    customerfoodmenu customerfoodmenu=this;
    EditText searchbar;
    ListView foodlist;
    ArrayList<foodinformation> foodsearch = new ArrayList<foodinformation>();



    public static customerfoodmenu newInstance() {
        customerfoodmenu fragment = new customerfoodmenu();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.customer_foodmenu,container,false);

        foodlist=(ListView) rootView.findViewById(R.id.foodlistview);

        foodlist.setAdapter(new foodlistviewadapter(this, foodmenu.foodlist));



        searchbar = (EditText) rootView.findViewById(R.id.searchbar);

        searchbar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s)
            {


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after)
            {

            }


            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
                String input=searchbar.getText().toString();

                foodsearch.clear();

                for (int loop=0;loop<foodmenu.foodlist.size();loop++)
                {
                    if (foodmenu.foodlist.get(loop).getFoodname().toLowerCase().contains(input.toLowerCase())==true)
                    {
                        String foodname=foodmenu.foodlist.get(loop).getFoodname();
                        String fooddesc=foodmenu.foodlist.get(loop).getFooddesc();
                        double foodprice=foodmenu.foodlist.get(loop).getFoodprice();
                        int foodspicy=foodmenu.foodlist.get(loop).getFoodspicy();
                        int containmeat=foodmenu.foodlist.get(loop).getContainmeat();
                        int foodsalty=foodmenu.foodlist.get(loop).getFoodsalty();
                        int foodsweetness=foodmenu.foodlist.get(loop).getFoodsweetness();
                        int foodsour=foodmenu.foodlist.get(loop).getFoodsour();
                        int chefsuggest=foodmenu.foodlist.get(loop).getChefsuggest();
                        int preparetime=foodmenu.foodlist.get(loop).getPreparetime();
                        char availableornot=foodmenu.foodlist.get(loop).getAvailableornot();

                        foodsearch.add(new foodinformation(foodname,fooddesc,foodprice,foodspicy,containmeat,foodsalty,foodsweetness,foodsour,chefsuggest,preparetime,availableornot));
                    }
                }

                foodlist.setAdapter(new foodlistviewadapter(customerfoodmenu, foodsearch));

            }

        });

        return rootView;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }







}
