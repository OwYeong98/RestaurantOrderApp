package communicateserverthread.customercommunicate;

import android.content.DialogInterface;
import android.content.Intent;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm;
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


public class sentorder implements Runnable
{
    customercartnotconfirm customercartnotconfirm;
    int tableno;

    public sentorder(customercartnotconfirm customercartnotconfirm,int tableno)
    {
        this.customercartnotconfirm=customercartnotconfirm;
        this.tableno=tableno;


    }

    @Override
    public void run()
    {
        try {

            Socket soc = new Socket();

            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),2000);


            DataOutputStream dout=new DataOutputStream(soc.getOutputStream());


            //request format --> requestkey-field required
            //format for GT request--> GT-whatinformation
            String sendrequest="OR-"+tableno;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            DataInputStream dis=new DataInputStream(soc.getInputStream());
            final String  codefromserver=(String)dis.readUTF();
            System.out.println("reply:"+codefromserver);

            String[] splitcode=codefromserver.split("-");

            if(splitcode[1].equals("sessiontimeout"))
            {
                customercartnotconfirm.getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        Intent inerr = new Intent(customercartnotconfirm.getActivity(), sessiontimeout.class);
                        inerr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        customercartnotconfirm.getActivity().startActivity(inerr);

                        foodmenu.foodcart.clear();
                        foodmenu.foodcartconfirmed.clear();
                        customercartconfirmed.callbefore=false;
                        customerpayment.callbefore=false;
                    }
                });
            }else
            {
                if(splitcode[2].equals("OK"))
                {
                    for(int loop=0;loop< foodmenu.foodcart.size();loop++)
                    {
                        sendrequest=foodmenu.foodcart.get(loop).getFoodname()+"-"+ foodmenu.foodcart.get(loop).getQuantity()+"-"+
                                foodmenu.foodcart.get(loop).getRemark();

                        //send requestcode to server
                        dout.writeUTF(sendrequest);
                        dout.flush();//refresh to make sure it send to the server
                    }

                    //tell server food list end
                    dout.writeUTF("OR-END");
                    dout.flush();

                    //wait for input
                    String serverreply=(String)dis.readUTF();
                    System.out.println("reply:"+serverreply);





                    String replycode[]= codefromserver.split("-");
                    if(replycode[2].equals("OK"))
                    {
                        //clear the foodcart
                        foodmenu.foodcart.clear();

                        //successfully added redirect user to ordered menu
                        customercartnotconfirm.getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(customercartnotconfirm.getActivity(), "Order sent to server succesfully!!", Toast.LENGTH_LONG).show();
                                FragmentTransaction transaction;
                                transaction = customercartnotconfirm.getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.customermain_frame, customercartconfirmed.newInstance());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });

                    }
                }else
                {
                    //if table already exist
                    customercartnotconfirm.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            //Do your UI operations like dialog opening or Toast here
                            //navigate user back to connectionerror page so that user know
                            AlertDialog.Builder dialog = new AlertDialog.Builder(customercartnotconfirm.getActivity());
                            dialog.setMessage("Table already Ordered by other customer!!");
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
                    });

                }
            }









        }catch(Exception e)
        {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            customercartnotconfirm.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent in = new Intent(customercartnotconfirm.getActivity(), serverconnectionerror.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    customercartnotconfirm.getActivity().startActivity(in);
                }
            });
        }


    }

}
