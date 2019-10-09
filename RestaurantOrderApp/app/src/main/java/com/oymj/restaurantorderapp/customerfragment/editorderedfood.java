package com.oymj.restaurantorderapp.customerfragment;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import communicateserverthread.Currentfragment;
import communicateserverthread.customercommunicate.editfoodcart;

/**
 * Created by OwYeong on 9/25/2017.
 */

public class editorderedfood extends Fragment implements View.OnClickListener
{
    private boolean validremark;
    private boolean validquantity=false;
    private EditText quantity;
    private EditText remark;
    private TextView totalprice;
    private double foodprice;

    public static editorderedfood newInstance(String foodname) {

        Bundle bundle = new Bundle();
        bundle.putString("foodname",foodname);

        editorderedfood fragment = new editorderedfood();

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
        View rootView=inflater.inflate(R.layout.customer_editorderedfood,container,false);

        validquantity=false;
        validremark=false;

        TextView pricetitle=(TextView) rootView.findViewById(R.id.editorderfoodpricetitle);
        TextView orderprice = (TextView) rootView.findViewById(R.id.editOrderprice);
        TextView foodtitle = (TextView) rootView.findViewById(R.id.editfoodtitleorderfood);
        ImageView foodimg = (ImageView) rootView.findViewById(R.id.editfoodimgorderfood);
        TextView quantitytitle=(TextView) rootView.findViewById(R.id.edittextViewquantity);
        TextView remarktitle=(TextView) rootView.findViewById(R.id.editremark);
        TextView pagetitle=(TextView) rootView.findViewById(R.id.pagetitle);
        totalprice=(TextView) rootView.findViewById(R.id.editfoodorderedtotalprice);

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

        for(int loop = 0; loop< foodmenu.foodlist.size(); loop++)
        {
            if(getArguments().getString("foodname").equals(foodmenu.foodlist.get(loop).getFoodname()))
            {
                //set desc get from foodmenu class that store all food detail
                orderprice.setText("RM  "+foodmenu.foodlist.get(loop).getFoodprice()+" Each");
                foodprice=foodmenu.foodlist.get(loop).getFoodprice();
            }
        }





        ImageButton ordernow = (ImageButton) rootView.findViewById(R.id.editorderfood);

        ordernow.setOnClickListener(this);

        quantity = (EditText) rootView.findViewById(R.id.editquantity);
        remark=(EditText) rootView.findViewById(R.id.editremark);

        for(int loop=0;loop<foodmenu.foodcart.size();loop++)
        {
            if(getArguments().getString("foodname").equals(foodmenu.foodcart.get(loop).getFoodname()))
            {
                quantity.setText(Integer.toString(foodmenu.foodcart.get(loop).getQuantity()));
                remark.setText(foodmenu.foodcart.get(loop).getRemark());
            }
        }




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
            case R.id.editorderfood:
                if(validremark && validquantity == true)
                {
                    editfoodcart edfoodcart=new editfoodcart(this,getArguments().getString("foodname"),Integer.parseInt(quantity.getText().toString()),remark.getText().toString());
                    Thread editfoodthread = new Thread(edfoodcart);

                    editfoodthread.start();


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

                break;
        }

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
