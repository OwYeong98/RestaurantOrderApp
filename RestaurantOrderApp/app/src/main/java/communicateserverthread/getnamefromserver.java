package communicateserverthread;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Spinner;
import android.widget.TextView;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.adminhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.sessiontimeout;
import com.oymj.restaurantorderapp.userspinneradapter;
import com.oymj.restaurantorderapp.employeefragment.employeehomepage;
import com.oymj.restaurantorderapp.loginfailed;
import com.oymj.restaurantorderapp.serverconnectionerror;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by OwYeong on 9/9/2017.
 */

public class getnamefromserver implements Runnable
{
    Activity activity;
    Context context;
    String clientname;
    customerhomepage customerhomepage;
    employeehomepage employeehomepage;
    adminhomepage adminhomepage;
    String privillage=null;

    public getnamefromserver(customerhomepage customerhomepage)
    {
        this.customerhomepage=customerhomepage;
        activity= customerhomepage.getActivity();
        privillage="customer";
        context=activity;

    }

    public getnamefromserver(employeehomepage employeehomepage)
    {
        this.employeehomepage=employeehomepage;
        activity=employeehomepage.getActivity();
        privillage="employee";
        context=activity;

    }

    public getnamefromserver(adminhomepage adminhomepage)
    {
        this.adminhomepage=adminhomepage;
        activity=adminhomepage.getActivity();
        privillage="admin";
        context=activity;

    }

    @Override
    public void run()
    {
        try {

            Socket soc = new Socket();

            soc.setSoTimeout(2000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),2000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());


            //request format --> requestkey-field required
            //format for GT request--> GT-whatinformation
            String sendrequest="GT-NM";

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

            String availableornot=replycode[1];

            if(availableornot.equals("sessiontimeout"))
            {
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Intent in = new Intent(activity, sessiontimeout.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(in);

                    }
                });

            }else
            {
                clientname=replycode[2];
                activity.runOnUiThread(new Runnable()
                {
                    public void run()
                    {

                        //pass name to setspinner function
                        setspinner(clientname);

                    }
                });
            }

        }catch(Exception e)
        {
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

    public void setspinner(final String name)
    {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                //Do your UI operations like dialog opening or Toast here
                //navigate user back to connectionerror page so that user know
                Spinner userspinner= (Spinner) activity.findViewById(R.id.userspinner);

                if(privillage.equals("admin"))
                {
                    userspinneradapter spinneradapter=new userspinneradapter(activity,activity,name, adminhomepage);
                    userspinner.setAdapter(spinneradapter);
                }else if(privillage.equals("employee"))
                {
                    userspinneradapter spinneradapter=new userspinneradapter(activity,activity,name,employeehomepage);
                    userspinner.setAdapter(spinneradapter);
                }else
                {
                    userspinneradapter spinneradapter=new userspinneradapter(activity,activity,name,customerhomepage);
                    userspinner.setAdapter(spinneradapter);
                }


                TextView tv= (TextView) activity.findViewById(R.id.showusername);
                tv.setText(clientname);

            }
        });
        //set thing to spinner

    }



}
