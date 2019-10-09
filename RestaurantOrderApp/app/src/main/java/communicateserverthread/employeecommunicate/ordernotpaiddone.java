package communicateserverthread.employeecommunicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.database_orderhistory.orderhistoryrecord;
import com.oymj.restaurantorderapp.employeefragment.employeeordernotpaid;
import com.oymj.restaurantorderapp.employeefragment.employeeordernotpaidfulldetail;
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

public class ordernotpaiddone implements Runnable
{
    private employeeordernotpaidfulldetail employeeordernotpaidfulldetail;
    Socket soc;
    private String date;
    private String username;

    public ordernotpaiddone(employeeordernotpaidfulldetail employeeordernotpaidfulldetail,String date,String username)
    {
        //get activity from class called
        this.employeeordernotpaidfulldetail=employeeordernotpaidfulldetail;
        this.date=date;
        this.username=username;
    }


    @Override
    public void run() {
        try {

            soc = new Socket();
            soc.setSoTimeout(3000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr, 11000), 3000);


            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());


            //request format for payment --> OP-username-date
            String sendrequest = "OP-"+username+"-"+date;

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
                employeeordernotpaidfulldetail.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(employeeordernotpaidfulldetail.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        employeeordernotpaidfulldetail.getActivity().startActivity(inerr);

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
                    employeeordernotpaidfulldetail.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            for (int loop = 0; loop < orderhistoryrecord.ordernotpaid.size(); loop++) {
                                if (orderhistoryrecord.ordernotpaid.get(loop).getDate().equals(date) && orderhistoryrecord.ordernotpaid.get(loop).getUsername().equals(username))
                                {
                                    orderhistoryrecord.ordernotpaid.remove(loop);
                                }
                            }

                            Toast.makeText(employeeordernotpaidfulldetail.getActivity(), "Successfully recorded!!", Toast.LENGTH_LONG).show();
                            FragmentTransaction transaction;
                            transaction = employeeordernotpaidfulldetail.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.employeemain_frame, employeeordernotpaid.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                } else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(employeeordernotpaidfulldetail.getActivity());
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
            employeeordernotpaidfulldetail.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(employeeordernotpaidfulldetail.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    employeeordernotpaidfulldetail.getActivity().startActivity(inerr);
                }
            });
        }
    }
}
