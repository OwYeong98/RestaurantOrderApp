package com.oymj.restaurantorderapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;

import java.net.*;
import java.io.*;

import communicateserverthread.checksession;


public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    public Socket s;



    EditText usernameinput;
    EditText passwordinput;

    TextView userview;
    TextView pwview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);

        userview=(TextView)findViewById(R.id.usertext);
        pwview =(TextView)findViewById(R.id.pwtext);

        Typeface immonolt =Typeface.createFromAsset(getAssets(),"font/immonolt.otf");

        userview.setTypeface(immonolt);
        pwview.setTypeface(immonolt);



        ImageButton lgbutton = (ImageButton) findViewById(R.id.loginbutton);
        ImageButton rgbutton = (ImageButton) findViewById(R.id.registerbutton);
        lgbutton.setOnClickListener(this);
        rgbutton.setOnClickListener(this);

        usernameinput = (EditText) findViewById(R.id.username);
        passwordinput = (EditText) findViewById(R.id.password);



        //check whether the user have logged in recently
        checksession cksession=new checksession(LoginScreen.this);
        Thread checksessionthread= new Thread(cksession);
        checksessionthread.start();





    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginbutton:
                Intent inlogin = new Intent(LoginScreen.this, loadingscreen.class);
                inlogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //pass the username and password to the loading screen
                //loading screen will send packet to server to validate the id
                inlogin.putExtra("username", usernameinput.getText().toString());
                inlogin.putExtra("password", passwordinput.getText().toString());

                startActivity(inlogin);


                break;
            case R.id.registerbutton:
                Intent inregister = new Intent(LoginScreen.this, Registerscreen.class);
                inregister.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(inregister);

                break;
        }


    }







}


