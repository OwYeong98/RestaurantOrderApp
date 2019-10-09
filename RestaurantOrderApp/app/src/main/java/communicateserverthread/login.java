package communicateserverthread;

import android.app.Activity;
import android.content.Intent;

import com.oymj.restaurantorderapp.adminfragment.adminmainscreen;
import com.oymj.restaurantorderapp.customerfragment.customermainscreen;
import com.oymj.restaurantorderapp.employeefragment.employeemainscreen;
import com.oymj.restaurantorderapp.loginfailed;
import com.oymj.restaurantorderapp.serverconnectionerror;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by OwYeong on 9/12/2017.
 */

public class login implements Runnable
{
    private Activity activity;
    private String username;
    private String password;
    private Thread thread;
    Socket s;
    String privillage;

    //contructor for collect username password input from user
    public login(Thread thread,Activity activity,String username, String password)
    {
        this.username=username;
        this.password=password;
        this.activity=activity;
        this.thread=thread;
    }

    @Override
    public void run()
    {
        try {


            s = new Socket();

            s.connect(new InetSocketAddress(Serverip.serveripaddr,11000),6000);
            s.setSoTimeout(0);

            //make loading screen at least stay for 4 second
            thread.sleep(4000);




            DataOutputStream dout=new DataOutputStream(s.getOutputStream());


            //request format --> requestkey-field required
            //format for LG request--> LG-username-password
            String sendrequest="LG-"+username+"-"+password;



            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server



            //wait for input
            DataInputStream dis=new DataInputStream(s.getInputStream());
            String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);



            String replycode[]= codefromserver.split("-");



            //server reply code format
            // if not successfull RE-LG-NO
            //if successful RE-LG-YES

            String successornot=replycode[2];

            if(replycode.length>3)
            {
                privillage=replycode[3];
            }



            if(successornot.equals("YES"))
            {


                //runOnUiThread function is to let the function to be run on main thread
                //bacause UI function cannot be run on worker thread
                //if equals yes means successful
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        //Do your UI operations like dialog opening or Toast here
                        //make it at least stay at loading screen for 7second


                        if(privillage.equals("admin"))
                        {
                            Intent in = new Intent(activity, adminmainscreen.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(in);
                        }else if (privillage.equals("employee"))
                        {
                            Intent in = new Intent(activity, employeemainscreen.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(in);
                        }else
                        {
                            Intent in = new Intent(activity, customermainscreen.class);
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            activity.startActivity(in);
                        }


                        //run serversocket to accept packet from server
                        startacceptpacket.startacceptpacketfromserver(s);

                    }
                });


            }else
            {

                //if no navigate user back to the login page
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        //Do your UI operations like dialog opening or Toast here

                        Intent in = new Intent(activity, loginfailed.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(in);


                    }
                });
            }


        }catch(Exception e)
        {
            e.printStackTrace();

            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent in = new Intent(activity, serverconnectionerror.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(in);
                }
            });
        }

    }
}
