package communicateserverthread.customercommunicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodcartconfirmed;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

import communicateserverthread.Serverip;

/**
 * Created by OwYeong on 10/29/2017.
 */

public class deletefoodordered implements Runnable
{

    private com.oymj.restaurantorderapp.customerfragment.customercartconfirmed customercartconfirmed;
    Socket soc;
    String foodname;

    public deletefoodordered(customercartconfirmed customercartconfirmed,String foodname)
    {
        //get activity from class called
        this.customercartconfirmed=customercartconfirmed;
        this.foodname=foodname;

    }


    @Override
    public void run()
    {
        try{

            soc=new Socket();
            soc.setSoTimeout(3000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),3000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());



            //request format for payment --> CO
            String sendrequest="DO-"+foodname;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);


            String replycode[]= codefromserver.split("-");

            //server reply code format
            // if not successful RE-DO-NO
            //if successful RE-DO-OK
            if(replycode[1].equals("sessiontimeout"))
            {
                customercartconfirmed.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(customercartconfirmed.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        customercartconfirmed.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;
                    }
                });
            }else
            {
                String sessionavailableornot=replycode[2];

                if(sessionavailableornot.equals("OK"))
                {
                    customercartconfirmed.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen

                            for(int loop=0;loop<foodmenu.foodcartconfirmed.get(0).getFoodordered().size();loop++)
                            {
                                if(foodmenu.foodcartconfirmed.get(0).getFoodordered().get(loop).getFoodname().equals(foodname))
                                {
                                    foodmenu.foodcartconfirmed.get(0).getFoodordered().remove(loop);
                                }
                            }


                            FragmentTransaction transaction;
                            transaction = customercartconfirmed.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.customermain_frame, com.oymj.restaurantorderapp.customerfragment.customercartconfirmed.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                }else
                {
                    customercartconfirmed.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen

                            AlertDialog.Builder dialog = new AlertDialog.Builder(customercartconfirmed.getActivity());
                            dialog.setMessage("Your order is already preparing!!We cant delete the order");
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
                    });

                }
            }



            soc.close();

        }catch(Exception e) {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            customercartconfirmed.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(customercartconfirmed.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    customercartconfirmed.getActivity().startActivity(inerr);

                }
            });
        }

    }
}
