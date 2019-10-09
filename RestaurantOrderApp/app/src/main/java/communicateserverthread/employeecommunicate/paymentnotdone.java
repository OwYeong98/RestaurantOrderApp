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
import com.oymj.restaurantorderapp.employeefragment.employeetablepayment;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import communicateserverthread.Serverip;

/**
 * Created by OwYeong on 10/8/2017.
 */

public class paymentnotdone implements Runnable
{
    private com.oymj.restaurantorderapp.employeefragment.employeetablepayment employeetablepayment;
    Socket soc;
    private int tableno;

    public paymentnotdone(employeetablepayment employeetablepayment, int tableno)
    {
        //get activity from class called
        this.employeetablepayment=employeetablepayment;
        this.tableno=tableno;

    }


    @Override
    public void run() {
        try {

            soc = new Socket();
            soc.setSoTimeout(3000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr, 11000), 3000);


            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());


            //request format for payment --> CO
            String sendrequest = "PN-" + tableno;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            final String codefromserver = (String) dis.readUTF();
            System.out.println("reply:" + codefromserver);


            String replycode[] = codefromserver.split("-");

            //server reply code format
            // if not used on database RE-PN-NO
            //if used on database RE-PN-YES
            if(replycode[1].equals("sessiontimeout"))
            {
                employeetablepayment.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(employeetablepayment.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        employeetablepayment.getActivity().startActivity(inerr);

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
                    employeetablepayment.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            for (int loop = 0; loop < database_currentrequest.currentpaymentrequest.size(); loop++) {
                                if (database_currentrequest.currentpaymentrequest.get(loop).getTableno() == tableno) {
                                    database_currentrequest.currentpaymentrequest.remove(loop);
                                }
                            }

                            Toast.makeText(employeetablepayment.getActivity(), "Successfully recorded!!", Toast.LENGTH_LONG).show();
                            FragmentTransaction transaction;
                            transaction = employeetablepayment.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.employeemain_frame, employeecurrentpaymentrequest.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(employeetablepayment.getActivity());
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
            employeetablepayment.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(employeetablepayment.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    employeetablepayment.getActivity().startActivity(inerr);
                }
            });
        }
    }
}
