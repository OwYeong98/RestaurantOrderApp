package communicateserverthread.employeecommunicate;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.employeecurrentpaymentrequest;
import com.oymj.restaurantorderapp.employeefragment.employeeorderinqueue;
import com.oymj.restaurantorderapp.employeefragment.employeetablefoodordered;
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

public class setorderdone implements Runnable
{
    private employeetablefoodordered employeetablefoodordered;
    Socket soc;
    private int tableno;

    public setorderdone(employeetablefoodordered employeetablefoodordered, int tableno)
    {
        //get activity from class called
        this.employeetablefoodordered=employeetablefoodordered;
        this.tableno=tableno;

    }


    @Override
    public void run()
    {
        try{

            soc=new Socket();
            soc.setSoTimeout(3000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),3000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());



            //request format for payment --> SC
            String sendrequest="CD-"+tableno;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);


            String replycode[]= codefromserver.split("-");

            //server reply code format
            // if not used on database RE-CD-NO
            //if used on database RE-CD-YES
            if(replycode[1].equals("sessiontimeout"))
            {
                employeetablefoodordered.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(employeetablefoodordered.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        employeetablefoodordered.getActivity().startActivity(inerr);

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
                    employeetablefoodordered.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            Toast.makeText(employeetablefoodordered.getActivity(), "Recorded table "+tableno+" cook done!", Toast.LENGTH_LONG).show();
                            FragmentTransaction transaction;
                            transaction = employeetablefoodordered.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.employeemain_frame, employeeorderinqueue.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }
                    });
                }
            }



            soc.close();

        }catch(Exception e) {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            employeetablefoodordered.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(employeetablefoodordered.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    employeetablefoodordered.getActivity().startActivity(inerr);
                }
            });
        }

    }
}

