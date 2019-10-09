package com.oymj.restaurantorderapp;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.oymj.restaurantorderapp.adminfragment.adminmainscreen;
import com.oymj.restaurantorderapp.customerfragment.customermainscreen;
import com.oymj.restaurantorderapp.employeefragment.employeemainscreen;


public class sessiondetected extends AppCompatActivity {
    private Thread sendtomainpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessiondetected);
        final Bundle extras=getIntent().getExtras();

        //start the animation of loadscreen imageview background which is loadingscreenanim
        ImageView loadscreen = (ImageView) findViewById(R.id.loadscreen);
        ((AnimationDrawable) loadscreen.getBackground()).start();

        sendtomainpage = new Thread(new Runnable() {

            @Override
            public void run ()
            {
                try
                {
                    sendtomainpage.sleep(7000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //runOnUiThread function is to let the function to be run on main thread
                //bacause UI function cannot be run on worker thread
                sessiondetected.this.runOnUiThread(new Runnable() {
                    public void run() {
                        //Do your UI operations like dialog opening or Toast here
                        //navigate user back to connectionerror page so that user know
                        if(extras.getString("privillage").equals("admin"))
                        {
                            Intent in = new Intent(sessiondetected.this, adminmainscreen.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                        }else if (extras.getString("privillage").equals("employee"))
                        {
                            Intent in = new Intent(sessiondetected.this, employeemainscreen.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                        }else
                        {
                            Intent in = new Intent(sessiondetected.this, customermainscreen.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);
                        }

                    }
                });


            }
        });

        sendtomainpage.start();
    }


}
