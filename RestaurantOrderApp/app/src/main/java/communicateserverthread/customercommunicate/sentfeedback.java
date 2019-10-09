package communicateserverthread.customercommunicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerfeedback;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import communicateserverthread.Serverip;

/**
 * Created by OwYeong on 11/5/2017.
 */

public class sentfeedback implements Runnable {

    customerfeedback customerfeedback;
    int starcount;
    String comment;

    public sentfeedback(customerfeedback customerfeedback,int starcount,String comment)
    {
        this.customerfeedback=customerfeedback;
        this.starcount=starcount;
        this.comment=comment;
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
            String sendrequest="FB-"+starcount+"-"+comment;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);


            String replycode[]= codefromserver.split("-");

            //server reply code format
            // if food already cooking RE-EO-NO
            //if edited successfully RE-EO-OK
            if(replycode[1].equals("sessiontimeout"))
            {
                customerfeedback.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(customerfeedback.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        customerfeedback.getActivity().startActivity(inerr);

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
                    customerfeedback.getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Toast.makeText(customerfeedback.getActivity(), "Feedback successfully sent!", Toast.LENGTH_LONG).show();

                            FragmentTransaction FragTrans = customerfeedback.getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.customermain_frame, customerhomepage.newInstance());
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();

                        }
                    });

                }else
                {
                    customerfeedback.getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {

                            //pass name to setspinner function
                            Toast.makeText(customerfeedback.getActivity(), "Feedback not sent successfully please contact staff D:", Toast.LENGTH_LONG).show();

                            FragmentTransaction FragTrans = customerfeedback.getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.customermain_frame, customerhomepage.newInstance());
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();

                        }
                    });
                }
            }



        }catch(Exception e)
        {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            customerfeedback.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent in = new Intent(customerfeedback.getActivity(), serverconnectionerror.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    customerfeedback.getActivity().startActivity(in);
                }
            });
        }


    }
}
