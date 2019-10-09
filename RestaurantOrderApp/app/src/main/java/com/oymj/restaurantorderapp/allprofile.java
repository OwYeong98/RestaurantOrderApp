package com.oymj.restaurantorderapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.adminfragment.adminhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.employeefragment.employeehomepage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import communicateserverthread.Serverip;
import communicateserverthread.editprofile;
import communicateserverthread.getnamefromserver;
import communicateserverthread.profilegetinformation;

/**
 * Created by OwYeong on 11/5/2017.
 */

public class allprofile extends Fragment implements View.OnClickListener
{
    EditText confirmpasswordinput;
    EditText passwordinput;
    EditText ageinput;
    EditText nameinput;
    EditText genderinput;


    boolean validpassword=true;
    boolean validconfirmpassword=true;
    boolean validage=true;
    boolean validname=true;
    boolean validgender=true;



    public static allprofile newInstance(adminhomepage adminhomepage) {
        allprofile fragment = new allprofile();

        Bundle bundle=new Bundle();
        bundle.putString("privillage","admin");
        fragment.setArguments(bundle);


        return fragment;
    }

    public static allprofile newInstance(employeehomepage employeehomepage) {
        allprofile fragment = new allprofile();

        Bundle bundle=new Bundle();
        bundle.putString("privillage","employee");
        fragment.setArguments(bundle);

        return fragment;
    }

    public static allprofile newInstance(customerhomepage customerhomepage) {
        allprofile fragment = new allprofile();

        Bundle bundle=new Bundle();
        bundle.putString("privillage","customer");
        fragment.setArguments(bundle);


        return fragment;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView=inflater.inflate(R.layout.all_profile,container,false);

        Typeface dsfatty =Typeface.createFromAsset(getActivity().getAssets(),"font/DSFattyContour.ttf");
        Typeface insanibu =Typeface.createFromAsset(getActivity().getAssets(),"font/insanibu.ttf");

        TextView toptitle=(TextView) rootView.findViewById(R.id.employee_toptitle);
        toptitle.setTypeface(dsfatty);

        toptitle.setText(getArguments().getString("privillage").toUpperCase());

        TextView topeditpagetitle=(TextView) rootView.findViewById(R.id.employee_topeditpagetitle);

        topeditpagetitle.setTypeface(insanibu);

        //all all previous detail
        profilegetinformation profilegetinformation=new profilegetinformation(this);
        Thread gtthread=new Thread(profilegetinformation);
        gtthread.start();

        ImageButton confirmchange=(ImageButton) rootView.findViewById(R.id.confirmchangebutton);
        ImageButton abortchange=(ImageButton) rootView.findViewById(R.id.abortchangebutton);

        confirmchange.setOnClickListener(this);
        abortchange.setOnClickListener(this);


        passwordinput=(EditText) rootView.findViewById(R.id.profilepasswordedit);
        ageinput=(EditText) rootView.findViewById(R.id.profileageedit);
        nameinput=(EditText) rootView.findViewById(R.id.profilenameedit);
        genderinput=(EditText) rootView.findViewById(R.id.profilegenderedit);
        confirmpasswordinput=(EditText) rootView.findViewById(R.id.profileconfirmpasswordedit);

        passwordinput.addTextChangedListener(new TextWatcher() {

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
                String input;
                input=passwordinput.getText().toString();
                boolean gotuppercase=!input.equals(input.toLowerCase());
                boolean gotnumber=input.contains("1")||input.contains("2")||input.contains("3")||input.contains("4")||input.contains("5")
                        ||input.contains("6")||input.contains("7")||input.contains("8")||input.contains("9")||input.contains("0");


                if(input.length()<8||input.length()>16)
                {
                    validpassword=false;
                    passwordinput.setError("password length must be at least 8 character and at most 16 character!!");
                }else if(gotuppercase==true && input.contains("@")&&gotnumber==true)
                {
                    validpassword=true;
                }else
                {
                    validpassword=false;
                    passwordinput.setError("Please set a harder password! Password must contains Uppercase,number, and a @ symbol");
                }
            }
        });

        //password ocnfirm validation
        confirmpasswordinput.addTextChangedListener(new TextWatcher() {

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
                String input=confirmpasswordinput.getText().toString();

                if(input.equals(passwordinput.getText().toString())==false)
                {
                    validconfirmpassword=false;
                    confirmpasswordinput.setError("The Password is not same");
                }else
                {
                    validconfirmpassword=true;
                }

            }

        });

        //validation for name
        nameinput.addTextChangedListener(new TextWatcher() {

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
                String input=nameinput.getText().toString();
                boolean gotsymbol=input.contains("+")||input.contains("-")||input.contains("*")||input.contains("/")||input.contains(")")
                        ||input.contains("(")||input.contains("&")||input.contains("^")||input.contains("%")||input.contains("$")
                        ||input.contains("#")||input.contains("@")||input.contains("!")||input.contains("`")||input.contains("~")
                        ||input.contains("<")||input.contains(">")||input.contains(",")||input.contains(".")||input.contains("?")
                        ||input.contains("{")||input.contains("}")||input.contains("[")||input.contains("]")||input.contains("|");

                boolean gotnumber=input.contains("1")||input.contains("2")||input.contains("3")||input.contains("4")||input.contains("5")
                        ||input.contains("6")||input.contains("7")||input.contains("8")||input.contains("9")||input.contains("0");

                if(input.length()<6||input.length() >15)
                {
                    validname=false;
                    nameinput.setError("The name must have at least 6 character and at most 15 character");
                }else if(gotnumber==true)
                {
                    validname=false;
                    nameinput.setError("Name should not contain number");
                }else if (gotsymbol==true)
                {
                    validname=false;
                    nameinput.setError("Name should not contain symbol");
                }else
                {
                    validname=true;
                }
            }
        });

        ageinput.addTextChangedListener(new TextWatcher() {

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
                String input=ageinput.getText().toString();

                if(input.length()>3)
                {
                    validage=false;
                    ageinput.setError("Please put appropiate age!");
                }else
                {
                    validage=true;
                }

            }

        });

        genderinput.addTextChangedListener(new TextWatcher() {

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
                String input=genderinput.getText().toString().toLowerCase();


                if(input.equals("male")==false && input.equals("female")==false)
                {
                    validgender=false;
                    genderinput.setError("Please enter only 'male' or 'female'!!");
                }else
                {
                    validgender=true;
                }

            }

        });


        return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction FragTrans;
        switch (v.getId()) {
            case R.id.confirmchangebutton:
                if(validage&&validconfirmpassword&&validgender&&validname&&validpassword==true)
                {
                    editprofile editprofile=new editprofile(this,nameinput.getText().toString(),ageinput.getText().toString(),passwordinput.getText().toString(),genderinput.getText().toString());
                    Thread thread=new Thread(editprofile);
                    thread.start();
                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                    dialog.setMessage("Please provide the correct information ");
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
            case R.id.abortchangebutton:
                if(getArguments().getString("privillage").equals("admin"))
                {
                    FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.adminmain_frame, adminhomepage.newInstance());
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }else if (getArguments().getString("privillage").equals("employee"))
                {
                    FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.employeemain_frame, employeehomepage.newInstance());
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }else
                {
                    FragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    FragTrans.replace(R.id.customermain_frame, customerhomepage.newInstance());
                    FragTrans.addToBackStack(null);
                    FragTrans.commit();
                }
                break;
        }
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
