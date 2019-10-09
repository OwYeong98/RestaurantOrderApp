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
import com.oymj.restaurantorderapp.employeefragment.employeecurrentpaymentrequest;
import com.oymj.restaurantorderapp.employeefragment.employeefoodavailabilitycontrol;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import communicateserverthread.Serverip;

/**
 * Created by OwYeong on 10/9/2017.
 */

public class setfoodavailability implements Runnable
{
    private employeefoodavailabilitycontrol employeefoodavailabilitycontrol;
    Socket soc;
    private String foodname;
    private boolean availability;
    String availableornot;

    public setfoodavailability(employeefoodavailabilitycontrol employeefoodavailabilitycontrol,String foodname,boolean availability)
    {
        //get activity from class called
        this.employeefoodavailabilitycontrol=employeefoodavailabilitycontrol;
        this.foodname=foodname;
        this.availability=availability;
    }


    @Override
    public void run() {
        try {

            soc = new Socket();
            soc.setSoTimeout(3000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr, 11000), 3000);


            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());


            availableornot=null;

            if(availability==true)
            {
                availableornot="YES";
            }else
            {
                availableornot="NO";
            }




            //request format for payment --> FA-foodname-availableornot
            String sendrequest = "FA-"+foodname+"-"+availableornot;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            final String codefromserver = (String) dis.readUTF();
            System.out.println("reply:" + codefromserver);


            String replycode[] = codefromserver.split("-");

            //server reply code format
            // if not used on database RE-OP-NO
            //if used on database RE-OP-YES
            if(replycode[1].equals("sessiontimeout"))
            {
                employeefoodavailabilitycontrol.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(employeefoodavailabilitycontrol.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        employeefoodavailabilitycontrol.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;
                    }
                });
            }else
            {
                String successornot = replycode[2];

                if (successornot.equals("OK")) {
                    employeefoodavailabilitycontrol.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            for(int loop=0;loop< foodmenu.foodlist.size();loop++)
                            {
                                if(foodmenu.foodlist.get(loop).getFoodname().equals(foodname))
                                {
                                    char available;
                                    if(availability==true)
                                    {
                                        available='Y';
                                    }else
                                    {
                                        available='N';
                                    }
                                    foodmenu.foodlist.get(loop).setAvailableornot(available);
                                }
                            }
                            Toast.makeText(employeefoodavailabilitycontrol.getActivity(), "Successfully changed availability", Toast.LENGTH_LONG).show();
                            FragmentTransaction FragTrans;
                            FragTrans = employeefoodavailabilitycontrol.getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.employeemain_frame, employeefoodavailabilitycontrol.newInstance());
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();
                        }
                    });
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(employeefoodavailabilitycontrol.getActivity());
                    dialog.setMessage("Some error occured !");
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



            soc.close();

        } catch (Exception e) {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            e.printStackTrace();
            employeefoodavailabilitycontrol.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know

                    Intent inerr = new Intent(employeefoodavailabilitycontrol.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    employeefoodavailabilitycontrol.getActivity().startActivity(inerr);
                }
            });
        }
    }
}
