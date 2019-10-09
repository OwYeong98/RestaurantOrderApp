package communicateserverthread.customercommunicate;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.customerfragment.editorderedfood;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.serverconnectionerror;
import com.oymj.restaurantorderapp.sessiontimeout;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import communicateserverthread.Serverip;

/**
 * Created by OwYeong on 9/26/2017.
 */

public class editfoodcart implements Runnable
{
    editorderedfood editorderedfood;
    String clientname;
    String foodname;
    int quantity;
    String remark;

    public editfoodcart(editorderedfood editorderedfood,String foodname,int quantity,String remark)
    {
        this.editorderedfood=editorderedfood;
        this.foodname=foodname;
        this.quantity=quantity;
        this.remark=remark;
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
            String sendrequest="EO-"+foodname+"-"+quantity+"-"+remark;

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
                editorderedfood.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(editorderedfood.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        editorderedfood.getActivity().startActivity(inerr);
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
                    editorderedfood.getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {

                            for(int loop = 0; loop< foodmenu.foodcartconfirmed.get(0).getFoodordered().size(); loop++)
                            {
                                if(foodname.equals(foodmenu.foodcartconfirmed.get(0).getFoodordered().get(loop).getFoodname()))
                                {
                                    Toast.makeText(editorderedfood.getActivity(), "Edited Successfully!!", Toast.LENGTH_LONG).show();
                                    foodmenu.foodcartconfirmed.get(0).getFoodordered().get(loop).setQuantity(quantity);
                                    foodmenu.foodcartconfirmed.get(0).getFoodordered().get(loop).setRemark(remark);


                                    FragmentTransaction FragTrans = editorderedfood.getActivity().getSupportFragmentManager().beginTransaction();
                                    FragTrans.replace(R.id.customermain_frame, customercartconfirmed.newInstance());
                                    FragTrans.addToBackStack(null);
                                    FragTrans.commit();
                                }
                            }

                        }
                    });

                }else
                {
                    editorderedfood.getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {

                            //pass name to setspinner function
                            Toast.makeText(editorderedfood.getActivity(), "Edit not success because food is preparing!! D:", Toast.LENGTH_LONG).show();

                            FragmentTransaction FragTrans = editorderedfood.getActivity().getSupportFragmentManager().beginTransaction();
                            FragTrans.replace(R.id.customermain_frame, customercartconfirmed.newInstance());
                            FragTrans.addToBackStack(null);
                            FragTrans.commit();

                        }
                    });
                }
            }



        }catch(Exception e)
        {
            e.printStackTrace();
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            editorderedfood.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent in = new Intent(editorderedfood.getActivity(), serverconnectionerror.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    editorderedfood.getActivity().startActivity(in);
                }
            });
        }


    }
}
