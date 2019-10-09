package communicateserverthread;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.oymj.restaurantorderapp.LoginScreen;
import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by OwYeong on 10/4/2017.
 */

public class logout implements Runnable
{
    private Activity activity;
    Socket soc;

    public logout(Activity activity)
    {
        //get activity from class called
        this.activity=activity;

    }


    @Override
    public void run()
    {
        try{

            soc=new Socket();
            soc.setSoTimeout(6000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());



            //request format --> requestkey-field required
            //format for LG request--> LG-username-password
            String sendrequest="LO";

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);


            String replycode[]= codefromserver.split("-");

            //server reply code format
            // if Successfull RE-LO-OK
            //if got error RE-LO-NO

            if(replycode[1].equals("sessiontimeout"))
            {
                activity.runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(activity, sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(inerr);
                    }
                });
            }else
            {
                String sessionavailableornot=replycode[2];


                if(sessionavailableornot.equals("OK"))
                {
                    //clear all old arraylist
                    database_currentrequest.currentorder.clear();
                    foodmenu.foodlist.clear();
                    foodmenu.foodcartconfirmed.clear();
                    foodmenu.foodcart.clear();
                    customercartconfirmed.callbefore=false;
                    customerpayment.callbefore=false;

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen

                            Intent in = new Intent(activity, LoginScreen.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(in);


                        }
                    });

                }
            }



            soc.close();

        }catch(Exception e) {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(activity, serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(inerr);
                }
            });
        }

    }
}
