package communicateserverthread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.adminhomepage;
import com.oymj.restaurantorderapp.allprofile;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.employeehomepage;
import com.oymj.restaurantorderapp.loginfailed;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;
import com.oymj.restaurantorderapp.userspinneradapter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by OwYeong on 11/5/2017.
 */

public class profilegetinformation implements Runnable
{
    allprofile allprofile;
    String name=null;
    String age=null;
    String gender=null;
    String password=null;


    public profilegetinformation(allprofile allprofile)
    {
        this.allprofile=allprofile;
    }

    @Override
    public void run()
    {
        try {
            Socket soc = new Socket();

            soc.setSoTimeout(6000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
            DataInputStream dis=new DataInputStream(soc.getInputStream());

            //request format --> requestkey-field required
            //format for GT request--> GT-whatinformation
            String sendrequest="GT-NM";

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input

            String  codefromserver=(String)dis.readUTF();

            String replycode[]= codefromserver.split("-");

            if(replycode[1].equals("sessiontimeout"))
            {
                allprofile.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(allprofile.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        allprofile.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;
                    }
                });
            }else
            {
                name=replycode[2];
            }


            //server reply code format
            // if not used on database RE-GT-NO
            //if used on database RE-GT-YES


            soc = new Socket();
            soc.setSoTimeout(6000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);
            dout=new DataOutputStream(soc.getOutputStream());
            dis=new DataInputStream(soc.getInputStream());
            //request format --> requestkey-field required
            //format for GT request--> GT-whatinformation

            sendrequest="GT-AG";

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input

            codefromserver=(String)dis.readUTF();

            replycode= codefromserver.split("-");

            if(replycode[1].equals("sessiontimeout"))
            {
                allprofile.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(allprofile.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        allprofile.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;
                    }
                });
            }else
            {
                age=replycode[2];
            }




            soc = new Socket();
            soc.setSoTimeout(6000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);
            dout=new DataOutputStream(soc.getOutputStream());
            dis=new DataInputStream(soc.getInputStream());

            //request format --> requestkey-field required
            //format for GT request--> GT-whatinformation
            sendrequest="GT-GD";

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            codefromserver=(String)dis.readUTF();

            replycode= codefromserver.split("-");

            if(replycode[1].equals("sessiontimeout"))
            {
                allprofile.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(allprofile.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        allprofile.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;
                    }
                });
            }else
            {
                gender=replycode[2];
            }




            soc = new Socket();
            soc.setSoTimeout(6000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);
            dout=new DataOutputStream(soc.getOutputStream());
            dis=new DataInputStream(soc.getInputStream());

            //request format --> requestkey-field required
            //format for GT request--> GT-whatinformation
            sendrequest="GT-PW";

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            codefromserver=(String)dis.readUTF();

            replycode= codefromserver.split("-");

            if(replycode[1].equals("sessiontimeout"))
            {
                allprofile.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(allprofile.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        allprofile.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;
                    }
                });
            }else
            {
                password=replycode[2];
            }



            setinformation();


        }catch(Exception e)
        {
            e.printStackTrace();
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            allprofile.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent in = new Intent(allprofile.getActivity(), serverconnectionerror.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    allprofile.getActivity().startActivity(in);
                }
            });
        }


    }

    public void setinformation()
    {
        allprofile.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                //Do your UI operations like dialog opening or Toast here
                //navigate user back to connectionerror page so that user know
                EditText passwordinput=(EditText) allprofile.getActivity().findViewById(R.id.profilepasswordedit);
                EditText confirmpasswordinput=(EditText) allprofile.getActivity().findViewById(R.id.profileconfirmpasswordedit);
                EditText ageinput=(EditText) allprofile.getActivity().findViewById(R.id.profileageedit);
                EditText nameinput=(EditText) allprofile.getActivity().findViewById(R.id.profilenameedit);
                EditText genderinput=(EditText) allprofile.getActivity().findViewById(R.id.profilegenderedit);


                passwordinput.setText(password);
                ageinput.setText(age);
                nameinput.setText(name);
                genderinput.setText(gender);
                confirmpasswordinput.setText(password);

            }
        });
        //set thing to spinner

    }
}
