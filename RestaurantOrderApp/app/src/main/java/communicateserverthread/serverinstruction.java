package communicateserverthread;

import android.support.v4.app.FragmentTransaction;
import android.widget.ListView;
import android.widget.Toast;

import com.oymj.restaurantorderapp.R;
import com.oymj.restaurantorderapp.adminfragment.admineditfoodmenu;
import com.oymj.restaurantorderapp.adminfragment.adminfoodsalesreport;
import com.oymj.restaurantorderapp.adminfragment.adminsalesreport;
import com.oymj.restaurantorderapp.adminfragment.adminviewfeedback;
import com.oymj.restaurantorderapp.customerfragment.customercartnotconfirm;
import com.oymj.restaurantorderapp.customerfragment.customerfeedback;
import com.oymj.restaurantorderapp.customerfragment.customerfoodmenu;
import com.oymj.restaurantorderapp.customerfragment.customerhomepage;
import com.oymj.restaurantorderapp.customerfragment.customerpayment;
import com.oymj.restaurantorderapp.database_feedback.feedback;
import com.oymj.restaurantorderapp.database_feedback.feedbackinfo;
import com.oymj.restaurantorderapp.database_orderhistory.foodorderhistory;
import com.oymj.restaurantorderapp.database_orderhistory.orderhistory;
import com.oymj.restaurantorderapp.database_orderhistory.orderhistoryrecord;
import com.oymj.restaurantorderapp.customerfragment.customercartconfirmed;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderfoodinformation;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.currentorderinqueue;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.database_currentrequest;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.ordernotpaid;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.ordernotpaidfoodinformation;
import com.oymj.restaurantorderapp.database_currentorderandpaymentandordernotpaid.paymentrequestinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodcartinformation;
import com.oymj.restaurantorderapp.database_foodmenuandcart.foodmenu;
import com.oymj.restaurantorderapp.employeefragment.employeecurrentpaymentrequest;
import com.oymj.restaurantorderapp.employeefragment.employeefoodavailabilitycontrol;
import com.oymj.restaurantorderapp.employeefragment.employeemainscreen;
import com.oymj.restaurantorderapp.employeefragment.employeeorderinqueue;
import com.oymj.restaurantorderapp.employeefragment.employeeordernotpaid;
import com.oymj.restaurantorderapp.employeefragment.listviewadapter.currentordertablelistview;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

/**
 * Created by OwYeong on 9/14/2017.
 *
 * Server send information to client
 * code format: UP-whattoupdate
 * if update foodmenu : UP-MN-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweerbess-foodsour-chefsugges-preparetime-availableornot
 *
 *
 *
 * if update current payment request by customer: UP-PR-tableno-username
 *
 * if update order not pay:  UP-ON-customerusername-date-totalpayment
 * followed by FL-foodname-quantity-totalprice
 * and send FL-END when foodlist end
 *
 *
 *
 *
 *
 *
 * if update foodcart: UP-FC-tableno,totalpayment-dateordered-cookcondition
 * followed by FL-foodname-quantity-remark
 * and sent FL-END when foodlist end
 *
 * if update order history:  UP-OR-orderid-totalpayment-date
 * followed by FL-foodname-quantity
 * and send FL-END when foodlist end
 *
 * if update feedback: UP-FB-username-date-starcount-comment
 *
 * if server want to tell the client that their cook is started cooking
 * cookcondition format : UP-CS-datestarted
 *
 * if server want to tell the client their cook is already done
 * cookdone format: UP-CD
 *
 * if server want to update the ordernotpaylist
 * the code format is: ON-start
 * Then the server will send ON-customerusername-date-totalpayment
 * Then FL-foodname-quantity-totalprice until FL-END
 * then send again until no more ordernotpay and send ON-END
 *
 *
 * if Server want to update the current Orderlist
 * the code format is : OL-start
 * Then the server will send OL-tableno-cookcondition-ordertime //which indenticate the order detail
 * then by FL-foodname-quantity-remark until the server send FL-END // which is food ordered in the order
 * and then send orderdetail until OL-END // which say that there are no more order to send
 *
 * if Server want to update the menu
 * the code format is : MN-start
 * Then the server will send:   MN-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty
 *                              -foodsweetness-foodsour-chefsugges-preparetime-availableornot
 * and send again until the server send MN-END //indenticate no more foodmenu item to send
 *
 * if server want to update current payment request
 * the code format is : PR-start
 * The the server will send: PR-tableno-username
 * and then send again until no more current payment request and send PR-END
 *
 *
 *
 */

public class serverinstruction implements Runnable
{
    Socket s;
    DataInputStream dis;
    DataInputStream disorder;

    public serverinstruction(Socket s)
    {
        this.s=s;
    }

    @Override
    public void run() {
        while(true){
            try {

                //keep check for any packet sent from client

                disorder = new DataInputStream(s.getInputStream());
                String requestfromserver = (String) disorder.readUTF();

                System.out.println("Server request: "+requestfromserver);

                String[] requestcode = requestfromserver.split("-");

                String functiontype = requestcode[0];


                switch (functiontype) {
                    case "UP":
                        updatedata(requestcode);
                        break;
                    case "OL":
                        updateorderlist(requestcode);
                        break;
                    case "MN":
                        updatemenu(requestcode);
                        break;
                    case "ON":
                        updateordernotpaylist(requestcode);
                        break;
                    case "PR":
                        updatecurrentpaymentrequest(requestcode);
                }

            }catch (SocketException e){
                System.out.print("connection lost!!");

            }catch (IOException e) {
                System.out.print("IOException catched at serverinstruction class!!");
            }
        }
    }

    public void updatedata(String[] codefromserver)
    {
        String replycode;
        String whattoupdate=codefromserver[1];

        switch (whattoupdate)
        {
            case "FC":
                //if update foodcart: UP-FC-tableno,totalpayment-dateordered-cookcondition
                //followed by FL-foodname-quantity-remark and sent FL-END when foodlist end
                int tableno;
                double totalpayment;
                String dateordered;
                boolean cookcondition=false;
                boolean cookdone=false;
                ArrayList<foodcartinformation> foodlist= new ArrayList<foodcartinformation>();
                String cookstartdate=null;

                tableno=Integer.parseInt(codefromserver[2]);
                totalpayment=Double.parseDouble(codefromserver[3]);
                dateordered=codefromserver[4];

                if(codefromserver[5].equals("DONE"))
                {
                    cookdone=true;
                }else if(codefromserver[5].equals("YES"))
                {
                    cookcondition=true;
                    cookstartdate=codefromserver[6];
                }else
                {
                    cookcondition=false;
                }

                //keep collect foodlist from server if server did not send END
                boolean keeploop=true;
                while(keeploop==true)
                {
                    try {
                        disorder=new DataInputStream(s.getInputStream());
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    try
                    {

                        String  requestfromclient=(String)disorder.readUTF();


                        String[] orderfoodcode=requestfromclient.split("-");

                        if(orderfoodcode[1].equals("END"))
                        {
                            keeploop=false;
                        }else
                        {
                            String cartfoodname=orderfoodcode[1];
                            int quantity=Integer.parseInt(orderfoodcode[2]);
                            String remark=orderfoodcode[3];

                            foodlist.add(new foodcartinformation(cartfoodname,quantity,remark));
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                //clear old one
                foodmenu.foodcartconfirmed.clear();

                //add to foodcart confirmed
                foodmenu.addfoodcartconfirmed(tableno,foodlist,totalpayment,dateordered,cookcondition);

                if(cookdone==true)
                {
                    foodmenu.foodcartconfirmed.get(0).setCookdone(true);
                }else if(cookcondition==true)
                {
                    foodmenu.foodcartconfirmed.get(0).setDatestartcook(cookstartdate);
                }

                customercartconfirmed.callbefore=true;

                break;

            case "OR":
                //receive order history
                int ORorderid;
                double ORtotalpayment;
                String ORdate;

                ORorderid=Integer.parseInt(codefromserver[2]);
                ORtotalpayment=Double.parseDouble(codefromserver[3]);
                ORdate=codefromserver[4];



                boolean ORkeeploop=true;

                ArrayList<foodorderhistory> ORfoodordered=new ArrayList<foodorderhistory>();

                while(ORkeeploop==true)
                {
                    try {
                        disorder=new DataInputStream(s.getInputStream());
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    try
                    {

                        String  requestfromclient=(String)disorder.readUTF();


                        String[] orderfoodcode=requestfromclient.split("-");

                        if(orderfoodcode[1].equals("END"))
                        {
                            ORkeeploop=false;
                        }else
                        {
                            System.out.println("New food added to arraylist!!");
                            String ORfoodname=orderfoodcode[1];
                            int ORquantity=Integer.parseInt(orderfoodcode[2]);

                            ORfoodordered.add(new foodorderhistory(ORfoodname,ORquantity));
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                boolean ORalreadyavailable=false;
                int ORavailableat=0;

                for(int loop=0;loop< orderhistoryrecord.orderhistory.size();loop++)
                {
                    if(orderhistoryrecord.orderhistory.get(loop).getOrderid()==ORorderid)
                    {
                        ORalreadyavailable=true;
                        ORavailableat=loop;
                    }
                }

                if(ORalreadyavailable==true)
                {
                    System.out.println("Successfully added order history");

                    orderhistoryrecord.orderhistory.remove(ORavailableat);
                    orderhistoryrecord.orderhistory.add(new orderhistory(ORorderid,ORtotalpayment,ORdate,ORfoodordered));

                }else
                {
                    System.out.println("Successfully added order history");

                    orderhistoryrecord.orderhistory.add(new orderhistory(ORorderid,ORtotalpayment,ORdate,ORfoodordered));
                }

                //if user is looking at the page that need to update refresh the page
                if(Currentfragment.currentfragmentclassname.equals("adminsalesreport"))
                {
                    try{
                        Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                try{
                                    FragmentTransaction transaction;
                                    transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.adminmain_frame, adminsalesreport.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }catch(NullPointerException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }catch ( Exception e)
                    {

                    }


                }

                if(Currentfragment.currentfragmentclassname.equals("adminfoodsalesreport"))
                {
                    try{
                        Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                try{
                                    FragmentTransaction transaction;
                                    transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.adminmain_frame, adminfoodsalesreport.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }catch(NullPointerException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }catch ( Exception e)
                    {

                    }

                }


                break;
            case "CS":
                String datestartcook=codefromserver[2];

                foodmenu.foodcartconfirmed.get(0).setCookcondition(true);
                foodmenu.foodcartconfirmed.get(0).setDatestartcook(datestartcook);

                employeemainscreen em;



                if(Currentfragment.currentfragmentclassname.equals("customercartconfirmed"))
                {
                    try{
                        Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                try{
                                    FragmentTransaction transaction;
                                    transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.customermain_frame, customercartconfirmed.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }catch(NullPointerException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }catch ( Exception e)
                    {

                    }

                }

                break;
            case "CD":
                foodmenu.foodcartconfirmed.get(0).setCookdone(true);


                if(Currentfragment.currentfragmentclassname.equals("customercartconfirmed"))
                {
                    try{
                        Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                try{
                                    FragmentTransaction transaction;
                                    transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.customermain_frame, customercartconfirmed.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }catch(NullPointerException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }catch ( Exception e)
                    {

                    }


                }

                break;
            case "PD":
                foodmenu.foodcartconfirmed.clear();
                foodmenu.foodcart.clear();
                customercartconfirmed.callbefore=false;
                customerpayment.callbefore=false;

                try{
                    Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            try{
                                FragmentTransaction transaction;
                                transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.customermain_frame, customerfeedback.newInstance());
                                transaction.addToBackStack(null);
                                transaction.commit();
                            }catch(NullPointerException e)
                            {
                                e.printStackTrace();
                            }

                        }
                    });
                }catch ( Exception e)
                {

                }




                break;
            case "FB":
                //update feedback format: UP-FB-username-date-starcount-comment
                String username=codefromserver[2];
                String date=codefromserver[3];
                int starcount=Integer.parseInt(codefromserver[4]);
                String comment=codefromserver[5];

                feedback.feedbacklist.add(new feedbackinfo(username,date,starcount,comment));

                //if user is looking at the page that need to update refresh the page
                if(Currentfragment.currentfragmentclassname.equals("adminviewfeedback"))
                {
                    try{
                        Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                try{
                                    FragmentTransaction transaction;
                                    transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.adminmain_frame, adminviewfeedback.newInstance());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }catch(NullPointerException e)
                                {
                                    e.printStackTrace();
                                }

                            }
                        });
                    }catch ( Exception e)
                    {

                    }


                }

                break;

        }

    }


    public void updateorderlist(String[] codefromserver)
    {
        database_currentrequest.currentorder.clear();
        while(true)
        {
            try{

                String serverrequest=(String)disorder.readUTF();
                String[] orderdetail=serverrequest.split("-");

                //if END the end loop
                if(orderdetail[1].equals("END"))
                {
                    break;
                }

                //OL-tableno-cookcondition-ordertime
                //followed by FL-foodname-quantity-remark



                int OLtableno=Integer.parseInt(orderdetail[1]);
                boolean OLcookcondition=false;
                boolean OLcookdone=false;
                String OLdateordered=orderdetail[3];

                if(orderdetail[2].equals("DONE"))
                {
                    OLcookcondition=true;
                    OLcookdone=true;
                }else if(orderdetail[2].equals("YES"))
                {
                    OLcookcondition=true;
                }

                ArrayList<currentorderfoodinformation> foodordered=new ArrayList<currentorderfoodinformation>();
                //get all the food ordered and store to foodordered
                boolean OLkeeploop=true;

                while(OLkeeploop==true)
                {

                    try
                    {

                        String  requestfromclient=(String)disorder.readUTF();


                        String[] orderfoodcode=requestfromclient.split("-");

                        if(orderfoodcode[1].equals("END"))
                        {
                            OLkeeploop=false;
                        }else
                        {
                            System.out.println("New food added to arraylist!!");
                            String OLfoodname=orderfoodcode[1];
                            int OLquantity=Integer.parseInt(orderfoodcode[2]);
                            String OLremark=orderfoodcode[3];

                            foodordered.add(new currentorderfoodinformation(OLfoodname,OLremark,OLquantity));
                        }

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                database_currentrequest.currentorder.add(new currentorderinqueue(OLtableno,OLcookcondition,OLcookdone,OLdateordered,foodordered));

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }



        //if user is looking at the page that need to update refresh the page
        if(Currentfragment.currentfragmentclassname.equals("employeeorderinqueue"))
        {
            try{
                Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try{
                            FragmentTransaction transactions;
                            transactions = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                            transactions.replace(R.id.employeemain_frame, employeeorderinqueue.newInstance());
                            transactions.addToBackStack(null);
                            transactions.commit();
                        }catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }catch ( Exception e)
            {

            }


        }
    }

    public void updatemenu(String[] codefromserver)
    {
        /*
        if Server want to update the menu
        the code format is : MN-start
        Then the server will send:   MN-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty
                                     -foodsweetness-foodsour-chefsugges-preparetime-availableornot
        and send again until the server send MN-END //indenticate no more foodmenu item to send
         */

        foodmenu.foodlist.clear();

        while (true)
        {
            try{

                String serverrequest=(String)disorder.readUTF();
                String[] fooddetail=serverrequest.split("-");

                //if END the end loop
                if(fooddetail[1].equals("END"))
                {
                    break;
                }

                String foodname=fooddetail[1];
                String fooddesc=fooddetail[2];
                double foodprice=Double.parseDouble(fooddetail[3]);
                int foodspicy=Integer.parseInt(fooddetail[4]);
                int containmeat=Integer.parseInt(fooddetail[5]);
                int foodsalty=Integer.parseInt(fooddetail[6]);
                int foodsweetness=Integer.parseInt(fooddetail[7]);
                int foodsour=Integer.parseInt(fooddetail[8]);
                int chefsuggest=Integer.parseInt(fooddetail[9]);
                int preparetime=Integer.parseInt(fooddetail[10]);
                char availableornot= fooddetail[11].charAt(0);



                foodmenu.addfooditem(foodname,fooddesc,foodprice,foodspicy,containmeat,foodsalty,foodsweetness,foodsour,chefsuggest,preparetime,availableornot);
                System.out.println(foodname+" updated successfully");

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //if user is looking at the page that need update refresh the page
        if(Currentfragment.currentfragmentclassname.equals("customerfoodmenu"))
        {
            try{
                Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try{
                            FragmentTransaction transaction;
                            transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.customermain_frame, customerfoodmenu.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }catch ( Exception e)
            {

            }


        }

        if(Currentfragment.currentfragmentclassname.equals("admineditfoodmenu"))
        {
            try{
                Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try{
                            FragmentTransaction transaction;
                            transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.adminmain_frame, admineditfoodmenu.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }catch ( Exception e)
            {

            }


        }

        if(Currentfragment.currentfragmentclassname.equals("employeefoodavailabilitycontrol"))
        {
            try{
                Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try{
                            FragmentTransaction transaction;
                            transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.employeemain_frame, employeefoodavailabilitycontrol.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();

                        }catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }catch ( Exception e)
            {

            }





        }

    }

    public void updateordernotpaylist(String[] codefromserver)
    {
        /*
        if server want to update the ordernotpaylist
        the code format is: ON-start
        Then the server will send ON-customerusername-date-totalpayment
        Then FL-foodname-quantity-totalprice until FL-END
        then send again until no more ordernotpay and send ON-END
        */
        orderhistoryrecord.ordernotpaid.clear();

        while(true)
        {
            try{

                String serverrequest=(String)disorder.readUTF();
                String[] ordernotpaydetail=serverrequest.split("-");

                //if END the end loop
                if(ordernotpaydetail[1].equals("END"))
                {
                    break;
                }

                //receive order not pay
                String ONusername;
                String ONdate;
                double ONtotalpayment;

                ONusername=ordernotpaydetail[1];
                ONdate=ordernotpaydetail[2];
                ONtotalpayment=Double.parseDouble(ordernotpaydetail[3]);

                boolean ONkeeploop=true;

                ArrayList<ordernotpaidfoodinformation> orderedfood=new ArrayList<ordernotpaidfoodinformation>();

                while(ONkeeploop==true)
                {
                    String  requestfromclient=(String)disorder.readUTF();


                    String[] orderfoodcode=requestfromclient.split("-");

                    if(orderfoodcode[1].equals("END"))
                    {
                        ONkeeploop=false;
                    }else
                    {
                        System.out.println("New food added to arraylist!!");
                        String ONfoodname=orderfoodcode[1];
                        int ONquantity=Integer.parseInt(orderfoodcode[2]);
                        double ONtotalprice=Double.parseDouble(orderfoodcode[3]);

                        orderedfood.add(new ordernotpaidfoodinformation(ONfoodname,ONquantity,ONtotalprice));
                    }
                }

                orderhistoryrecord.ordernotpaid.add(new ordernotpaid(ONusername,ONdate,ONtotalpayment,orderedfood));
                System.out.println("Successfully added order not paid");


            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        //if user is looking at the page that need to update refresh the page
        if(Currentfragment.currentfragmentclassname.equals("employeeordernotpaid"))
        {
            try{
                Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try{
                            FragmentTransaction transaction;
                            transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.employeemain_frame, employeeordernotpaid.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }catch ( Exception e)
            {

            }



        }

    }

    public void updatecurrentpaymentrequest(String[] codefromserver)
    {
        /*
        if server want to update current payment request
        the code format is : PR-start
        The the server will send: PR-tableno-username
        and then send again until no more current payment request and send PR-END
        */
        database_currentrequest.currentpaymentrequest.clear();

        while (true)
        {
            try{

                String serverrequest=(String)disorder.readUTF();
                String[] paymentrequestdetail=serverrequest.split("-");

                //if END the end loop
                if(paymentrequestdetail[1].equals("END"))
                {
                    break;
                }

                int PRtableno=Integer.parseInt(paymentrequestdetail[1]);
                String PRusername=paymentrequestdetail[2];

                database_currentrequest.currentpaymentrequest.add(new paymentrequestinformation(PRusername,PRtableno));





            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        //if user is looking at the page that need to update refresh the page
        if(Currentfragment.currentfragmentclassname.equals("employeecurrentpaymentrequest"))
        {
            try{
                Currentfragment.currentfragment.getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try{
                            FragmentTransaction transaction;
                            transaction = Currentfragment.currentfragment.getActivity().getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.employeemain_frame, employeecurrentpaymentrequest.newInstance());
                            transaction.addToBackStack(null);
                            transaction.commit();
                        }catch(NullPointerException e)
                        {
                            e.printStackTrace();
                        }

                    }
                });
            }catch ( Exception e)
            {

            }

        }

    }



}
