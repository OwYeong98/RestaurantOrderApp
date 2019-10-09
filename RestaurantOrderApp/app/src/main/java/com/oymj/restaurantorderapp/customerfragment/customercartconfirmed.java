package com.oymj.restaurantorderapp.customerfragment;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.listviewadapter.orderconfirmlistviewadapter;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import communicateserverthread.Currentfragment;
import communicateserverthread.customercommunicate.cancelorder;
import communicateserverthread.customercommunicate.paymentrequest;

public class customercartconfirmed extends Fragment implements View.OnClickListener{
    public static boolean callbefore=false;

    public ListView foodcartconfirm;
    public CountDownTimer countdowntimer;
    private TextView countdown;

    public static customercartconfirmed newInstance() {
        callbefore=true;
        customercartconfirmed fragment = new customercartconfirmed();

        //update current displaying fragment
        Currentfragment.currentfragment=fragment;
        Currentfragment.currentfragmentclassname=fragment.getClass().getSimpleName();


        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.customer_cartconfirmed,container,false);

        TextView tableno= (TextView) rootView.findViewById(R.id.cartconfirmtableno);
        TextView cookcondition= (TextView) rootView.findViewById(R.id.cartconfirmcookcondition);
        countdown=(TextView) rootView.findViewById(R.id.countdowntime);
        TextView estimatetimetitle=(TextView) rootView.findViewById(R.id.orderconfirmedestimatedtimetitle);

        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");
        tableno.setTypeface(insanibu);
        cookcondition.setTypeface(insanibu);

        Typeface immonolt =Typeface.createFromAsset(getActivity().getAssets(),"font/immonolt.otf");
        countdown.setTypeface(immonolt);
        estimatetimetitle.setTypeface(immonolt);

        tableno.setText("Table: "+Integer.toString(foodmenu.foodcartconfirmed.get(0).getTableno()));
        int estimatedtime=foodmenu.foodcartconfirmed.get(0).getEstimatedtime();
        int estimatedhour=estimatedtime/60;
        int estimatedminute=estimatedtime%60;

        countdown.setText(estimatedhour+" hour "+estimatedminute+" minutes 0 second");

        if(foodmenu.foodcartconfirmed.get(0).isCookcondition()==false)
        {
            cookcondition.setText("Status: In Queue");
        }else
        {
            cookcondition.setText("Status: Cooking");

            startcountdowntimer();
        }

        if (foodmenu.foodcartconfirmed.get(0).isCookdone()==true)
        {
            cookcondition.setText("Status: Done");
        }





        foodcartconfirm=(ListView) rootView.findViewById(R.id.orderconfirmedlistview);
        ImageButton payment=(ImageButton) rootView.findViewById(R.id.paymentbutton);
        ImageButton cancel = (ImageButton) rootView.findViewById(R.id.cancelbutton);

        foodcartconfirm.setAdapter(new orderconfirmlistviewadapter(this, foodmenu.foodcartconfirmed.get(0).getFoodordered()));


        payment.setOnClickListener(this);
        cancel.setOnClickListener(this);





        return rootView;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.paymentbutton:
                if(foodmenu.foodcartconfirmed.get(0).isCookdone()==true)
                {
                    paymentrequest payrequest=new paymentrequest(this);
                    Thread paythread=new Thread(payrequest);

                    paythread.start();
                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("You can only request payment when your order is done!!");
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
            case R.id.cancelbutton:

                cancelorder cancelor=new cancelorder(this);
                Thread cancelthread=new Thread(cancelor);

                cancelthread.start();


                break;

        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    public void startcountdowntimer()
    {
        String stringcookstartdate=foodmenu.foodcartconfirmed.get(0).getDatestartcook();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date cookstartdate=null;

        try {
            cookstartdate = dateFormat.parse(stringcookstartdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long MinutesinMillis=60000;//millisecs

        System.out.println("Minutes in milli "+foodmenu.foodcartconfirmed.get(0).getEstimatedtime() * MinutesinMillis);

        long cookstartmillis= cookstartdate.getTime();
        Date estimateddatefinish=new Date(cookstartmillis + (foodmenu.foodcartconfirmed.get(0).getEstimatedtime() * MinutesinMillis));

        Date datenow=new Date();

        long estimatedcooktimeleft=estimateddatefinish.getTime()-datenow.getTime();

        //make the time countdown if it cooking
        countdowntimer=new CountDownTimer(estimatedcooktimeleft, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);

                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                countdown.setText(String.format("%02d", hours)+" hour "+String.format("%02d", minutes)+" minutes "+String.format("%02d", seconds)+"second");

            }

            public void onFinish() {
                countdown.setText("0 hours 0 minutes 0 seconds");
            }
        }.start();
    }

    public void stopcountdowntimer()
    {
        if(countdowntimer!=null)
        {
            countdowntimer.cancel();
        }
    }



}
