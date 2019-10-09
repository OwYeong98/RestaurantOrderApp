package com.oymj.restaurantorderapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.net.Socket;

public class registersuccessful extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registersuccessful);

        ImageButton backlogin=(ImageButton) findViewById(R.id.backloginpage);
        backlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backloginpage:
                Intent in = new Intent(registersuccessful.this, LoginScreen.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);


                break;
        }
    }
}
