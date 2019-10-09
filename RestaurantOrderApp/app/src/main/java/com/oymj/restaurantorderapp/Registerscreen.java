package com.oymj.restaurantorderapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import communicateserverthread.Serverip;
import communicateserverthread.registeraccount;

public class Registerscreen extends AppCompatActivity implements View.OnClickListener{
    //for validate text
    EditText username;

    EditText password;
    EditText confirmpassword;
    EditText name;
    EditText email;
    EditText age;
    EditText gender;

    boolean validusername=false;
    boolean validpassword=false;
    boolean validconfirmpassword=false;
    boolean validname=false;
    boolean validemail=false;
    boolean validage=false;
    boolean valiadgender=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerscreen);

        ImageButton register=(ImageButton) findViewById(R.id.register);
        register.setOnClickListener(this);

        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        confirmpassword=(EditText) findViewById(R.id.confirmpassword);
        name=(EditText) findViewById(R.id.name);
        email=(EditText) findViewById(R.id.email);
        age=(EditText) findViewById(R.id.age);
        gender=(EditText) findViewById(R.id.gender);


        username.addTextChangedListener(new TextWatcher() {

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
                input=username.getText().toString();

                if(input!=null)
                {
                    if(input.length()<5||input.length()>15)
                    {
                        validusername=false;
                        username.setError("The username must be atleast 5 character and at most 15 character");
                    }else
                    if(input.contains("+")||input.contains("-")||input.contains("*")||input.contains("/")||input.contains(")")
                            ||input.contains("(")||input.contains("&")||input.contains("^")||input.contains("%")||input.contains("$")
                            ||input.contains("#")||input.contains("@")||input.contains("!")||input.contains("`")||input.contains("~")
                            ||input.contains("<")||input.contains(">")||input.contains(",")||input.contains(".")||input.contains("?")
                            ||input.contains("{")||input.contains("}")||input.contains("[")||input.contains("]")||input.contains("|"))
                    {
                        validusername=false;
                        username.setError("username should not contain symbols");
                    }else
                    {
                        Thread thread = new Thread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                try {

                                    Socket soc = new Socket(Serverip.serveripaddr, 11000);


                                    DataOutputStream dout=new DataOutputStream(soc.getOutputStream());


                                    //request format --> requestkey-field required
                                    //format for LG request--> LG-username-password
                                    String sendrequest="CK-UN-"+username.getText().toString();

                                    //send requestcode to server
                                    dout.writeUTF(sendrequest);
                                    dout.flush();//refresh to make sure it send to the server

                                    //wait for input
                                    DataInputStream dis=new DataInputStream(soc.getInputStream());
                                    final String  codefromserver=(String)dis.readUTF();
                                    System.out.println("reply:"+codefromserver);


                                    String replycode[]= codefromserver.split("-");

                                    //server reply code format
                                    // if not used on database RE-CK-NO
                                    //if used on database RE-CK-YES

                                    String usedornot=replycode[2];

                                    if(usedornot.equals("YES"))
                                    {
                                        Registerscreen.this.runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                validusername=false;
                                                username.setError("Username already used!!");
                                            }
                                        });

                                    }else
                                    {
                                        validusername=true;
                                    }

                                }catch(Exception e)
                                {
                                    //runOnUiThread function is to let the function to be run on main thread
                                    //bacause UI function cannot be run on worker thread
                                    Registerscreen.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            //Do your UI operations like dialog opening or Toast here
                                            //navigate user back to connectionerror page so that user know
                                            Intent in = new Intent(Registerscreen.this, serverconnectionerror.class);
                                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(in);
                                        }
                                    });
                                }
                            }
                        });//end thread
                        thread.start();

                    }//end else
                }else//else for input null
                {
                    validusername=false;
                    username.setError("You have not entered anything!");
                }

            }
        });

        password.addTextChangedListener(new TextWatcher() {

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
                input=password.getText().toString();
                boolean gotuppercase=!input.equals(input.toLowerCase());
                boolean gotnumber=input.contains("1")||input.contains("2")||input.contains("3")||input.contains("4")||input.contains("5")
                                  ||input.contains("6")||input.contains("7")||input.contains("8")||input.contains("9")||input.contains("0");


                if(input.length()<8||input.length()>16)
                {
                    validpassword=false;
                    password.setError("password length must be at least 8 character and at most 16 character!!");
                }else if(gotuppercase==true && input.contains("@")&&gotnumber==true)
                {
                    validpassword=true;
                }else
                {
                    validpassword=false;
                    password.setError("Please set a harder password! Password must contains Uppercase,number, and a @ symbol");
                }
            }
        });

        //password ocnfirm validation
        confirmpassword.addTextChangedListener(new TextWatcher() {

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
                String input=confirmpassword.getText().toString();

                if(input.equals(password.getText().toString())==false)
                {
                    validconfirmpassword=false;
                    confirmpassword.setError("The Password is not same");
                }else
                {
                    validconfirmpassword=true;
                }

            }

        });

        //validation for name
        name.addTextChangedListener(new TextWatcher() {

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
                String input=name.getText().toString();
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
                    name.setError("The name must have at least 6 character and at most 15 character");
                }else if(gotnumber==true)
                {
                    validname=false;
                    name.setError("Name should not contain number");
                }else if (gotsymbol==true)
                {
                    validname=false;
                    name.setError("Name should not contain symbol");
                }else
                {
                    validname=true;
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {

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
                String input=email.getText().toString();
                boolean gotsymbol=input.contains("+")||input.contains("-")||input.contains("*")||input.contains("/")||input.contains(")")
                        ||input.contains("(")||input.contains("&")||input.contains("^")||input.contains("%")||input.contains("$")
                        ||input.contains("#")||input.contains("|")||input.contains("!")||input.contains("`")||input.contains("~")
                        ||input.contains("<")||input.contains(">")||input.contains(",")||input.contains("]")||input.contains("?")
                        ||input.contains("{")||input.contains("}")||input.contains("[");

                if(input.length()<10||input.length()>50)
                {
                    validemail=false;
                    email.setError("Email should not be shorter than 10 character or longer than 50 character");
                }else if(gotsymbol==true)
                {
                    validemail=false;
                    email.setError("Email should not contain symbol");
                }else if(input.contains(".com")&&input.contains("@"))
                {
                    //email format all is correct here
                    //but we need to validate with server to see whether it is already used
                    Thread thread = new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try {

                                Socket soc = new Socket(Serverip.serveripaddr, 11000);


                                DataOutputStream dout=new DataOutputStream(soc.getOutputStream());


                                //request format --> requestkey-field required
                                //format for LG request--> LG-username-password
                                String sendrequest="CK-EM-"+email.getText().toString();

                                //send requestcode to server
                                dout.writeUTF(sendrequest);
                                dout.flush();//refresh to make sure it send to the server

                                //wait for input
                                DataInputStream dis=new DataInputStream(soc.getInputStream());
                                final String  codefromserver=(String)dis.readUTF();
                                System.out.println("reply:"+codefromserver);


                                String replycode[]= codefromserver.split("-");

                                //server reply code format
                                // if not used on database RE-CK-NO
                                //if used on database RE-CK-YES

                                String usedornot=replycode[2];

                                if(usedornot.equals("YES"))
                                {
                                    Registerscreen.this.runOnUiThread(new Runnable()
                                    {
                                        public void run()
                                        {
                                            validemail=false;
                                            email.setError("Email already used!!");
                                        }
                                    });

                                }else
                                {
                                    validemail=true;
                                }

                            }catch(Exception e)
                            {
                                //runOnUiThread function is to let the function to be run on main thread
                                //bacause UI function cannot be run on worker thread
                                Registerscreen.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        //Do your UI operations like dialog opening or Toast here
                                        //navigate user back to connectionerror page so that user know
                                        Intent in = new Intent(Registerscreen.this, serverconnectionerror.class);
                                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(in);
                                    }
                                });
                            }
                        }
                    });//end thread
                    thread.start();

                }else
                {
                    validemail=false;
                    email.setError("Please use a valid email format");
                }

            }
        });

        age.addTextChangedListener(new TextWatcher() {

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
                String input=age.getText().toString();

                if(input.length()>3)
                {
                    validage=false;
                    age.setError("Please put appropiate age!");
                }else
                {
                    validage=true;
                }

            }

        });

        gender.addTextChangedListener(new TextWatcher() {

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
                String input=gender.getText().toString().toLowerCase();


                if(input.equals("male")==false && input.equals("female")==false)
                {
                    valiadgender=false;
                    gender.setError("Please enter only 'male' or 'female'!!");
                }else
                {
                    valiadgender=true;
                }

            }

        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:

                boolean validinformation=validusername && validpassword && validconfirmpassword && validname && validemail && valiadgender && validage;

                if(validinformation==true)
                {
                    registeraccount rgaccount=new registeraccount(Registerscreen.this,username.getText().toString(),password.getText().toString(),name.getText().toString()
                    ,email.getText().toString(),age.getText().toString(),gender.getText().toString());

                    Thread rgthread=new Thread(rgaccount);

                    rgthread.start();




                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Registerscreen.this);
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

}
