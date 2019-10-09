package communicateserverthread.admincommunicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.admineditfooddetail;
import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
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
 * Created by OwYeong on 10/14/2017.
 */

public class deletefoodmenu implements Runnable
{
    admineditfoodmenu admineditfoodmenu;
    String foodname;
    Socket soc;
    public deletefoodmenu(admineditfoodmenu admineditfoodmenu, String foodname)
    {
        //get activity from class called
        this.admineditfoodmenu=admineditfoodmenu;
        this.foodname=foodname;
    }


    @Override
    public void run()
    {
        try{

            soc=new Socket();
            soc.setSoTimeout(3000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),3000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());


            //request format for payment --> DF-foodname
            String sendrequest="DF-"+foodname;
            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);


            String replycode[]= codefromserver.split("-");

            //server reply code format
            // if got error RE-DF-NO
            //if successfull RE-DF-YES
            if(replycode[1].equals("sessiontimeout"))
            {
                admineditfoodmenu.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(admineditfoodmenu.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        admineditfoodmenu.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;

                    }
                });
            }else
            {
                String sessionavailableornot=replycode[2];

                if(sessionavailableornot.equals("OK"))
                {
                    admineditfoodmenu.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            for(int loop = 0; loop< foodmenu.foodlist.size(); loop++)
                            {
                                if(foodmenu.foodlist.get(loop).getFoodname().equals(foodname))
                                {
                                    foodmenu.foodlist.remove(loop);
                                }
                            }

                            Toast.makeText(admineditfoodmenu.getActivity(), "Successfully deleted!!", Toast.LENGTH_LONG).show();
                            FragmentTransaction transaction;
                            transaction = admineditfoodmenu.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.adminmain_frame, admineditfoodmenu.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(admineditfoodmenu.getActivity());
                    dialog.setMessage("Some error occured! Delete was not made successfully. ");
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

        }catch(Exception e) {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            admineditfoodmenu.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(admineditfoodmenu.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    admineditfoodmenu.getActivity().startActivity(inerr);
                }
            });
        }

    }
}
