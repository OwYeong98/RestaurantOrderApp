package communicateserverthread;

import android.app.Activity;
import android.content.Intent;

import com.oymj.restaurantorderapp.LoginScreen;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiondetected;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

/**
 * Created by OwYeong on 9/9/2017.
 */

public class checksession implements Runnable
{
    private Activity activity;
    Socket soc;
    String privillage;

    public checksession(Activity activity)
    {
        //get activity from class called
        this.activity=activity;
        //this.soc=soc;

    }


    @Override
    public void run()
    {
        try{

            soc=new Socket();

            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);
            soc.setSoTimeout(0);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());



            //request format --> requestkey-field required
            //format for LG request--> LG-username-password
            String sendrequest="SS";

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

            String sessionavailableornot=replycode[2];


            if(sessionavailableornot.equals("YES"))
            {
                privillage=replycode[3];
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        //Do your UI operations like dialog opening or Toast here
                        //navigate user to main screen

                        Intent in = new Intent(activity, sessiondetected.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.putExtra("privillage",privillage);
                        activity.startActivity(in);

                        //run serversocket to accept packet from server
                        startacceptpacket.startacceptpacketfromserver(soc);
                    }
                });

            }

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
