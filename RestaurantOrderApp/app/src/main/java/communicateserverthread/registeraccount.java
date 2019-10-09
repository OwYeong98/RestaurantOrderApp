package communicateserverthread;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.oymj.restaurantorderapp.Registerscreen;
import com.oymj.restaurantorderapp.registersuccessful;
import com.oymj.restaurantorderapp.serverconnectionerror;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by OwYeong on 9/9/2017.
 */

public class registeraccount implements Runnable
{
    private String username;
    private String password;
    private String name;
    private String email;
    private String age;
    private String gender;
    private Activity activity;

    public registeraccount(Activity activity, String username, String password, String name, String email, String age, String gender)
    {
        this.username=username;
        this.password=password;
        this.name=name;
        this.email=email;
        this.age=age;
        this.gender=gender;
        this.activity=activity;
    }







    @Override
    public void run()
    {
        try {

            Socket soc = new Socket();
            soc.setSoTimeout(2000);
            soc.connect(new InetSocketAddress(Serverip.serveripaddr,11000),2000);


            DataOutputStream dout = new DataOutputStream(soc.getOutputStream());


            //request format --> requestkey-field required
            //format for LG request--> LG-username-password
            String sendrequest = "RG-" + username + "-" + password + "-" + name + "-" + email + "-" + age + "-" + gender;

            //send requestcode to server
            dout.writeUTF(sendrequest);
            dout.flush();//refresh to make sure it send to the server

            //wait for input
            DataInputStream dis = new DataInputStream(soc.getInputStream());
            final String codefromserver = (String) dis.readUTF();
            System.out.println("reply:" + codefromserver);


            String replycode[] = codefromserver.split("-");

            //server reply code format
            // if not used on database RE-CK-NO
            //if used on database RE-CK-YES

            String registersuccess = replycode[2];

            if (registersuccess.equals("YES")) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        Intent in = new Intent(activity, registersuccessful.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(in);
                    }
                });

            } else {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setMessage("Server did not send the success register code");
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

        } catch (Exception e) {
            //runOnUiThread function is to let the function to be run on main thread
            //bacause UI function cannot be run on worker thread
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    //Do your UI operations like dialog opening or Toast here
                    //navigate user back to connectionerror page so that user know
                    Intent in = new Intent(activity, serverconnectionerror.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(in);
                }
            });
        }
    }
}
