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
import com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm;
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

public class editfoodmenu implements Runnable
{
    admineditfooddetail admineditfooddetail;
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

    public editfoodmenu(admineditfooddetail admineditfooddetail,String foodname,String fooddesc,double foodprice,int foodspicy,int containmeat,int foodsalty,int foodsweetness,int foodsour,int chefsuggest,int preparetime)
    {
        //get activity from class called
        this.admineditfooddetail=admineditfooddetail;

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
            String sendrequest="EF-"+foodname+"-"+fooddesc+"-"+foodprice+"-"+foodspicy+"-"+containmeat+"-"+foodsalty+"-"+foodsweetness+"-"+foodsour+"-"+chefsuggest+"-"+preparetime;

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
                admineditfooddetail.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(admineditfooddetail.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        admineditfooddetail.getActivity().startActivity(inerr);

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
                    admineditfooddetail.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user to main screen
                            for(int loop=0;loop<foodmenu.foodlist.size();loop++)
                            {
                                if(foodmenu.foodlist.get(loop).getFoodname().equals(foodname))
                                {
                                    foodmenu.foodlist.get(loop).setFooddesc(fooddesc);
                                    foodmenu.foodlist.get(loop).setFoodprice(foodprice);
                                    foodmenu.foodlist.get(loop).setFoodspicy(foodspicy);
                                    foodmenu.foodlist.get(loop).setContainmeat(containmeat);
                                    foodmenu.foodlist.get(loop).setFoodsalty(foodsalty);
                                    foodmenu.foodlist.get(loop).setFoodsweetness(foodsweetness);
                                    foodmenu.foodlist.get(loop).setFoodsour(foodsour);
                                    foodmenu.foodlist.get(loop).setChefsuggest(chefsuggest);
                                    foodmenu.foodlist.get(loop).setPreparetime(preparetime);
                                }
                            }

                            Toast.makeText(admineditfooddetail.getActivity(), "Successfully edited!!", Toast.LENGTH_LONG).show();
                            FragmentTransaction transaction;
                            transaction = admineditfooddetail.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.adminmain_frame, admineditfoodmenu.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }
                    });
                }else
                {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(admineditfooddetail.getActivity());
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
            admineditfooddetail.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent inerr = new Intent(admineditfooddetail.getActivity(), serverconnectionerror.class);
                    inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    admineditfooddetail.getActivity().startActivity(inerr);
                }
            });
        }

    }
}
