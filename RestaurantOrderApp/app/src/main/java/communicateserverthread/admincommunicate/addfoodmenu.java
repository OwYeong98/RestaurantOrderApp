package communicateserverthread.admincommunicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.adminaddfood;
import com.oymj.restaurantorderapp.adminfragment.admineditfooddetail;
import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import communicateserverthread.Serverip;

/**
 * Created by OwYeong on 11/9/2017.
 */

public class addfoodmenu implements Runnable
{
    adminaddfood adminaddfood;
    Socket soc;

    String foodname;
    String fooddesc;
    double foodprice;
    int foodspicy;
    int containmeat;
    int foodsalty;
    int foodsweetness;
    int foodsour;
    int chefsuggest;
    int preparetime;

    public addfoodmenu(adminaddfood adminaddfood,String foodname,String fooddesc,double foodprice,int foodspicy,int containmeat,int foodsalty,int foodsweetness,int foodsour,int chefsuggest,int preparetime)
    {
        //get activity from class called
        this.adminaddfood=adminaddfood;

        this.foodname=foodname;
        this.fooddesc=fooddesc;
        this.foodprice=foodprice;
        this.foodspicy=foodspicy;
        this.containmeat=containmeat;
        this.foodsalty=foodsalty;
        this.foodsweetness=foodsweetness;
        this.foodsour=foodsour;
        this.chefsuggest=chefsuggest;
        this.preparetime=preparetime;

    }


    @Override
    public void run()
    {
        try{

            soc=new Socket();
            soc.setSoTimeout(3000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),3000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());


            //request format for payment --> EF-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweetness-foodsour-chefsuggest-preparetime
            //EF-thai satay-sad
            String sendrequest="AF-"+foodname+"-"+fooddesc+"-"+foodprice+"-"+foodspicy+"-"+containmeat+"-"+foodsalty+"-"+foodsweetness+"-"+foodsour+"-"+chefsuggest+"-"+preparetime;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);


            String replycode[]= codefromserver.split("-");

            //server reply code format
            // if got error RE-EF-NO
            //if successfull RE-EF-YES
            if(replycode[1].equals("sessiontimeout"))
            {
                adminaddfood.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(adminaddfood.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        adminaddfood.getActivity().startActivity(inerr);

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
                    adminaddfood.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            foodmenu.foodlist.add(new foodinformation(foodname,fooddesc,foodprice,foodspicy,containmeat,foodsalty,foodsweetness,foodsour,chefsuggest,preparetime,'Y'));

                            Toast.makeText(adminaddfood.getActivity(), "Successfully added!!", Toast.LENGTH_LONG).show();
                            FragmentTransaction transaction;
                            transaction = adminaddfood.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.adminmain_frame, admineditfoodmenu.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(adminaddfood.getActivity());
                    dialog.setMessage("Some error occured! Edit was not made successfully. ");
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
            adminaddfood.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(adminaddfood.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    adminaddfood.getActivity().startActivity(inerr);
                }
            });
        }

    }
}
