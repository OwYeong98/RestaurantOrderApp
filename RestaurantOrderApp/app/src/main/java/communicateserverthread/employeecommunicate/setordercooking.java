package communicateserverthread.employeecommunicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.employeecurrentpaymentrequest;
import com.oymj.restaurantorderapp.employeefragment.employeeorderinqueue;
import com.oymj.restaurantorderapp.employeefragment.employeetablepayment;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import communicateserverthread.Serverip;

/**
 * Created by OwYeong on 11/6/2017.
 */

public class setordercooking implements Runnable
{
    private employeeorderinqueue employeeorderinqueue;
    Socket soc;
    private int tableno;

    public setordercooking(employeeorderinqueue employeeorderinqueue, int tableno)
    {
        //get activity from class called
        this.employeeorderinqueue=employeeorderinqueue;
        this.tableno=tableno;

    }


    @Override
    public void run()
    {
        try{

            soc=new Socket();
            soc.setSoTimeout(10000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),10000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());



            //request format for cook condition --> CS-tableno
            String sendrequest="CS-"+tableno;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);


            String replycode[]= codefromserver.split("-");

            //server reply code format
            // if not used on database RE-PD-NO
            //if used on database RE-PD-YES
            if(replycode[1].equals("sessiontimeout"))
            {
                employeeorderinqueue.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(employeeorderinqueue.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        employeeorderinqueue.getActivity().startActivity(inerr);

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
                    employeeorderinqueue.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            Toast.makeText(employeeorderinqueue.getActivity(), "Recorded table "+tableno+" start cooking now!", Toast.LENGTH_LONG).show();

                        }
                    });
                }
            }


            soc.close();

        }catch(Exception e) {
            e.printStackTrace();
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            employeeorderinqueue.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(employeeorderinqueue.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    employeeorderinqueue.getActivity().startActivity(inerr);
                }
            });
        }

    }
}
