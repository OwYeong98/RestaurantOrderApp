package com.oymj.restaurantorderapp.customerfragment;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodcartinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;

public class orderfood extends Fragment implements View.OnClickListener{

    private boolean validquantity;
    private boolean validremark;
    EditText quantity;
    EditText remark;
    TextView totalprice;
    private  double foodprice;

    public static orderfood newInstance(String foodname) {

        Bundle bundle = new Bundle();
        bundle.putString("foodname",foodname);

        orderfood fragment = new orderfood();

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
        View rootView=inflater.inflate(R.layout.customer_orderfood,container,false);

        validquantity=false;
        validremark=false;

        TextView orderprice = (TextView) rootView.findViewById(R.id.Orderprice);
        TextView foodtitle = (TextView) rootView.findViewById(R.id.foodtitleorderfood);
        ImageView foodimg = (ImageView) rootView.findViewById(R.id.foodimgorderfood);

        TextView pricetitle=(TextView) rootView.findViewById(R.id.orderfoodpricetitle);
        TextView quantitytitle=(TextView) rootView.findViewById(R.id.orderfoodquantity);
        TextView remarktitle=(TextView) rootView.findViewById(R.id.orderfoodremark);
        TextView pagetitle=(TextView) rootView.findViewById(R.id.pagetitle);
        totalprice=(TextView) rootView.findViewById(R.id.orderfoodtotalprice);

        Typeface immonolt = Typeface.createFromAsset(getActivity().getAssets(),"font/immonolt.otf");
        quantitytitle.setTypeface(immonolt);
        remarktitle.setTypeface(immonolt);
        totalprice.setTypeface(immonolt);


        Typeface arialnarrow = Typeface.createFromAsset(getActivity().getAssets(),"font/arialnarrow.ttf");
        orderprice.setTypeface(arialnarrow);
        pricetitle.setTypeface(arialnarrow);

        Typeface insanibu = Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        pagetitle.setTypeface(insanibu);

        foodtitle.setText(getArguments().getString("foodname"));

        String nospacefoodname=getArguments().getString("foodname").replace(" ","");
        String imagefoodname=nospacefoodname.toLowerCase();

        int imageid = getActivity().getResources().getIdentifier("big"+imagefoodname, "drawable", getActivity().getPackageName());

        //return 0 mean cant find image, so we set image to image that show no image available
        if(imageid==0)
        {
            foodimg.setImageResource(getActivity().getResources().getIdentifier("login", "drawable", getActivity().getPackageName()));

        }else
        {
            foodimg.setImageResource(imageid);

        }

        for(int loop=0;loop<foodmenu.foodlist.size();loop++)
        {
            if(getArguments().getString("foodname").equals(foodmenu.foodlist.get(loop).getFoodname()))
            {
                //set desc get from foodmenu class that store all food detail
                orderprice.setText("Order this food: RM "+foodmenu.foodlist.get(loop).getFoodprice()+" each");
                foodprice=foodmenu.foodlist.get(loop).getFoodprice();
            }
        }





        ImageButton ordernow = (ImageButton) rootView.findViewById(R.id.orderfood);

        ordernow.setOnClickListener(this);

        quantity = (EditText) rootView.findViewById(R.id.quantity);



        quantity.addTextChangedListener(new TextWatcher() {

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
                if(quantity.getText().toString().equals(""))
                {
                    validquantity=false;
                    quantity.setError("Please enter something !!");
                }else
                {
                    int input=Integer.parseInt(quantity.getText().toString());

                    if(input<=0)
                    {
                        validquantity=false;
                        quantity.setError("You must order at least 1 !!");
                    }else
                    {
                        validquantity=true;
                        double pricemulquantity=Integer.parseInt(quantity.getText().toString()) * foodprice;
                        totalprice.setText("Total Price: RM"+String.format("%,.2f", pricemulquantity));
                    }
                }
            }

        });

        remark=(EditText) rootView.findViewById(R.id.remark);

        remark.addTextChangedListener(new TextWatcher() {

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
                String input=remark.getText().toString();

                if(input.length()>50)
                {
                    validremark=false;
                    remark.setError("Your remark should not be too long");
                }else
                {
                    validremark=true;
                }

            }

        });


        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.orderfood:
                if(customercartconfirmed.callbefore==false)
                {
                    if(validremark && validquantity == true)
                    {
                        boolean foodalreadyincart=false;

                        for(int loop=0;loop<foodmenu.foodcart.size();loop++)
                        {
                            if(getArguments().getString("foodname").equals(foodmenu.foodcart.get(loop).getFoodname()))
                            {
                                foodalreadyincart=true;
                                Toast.makeText(getActivity(), "Food already ordered in your order!!Added more quantity", Toast.LENGTH_LONG).show();
                                int newvalue=foodmenu.foodcart.get(loop).getQuantity()+Integer.parseInt(quantity.getText().toString());
                                foodmenu.foodcart.get(loop).setQuantity(newvalue);

                                FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                                FragTrans.replace(R.id.customermain_frame, customerfoodmenu.newInstance());
                                FragTrans.addToBackStack(null);
                                FragTrans.commit();
                            }
                        }

                        if (foodalreadyincart==false)
                        {
                            foodmenu.foodcart.add(new foodcartinformation(getArguments().getString("foodname"),Integer.parseInt(quantity.getText().toString()),remark.getText().toString()));
                            Toast.makeText(getActivity(), "Ordered Successfully!!", Toast.LENGTH_LONG).show();

                            FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.customermain_frame, customerfoodmenu.newInstance());
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();
                        }


                    }else
                    {
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        dialog.setMessage("Please make sure all information with error is corrected!!");
                        dialog.setCancelable(true);

                        dialog.setPositiveButton(
                                "Ok!",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });


                        AlertDialog alert = dialog.create();
                        alert.show();
                    }
                }else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("You have already sent your order!!");
                    dialog.setCancelable(true);

                    dialog.setPositiveButton(
                            "Ok!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });


                    AlertDialog alert = dialog.create();
                    alert.show();
                }

                break;
        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
