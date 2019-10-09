package com.oymj.restaurantorderapp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class serverconnectionerror extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serverconnectionerror);

        TextView connecttexterror = (TextView) findViewById(R.id.connecterrordisplay);


        Typeface immonolt = Typeface.createFromAsset(getAssets(), "font/immonolt.otf");

        connecttexterror.setTypeface(immonolt);

        ImageButton backlgpage = (ImageButton) findViewById(R.id.backloginpage);
        backlgpage.setOnClickListener(this);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.backloginpage:

                //navigate user back to loginpage
                Intent in = new Intent(serverconnectionerror.this, LoginScreen.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(in);

                break;
        }

    }
}
