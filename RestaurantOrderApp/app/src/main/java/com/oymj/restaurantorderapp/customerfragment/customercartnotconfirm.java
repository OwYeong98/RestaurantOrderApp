package com.oymj.restaurantorderapp.customerfragment;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.listviewadapter.ordernotconfirmlistviewadapter;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import java.text.ParseException;

import communicateserverthread.Currentfragment;
import communicateserverthread.customercommunicate.sentorder;

public class customercartnotconfirm extends Fragment implements View.OnClickListener{
    public ListView foodcart;
    EditText inputtableno;
    Thread sendorderthread;
    double totalprice=0;


    public static customercartnotconfirm newInstance() {
        customercartnotconfirm fragment = new customercartnotconfirm();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.customer_cartnotconfirm,container,false);

        TextView myordertitle=(TextView) rootView.findViewById(R.id.cartnotconfirmtitle);
        TextView myordernotsent=(TextView) rootView.findViewById(R.id.cartnotconfirmnotsent);
        TextView myordertableno=(TextView) rootView.findViewById(R.id.cartnotconfirmtableno);
        TextView myordertotalprice=(TextView) rootView.findViewById(R.id.cartnotconfirmtotalprice);


        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        myordertitle.setTypeface(dsfatty);

        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        myordernotsent.setTypeface(insanibu);

        Typeface immonolt =Typeface.createFromAsset(getActivity().getAssets(),"font/immonolt.otf");
        myordertableno.setTypeface(immonolt);
        myordertotalprice.setTypeface(immonolt);



        foodcart=(ListView) rootView.findViewById(R.id.ordernotconfirmlistview);
        ImageButton sendorder=(ImageButton) rootView.findViewById(R.id.confirmorder);

        foodcart.setAdapter(new ordernotconfirmlistviewadapter(this, foodmenu.foodcart));
        inputtableno=(EditText) rootView.findViewById(R.id.tablenumber);



        //total up the price
        for(int loop=0;loop<foodmenu.foodcart.size();loop++)
        {
            for (int foodloop=0;foodloop<foodmenu.foodlist.size();foodloop++)
            {
                if(foodmenu.foodcart.get(loop).getFoodname().equals(foodmenu.foodlist.get(foodloop).getFoodname()))
                {
                    double pricemulquantity=foodmenu.foodcart.get(loop).getQuantity()*foodmenu.foodlist.get(foodloop).getFoodprice();
                    totalprice+=pricemulquantity;
                }
            }

        }
        myordertotalprice.setText("Total: RM"+String.format("%.2f", totalprice));

        sendorder.setOnClickListener(this);





        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.confirmorder:

                try
                {
                    sentorder sorder=new sentorder(this,Integer.parseInt(inputtableno.getText().toString()));
                    sendorderthread= new Thread(sorder);

                    //if table already exist
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user back to connectionerror page so that user know
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setMessage("Are you sure you want to confirm ur order? Total price is: RM"+String.format("%.2f", totalprice)+"(not included GST and tax)");
                            dialog.setCancelable(true);

                            dialog.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                            sendorderthread.start();
                                        }
                                    });

                            dialog.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert = dialog.create();
                            alert.show();
                        }
                    });




                }catch (NumberFormatException e)
                {
                    //if table already exist
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user back to connectionerror page so that user know
                            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                            dialog.setMessage("Please Enter a valid number");
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
                    });
                }




        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
