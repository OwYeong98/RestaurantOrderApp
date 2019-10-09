package communicateserverthread;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.adminfragment.adminhomepage;
import com.oymj.restaurantorderapp.allprofile;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.employeehomepage;
import com.oymj.restaurantorderapp.employeefragment.employeeorderinqueue;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;


/**
 * Created by OwYeong on 11/5/2017.
 */

public class editprofile implements Runnable
{
    allprofile allprofile;
    String name;
    String age;
    String password;
    String gender;



    public editprofile(allprofile allprofile,String name,String age,String password,String gender)
    {
        this.allprofile=allprofile;
        this.name=name;
        this.age=age;
        this.password=password;
        this.gender=gender;
    }

    @Override
    public void run()
    {
        try {

            Socket soc = new Socket();

            soc.setSoTimeout(6000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());

            //Edit profile format is PF-name-password-age-gender
            //request format --> requestkey-field required
            //format for GT request--> GT-whatinformation
            String sendrequest="PF-"+name+"-"+password+"-"+age+"-"+gender;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
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
                String successornot=replycode[2];



                if(successornot.equals("OK"))
                {
                    soc = new Socket();

                    soc.setSoTimeout(6000);
                    soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);
                    dout=new DataOutputStream(soc.getOutputStream());
                    dis=new DataInputStream(soc.getInputStream());

                    sendrequest="GT-PR";

                    //send requestcode to server
                    dout.writeUTF(sendrequest);
                    dout.flush();//refresh to make sure it send to the server

                    //wait for input
                    dis=new DataInputStream(soc.getInputStream());
                    codefromserver=(String)dis.readUTF();

                    replycode =codefromserver.split("-");

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
                        String privillage=replycode[2];
                        if(privillage.equals("admin"))
                        {
                            allprofile.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //Do your UI operations like dialog opening or Toast here
                                    //navigate user to main screen

                                    Toast.makeText(allprofile.getActivity(), "Successfully edited!!", Toast.LENGTH_LONG).show();
                                    FragmentTransaction transaction;
                                    transaction = allprofile.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.adminmain_frame, adminhomepage.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            });
                        }else if(privillage.equals("employee"))
                        {
                            allprofile.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //Do your UI operations like dialog opening or Toast here
                                    //navigate user to main screen

                                    Toast.makeText(allprofile.getActivity(), "Successfully edited!!", Toast.LENGTH_LONG).show();
                                    FragmentTransaction transaction;
                                    transaction = allprofile.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.employeemain_frame, employeehomepage.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            });
                        }else{
                            allprofile.getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //Do your UI operations like dialog opening or Toast here
                                    //navigate user to main screen

                                    Toast.makeText(allprofile.getActivity(), "Successfully edited!!", Toast.LENGTH_LONG).show();
                                    FragmentTransaction transaction;
                                    transaction = allprofile.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.customermain_frame, customerhomepage.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            });
                        }
                    }




                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(allprofile.getActivity());
                    dialog.setMessage("Some error occured! Edit was not made successfully. ");
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
            }



        }catch(Exception e)
        {
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
}
