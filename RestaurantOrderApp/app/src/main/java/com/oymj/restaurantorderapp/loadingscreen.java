package com.oymj.restaurantorderapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import communicateserverthread.login;

public class loadingscreen extends AppCompatActivity {
    Intent intent;
    Bundle extras;
    Socket s;
    Thread thread;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_loadingscreen);

        //start the animation of loadscreen imageview background which is loadingscreenanim
        ImageView loadscreen = (ImageView) findViewById(R.id.loadscreen);
        ((AnimationDrawable) loadscreen.getBackground()).start();

        intent=getIntent();

        extras = intent.getExtras();

        login lgthread=new login(thread,loadingscreen.this,intent.getStringExtra("username"),intent.getStringExtra("password"));

        thread=new Thread(lgthread);

        thread.start();

    }




}
