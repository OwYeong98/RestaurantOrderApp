package servercomponent;
//this class will handle all packet sent by the client and 
//reply to the client accordingly by what function it call

/*LOGIN Request
 * login function format is LG-username-password
 * login reply format:
 * if wrong RE-LG-NO
 * if correct RE-LG-YES-privillage
 * 
 * Logout Request
 * logout function format is LO
 * logout reply format:
 * if successfully RE-LO-OK
 * if error occured RE-LO-NO
 * 
 * Check Request
 * Check whether data already exist in database
 * check function format is CK-whattocheck-informationtocheck
 * there are 2 data to check username and email
 * username-> CK-UN-Stringtocheck
 * email->CK-EM-Stringtocheck
 * Check reply format:
 * if already exist RE-CK-YES
 * if not exist RE-CK-NO
 * 
 * Edit Profile
 * Edit user profile in database
 * Edit profile format is PF-name-password-age-gender
 * Edit Profile reply format:
 * if successful edited in database RE-PF-OK
 * if error occured                 RE-PF-NO
 * 
 * Register Request
 * Register an acount format is RG-username-password-name-email-age-gender
 * Register reply format:
 * if successful add to database RE-RG-YES
 * if not successful RE-RG-NO
 * 
 * Get information Request
 * Get information Request is GT-information
 * type of information to get: name
 * get name ->GT-NM
 * Get information reply format:
 * RE-GT-information
 * 
 * check session request:
 * Check whether their ip have account logged in on our server
 * check session function format is SS
 * Check session reply format:
 * if exist RE-SS-YES-privillage
 * if not exist RE-SS-NO
 * 
 * Order request:
 * add new order to the current order
 * Order request format: OR-tablenumber
 * Foodorder request format: foodname-quantity-remark
 * client will sent OR-tablenumber then
 * followed by foodname-quantity-remark until there is no more food
 * and send the OR-END
 * order reply format:
 * if ordered successfully RE-OR-OK
 * if table already ordered RE-OR-NO
 *
 * Edit order request:
 * edit existing order placed
 * Edit Order request format: EO-foodname-quantity-remark
 * Edit order reply code:
 * if edited successfully RE-EO-OK
 * if order already preparing RE-EO-NO
 * 
 * Delete food ordered request:
 * delete food ordered from existing order placed
 * Delete food ordered request formatL DO-foodname
 * Delete order reply code:
 * if deleted successfully RE-DO-OK
 * if order already preparing RE-EO-NO
 * 
 * Cancel order request:
 * cancel client order
 * Cancel order request format: CO
 * Cancel order reply code
 * if cancelled successfully RE-CO-OK
 * if order already preparing RE-CO-NO 
 * 
 * payment request:
 * call our waiter for payment
 * payment request format: PO
 * payment request reply code
 * if payment request successfully sent RE-PO-OK
 * if error occur not sent successful   RE-PO-NO
 * 
 * send feedback:
 * customer send feedback to us after paying
 * feedback request format: FB-starcount-comment
 * feedback request reply code
 * if feedback recorded successfully in database RE-FB-OK
 * if error occured                              RE-FB-NO
 * 
 * Record payment done:
 * record the order done to sql database
 * record payment done request: PD-tableno
 * record payment done reply code
 * if successfully save to database RE-PD-OK
 * if error occur                   RE-PD-NO
 * 
 * Record payment not done:
 * record the order done to sql database
 * record payment not done request: PN-tableno
 * record payment not done reply code
 * if successfully save to database RE-PN-OK
 * if error occur                   RE-PN-NO
 * 
 * Record order not paid done:
 * record order not paid as done
 * record order not paid request: OP-username-date
 * record order not paid reply code
 * if successfully save to database RE-OP-OK
 * if error occur                   RE-OP-NO
 * 
 * Set Food availability 
 * set food to be available to order or not available for order
 * send FA-foodname-YES if want to set the food availability to available
 * send FA-foodname-NO if want to set the food availability to not available
 * Set Food availability reply code
 * if successfully save to database RE-FA-OK
 * if error occur                   RE-FA-NO
 * 
 * Record cook start
 * record that the table order is start cooking
 * Record cook start format: CS-tableno
 * if successfully  RE-CS-OK
 * if error occur   RE-CS-NO
 * 
 * Record cook Done
 * record that the table order is start cooking
 * Record cook Done format: CD-tableno
 * if successfully  RE-CD-OK
 * if error occur   RE-CD-NO
 * 
 * 
 * Edit food menu
 * edit information of food inside food menu
 * edit food menu request code: EF-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweetness-foodsour-chefsuggest-preparetime
 * edit food menu replycode
 * if successfully edited RE-EF-OK
 * if error occured       RE-EF-NO
 * 
 * Delete food menu
 * delete a food from food menu
 * delete food menu request code: DF-foodname
 * delete food menu reply code
 * if successful delete RE-DF-OK
 * if error occured     RE-DF-NO
 * 
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import orderdatabase.currentpaymentrequest;
import orderdatabase.foodorderdetail;
import orderdatabase.orderdetail;

import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class handleclient implements Runnable
{
	private Socket client;
	private boolean keeprunning=true;
	DataInputStream disorder;
	private Socket clientUpdateStreamConnection;
	
	Scanner input = new Scanner(System.in);

	
	public handleclient(Socket client)
	{
		this.client=client;
		
	}
	
	
	
	@Override
	public void run() 
	{
		try
	   	{  
			
			//keep check for any packet sent from client
	    		
    		DataInputStream dis=new DataInputStream(client.getInputStream());  
    		String  requestfromclient=(String)dis.readUTF();  
    			
	    			
    		System.out.println(client.getInetAddress()+": "+requestfromclient);
	    		
    		String[] requestcode = requestfromclient.split("-");
    		
    		boolean haveloggedinaccount=false;
    		
    		for(int loop=0;loop<Server.Currentclient.size();loop++)
    		{
    			if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
    			{
    				haveloggedinaccount=true;
    				clientUpdateStreamConnection = Server.Currentclient.get(loop).getClientConnection();
    			}
    		}
    		
    		String requestfunction=requestcode[0];
    		String replycode=null;
    		
    		switch(requestfunction)
    		{
    			//for any type of privillage
    			case "LG":
    				System.out.println(client.getInetAddress().toString()+": Request login function");
    				replycode=loginrequest(requestfromclient);
    				break;
    			case "LO":
    				System.out.println(client.getInetAddress().toString()+": Request logout function");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=logoutrequest();
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    			case "SS":
    				System.out.println(client.getInetAddress().toString()+": Request to check loginsession");
    				replycode=checksession();
    				break;
    			case "PF":
    				System.out.println(client.getInetAddress().toString()+": Request edit Profile");
    				if(haveloggedinaccount==true)
    				{
    					replycode=editprofile(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			case "CK":
    				System.out.println(client.getInetAddress().toString()+": Request check data in sql");
    				replycode=checkrequest(requestfromclient);
    				break;
    			case "RG":
    				System.out.println(client.getInetAddress().toString()+": Request to register account");
    				replycode=registerrequest(requestfromclient);
    				break;
    			case "GT":
    				System.out.println(client.getInetAddress().toString()+": Request to get infomation");
    				if(haveloggedinaccount==true)
    				{
    					replycode=getinformationrequest(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			//for customer privillage
    			case "CO":
    				System.out.println(client.getInetAddress().toString()+": Request cancel order");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=cancelorder();
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    			case "OR":
    				System.out.println(client.getInetAddress().toString()+": Request to Add new Order");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=addorder(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    			case "PO":
    				System.out.println(client.getInetAddress().toString()+": Request for payment");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=paymentrequest();
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    			case "EO":
    				System.out.println(client.getInetAddress().toString()+": Request to edit order");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=editorder(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    			case "DO":
    				System.out.println(client.getInetAddress().toString()+": Request to delete food ordered");
    				if(haveloggedinaccount==true)
    				{
    					replycode=deletefoodordered(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			case "FB":
    				System.out.println(client.getInetAddress().toString()+": Request to send feedback");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=recordfeedback(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    			//for employee privillage
    			case "PD":
    				System.out.println(client.getInetAddress().toString()+": Record Table payment done");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=paymentdone(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			case "PN":
    				System.out.println(client.getInetAddress().toString()+": Record Table payment not done");
    				if(haveloggedinaccount==true)
    				{
    					replycode=paymentnotdone(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			case "OP":
    				System.out.println(client.getInetAddress().toString()+": Record Order not paid as done payment");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=ordernotpaiddone(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    			case "FA":
    				System.out.println(client.getInetAddress().toString()+": Set food availability");
    				if(haveloggedinaccount==true)
    				{
    					replycode=setfoodavailability(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			case "CS":
    				System.out.println(client.getInetAddress().toString()+": Record table start cook");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=cookstart(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    			
    				break;
    			case "CD":
    				System.out.println(client.getInetAddress().toString()+": Record table Cook done");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=cookdone(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			//for admin privillage
    			case "AF":
    				System.out.println(client.getInetAddress().toString()+": Request add food menu");
    				if(haveloggedinaccount==true)
    				{
    					replycode=addfoodmenu(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    				
    			case "EF":
    				System.out.println(client.getInetAddress().toString()+": Request edit food menu");
    				if(haveloggedinaccount==true)
    				{
    					replycode=editfoodmenu(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				
    				break;
    			case "DF":
    				System.out.println(client.getInetAddress().toString()+": Request Delete food menu");
    				
    				if(haveloggedinaccount==true)
    				{
    					replycode=deletefoodmenu(requestfromclient);
    				}else
    				{
    					replycode="RE-sessiontimeout";
    				}
    				break;
    				
    		}
    		
    		
    			   		
    		DataOutputStream dout=new DataOutputStream(client.getOutputStream());
	    			
 
    		dout.writeUTF(replycode);  
    		dout.flush();
    		
	    	System.out.println("replycode: "+replycode);
    			
    		
    		
    		

    		
    		//if user request login and login successful
	    	//or if user session detected
    		if(requestfunction.equals("LG") && replycode.split("-")[2].equals("YES") ||requestfunction.equals("SS") && replycode.split("-")[2].equals("YES"))
    		{
    			String privillage=null;
    			//find user privillage    		
        		for(int loop=0;loop<Server.Currentclient.size();loop++)
        		{
        			if(client.getInetAddress().toString().equals(Server.Currentclient.get(loop).getipaddr()))
        			{
        				privillage=Server.Currentclient.get(loop).getprivillage();
        			}
        		}
        		
        		if(privillage.equals("admin"))
        		{//admin
        			sendfoodlisttoclient();
        			sendorderhistorytoclient();
        			sendfeedbacklisttoclient();
        		}else if(privillage.equals("employee"))
        		{//employee
        			sendfoodlisttoclient();
        			sendcurrentorderlisttoclient();
        			sendcurrentpaymentrequesttoclient();
        			sendordernotpaytoclient();
        		}else
        		{//customer
        			sendfoodlisttoclient();
        			sendfoodcarttoclient();
        		}
    		}

	    
    	}catch(Exception e)
    	{
	    	System.out.println("Input output at thread handleclient got an Error!!");
	    	e.printStackTrace();
    	}
		

	}
	
	public void sendfoodcarttoclient()
	{
		String clientusername=null;
		
		String clientipaddr=client.getInetAddress().toString();
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		
		System.out.println("The client "+clientipaddr);
		
		Socket clientConnection= null;
		
		//find clientusername with their ipaddress
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			if (client.getInetAddress().toString().equals(Server.Currentclient.get(loop).getipaddr()))
			{
				clientusername=Server.Currentclient.get(loop).getusername();
				clientConnection = Server.Currentclient.get(loop).getClientConnection();
			}
		}
		
		//send foodcart to client if available
		for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
		{
			//check if client have currentorder with their username
			if (Server.Currentorderinqueue.get(loop).getcustomerusername().equals(clientusername))
			{
				ArrayList<foodorderdetail> foodcart = Server.Currentorderinqueue.get(loop).getfoodordered();
				try {
					
					DataOutputStream ssdout;
            	
					
					ssdout=new DataOutputStream(clientUpdateStreamConnection.getOutputStream());
					
					String cookcondition;
					
					String sendrequest;
					
					if(Server.Currentorderinqueue.get(loop).getcookdone()==true)
					{
						cookcondition="DONE";
						sendrequest="UP-FC-"+Server.Currentorderinqueue.get(loop).gettableno()
								+"-"+Server.Currentorderinqueue.get(loop).gettotalpayment()+"-"
								+Server.Currentorderinqueue.get(loop).getdateorder()+"-"+cookcondition;
					}else if(Server.Currentorderinqueue.get(loop).getcookcondition()==true)
					{
						cookcondition="YES";
						sendrequest="UP-FC-"+Server.Currentorderinqueue.get(loop).gettableno()
								+"-"+Server.Currentorderinqueue.get(loop).gettotalpayment()+"-"
								+Server.Currentorderinqueue.get(loop).getdateorder()+"-"+cookcondition+"-"+Server.Currentorderinqueue.get(loop).getcookstarttime();
					}else
					{
						cookcondition="NO";
						sendrequest="UP-FC-"+Server.Currentorderinqueue.get(loop).gettableno()
								+"-"+Server.Currentorderinqueue.get(loop).gettotalpayment()+"-"
								+Server.Currentorderinqueue.get(loop).getdateorder()+"-"+cookcondition;
					}
					



		            //send requestcode to server
		            ssdout.writeUTF(sendrequest);
		            ssdout.flush();//refresh to make sure it send to the server
					
    				//send the cart to client
		            for(int innerloop=0;innerloop<foodcart.size();innerloop++)
    				{
		            	//format to update client foodmenu : UP-FC-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweerbess-foodsour-chefsugges-preparetime-availableornot
			            String sendfoodlist="FL-"+foodcart.get(innerloop).getfoodname()+"-"+foodcart.get(innerloop).getquantity()+"-"+foodcart.get(innerloop).getremark();



			            //send requestcode to server
			            ssdout.writeUTF(sendfoodlist);
			            ssdout.flush();//refresh to make sure it send to the server
			            
			            System.out.println("customer food cart has been sent!!");
    				
    				}
		            
		            ssdout.writeUTF("FL-END");
		            ssdout.flush();//refresh to make sure it send to the server
		            
		            //if the order already start cooking
		            if(Server.Currentorderinqueue.get(loop).getcookdone()==true)
		            {
		            	tellclientcookdone(Server.Currentorderinqueue.get(loop).getcustomerusername());
		            }else if(Server.Currentorderinqueue.get(loop).getcookcondition()==true)
		            {
		            	tellclientcookstart(Server.Currentorderinqueue.get(loop).getcustomerusername(),Server.Currentorderinqueue.get(loop).getcookstarttime());
		            }
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public void tellclientcookstart(String username,String starttime)
	{

		String clientipaddr=null;
		Socket clientConnection= null;
		
		//find client ip address
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			System.out.println(Server.Currentclient.get(loop).getusername()+" not same arraylist size: "+Server.Currentclient.size());
			if(Server.Currentclient.get(loop).getusername().equals(username))
			{
				System.out.println(Server.Currentclient.get(loop).getusername()+" found");
				clientipaddr=Server.Currentclient.get(loop).getipaddr();
				clientConnection = Server.Currentclient.get(loop).getClientConnection();
			}
		}
		System.out.println("The username is: "+username);
	
		if(clientipaddr!=null)
		{
			StringBuilder ip = new StringBuilder(clientipaddr);
			
			ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
			
			clientipaddr=ip.toString();
			try {
				
				DataOutputStream ssdout;
				
				ssdout=new DataOutputStream(clientConnection.getOutputStream());
				

				
				String sendrequest="UP-CS-"+starttime;
				

		        //send requestcode to server
		        ssdout.writeUTF(sendrequest);
		        ssdout.flush();//refresh to make sure it send to the server
		        
		        //update client
		        Thread t = new Thread() {
		            public void run() {
		            	updatecurrentorderlisttoclient();
		            }
		        };
		        t.start();
		        
		        
			}catch(Exception e)
			{
				
			}
		}
		
		
		
	}
	
	public void tellclientcookdone(String username)
	{
		String clientipaddr=null;
		Socket clientConnection = null;
		
		//find client ip address
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			if(Server.Currentclient.get(loop).getusername().equals(username))
			{
				clientipaddr=Server.Currentclient.get(loop).getipaddr();
				clientConnection = Server.Currentclient.get(loop).getClientConnection();
			}
		}
		
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		try {
			DataOutputStream ssdout;
		
			
			ssdout=new DataOutputStream(clientConnection.getOutputStream());
			

			
			String sendrequest="UP-CD";
			

	        //send requestcode to server
	        ssdout.writeUTF(sendrequest);
	        ssdout.flush();//refresh to make sure it send to the server
	        
	      
	        
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	public void tellclientpaymentdone(String username)
	{
		String clientipaddr=null;
		Socket clientConnection = null;
		
		//find client ip address
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			if(Server.Currentclient.get(loop).getusername().equals(username))
			{
				clientipaddr=Server.Currentclient.get(loop).getipaddr();
				clientConnection = Server.Currentclient.get(loop).getClientConnection();
			}
		}
		
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		try {
			DataOutputStream ssdout;
		
			
			ssdout=new DataOutputStream(clientConnection.getOutputStream());
			

			
			String sendrequest="UP-PD";
			

	        //send requestcode to server
	        ssdout.writeUTF(sendrequest);
	        ssdout.flush();//refresh to make sure it send to the server
	        
	      
	        
		}catch(Exception e)
		{
			System.out.println("Unable to connect to: "+clientipaddr+" when tellclientpaymentdone");
		}
		
		
	}
	
	
	
	//this method is called to send foodmenu to the client and also client ordered foodcart to them if available
	//it will be called when client login or session detected
	public void sendfoodlisttoclient()
	{
		String clientipaddr=client.getInetAddress().toString();
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		
		try {
			 DataOutputStream ssdout;

			 ssdout=new DataOutputStream(clientUpdateStreamConnection.getOutputStream());
			 
			 String sendrequest="MN-start";
			 
			 ssdout.writeUTF(sendrequest);
			 ssdout.flush();//refresh to make sure it send to the server
			 
			 Class.forName("com.mysql.jdbc.Driver");
			 
			 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			 Statement stmt;
			 ResultSet rs;
			 stmt=con.createStatement();  
			 rs=stmt.executeQuery("select * from foodinfo");
	    		
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
			 char availableornot;
				
			 while(rs.next())
			 {
				 foodname=rs.getString(1);
				 fooddesc=rs.getString(2);
				 foodprice=rs.getDouble(3);
				 foodspicy=rs.getInt(4);
				 containmeat=rs.getInt(5);
				 foodsalty=rs.getInt(6);
				 foodsweetness=rs.getInt(7);
				 foodsour=rs.getInt(8);
				 chefsuggest=rs.getInt(9);
				 preparetime=rs.getInt(10);
				 availableornot=rs.getString(11).charAt(0);
	    			
				//format to update client foodmenu : UP-MN-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweerbess-foodsour-chefsugges-preparetime-availableornot
				sendrequest="MN-"+foodname+"-"+fooddesc+"-"+foodprice+"-" +foodspicy
						 +"-"+containmeat+"-"+foodsalty+"-"+foodsweetness+"-"+foodsour
						 +"-"+chefsuggest+"-"+preparetime+"-"+availableornot;
		         

		            //send requestcode to server
				 ssdout.writeUTF(sendrequest);
				 ssdout.flush();//refresh to make sure it send to the server
				 
			 }
			 con.close();
			 
			 sendrequest="MN-END";
			 
			 ssdout.writeUTF(sendrequest);
			 ssdout.flush();//refresh to make sure it send to the server
			 System.out.println("Done Send food list");
			 
		}catch(Exception e)
		{
			System.out.println("Could not connect to : "+clientipaddr+" When sending foodlist!");
			e.printStackTrace();
		}
    	
	}
	
	public void sendcurrentorderlisttoclient()
	{
		String clientipaddr=client.getInetAddress().toString();
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		
		try {
			 DataOutputStream ssdout;
			 
			 
			 ssdout=new DataOutputStream(clientUpdateStreamConnection.getOutputStream());
			 
			 String sendrequest="OL-start";
			 
			 ssdout.writeUTF(sendrequest);
			 ssdout.flush();//refresh to make sure it send to the server
			 
			 for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
			 {
				 int tableno=Server.Currentorderinqueue.get(loop).gettableno();
				 String cookcondition="NO";
				 String ordertime=Server.Currentorderinqueue.get(loop).getdateorder();
				 
				 if(Server.Currentorderinqueue.get(loop).getcookdone()==true)
				 {
					 cookcondition="DONE";
				 }else if(Server.Currentorderinqueue.get(loop).getcookcondition()==true)
				 {
					 cookcondition="YES";
				 }
				 
				 sendrequest="OL-"+tableno+"-"+cookcondition+"-"+ordertime;
				 
				 System.out.println(sendrequest);
				 
				 ssdout.writeUTF(sendrequest);
				 ssdout.flush();//refresh to make sure it send to the server
				 
				 for(int foodorderloop=0;foodorderloop<Server.Currentorderinqueue.get(loop).getfoodordered().size();foodorderloop++)
				 {
					 String foodname;
					 int quantity;
					 String remark;
					 
					 foodname=Server.Currentorderinqueue.get(loop).getfoodordered().get(foodorderloop).getfoodname();
					 quantity=Server.Currentorderinqueue.get(loop).getfoodordered().get(foodorderloop).getquantity();
					 remark=Server.Currentorderinqueue.get(loop).getfoodordered().get(foodorderloop).getremark();
							 
					 sendrequest="FL-"+foodname+"-"+quantity+"-"+remark;
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();
				 }
				 sendrequest="FL-END";
				 ssdout.writeUTF(sendrequest);

			 }
			 
			 sendrequest="OL-END";
			 ssdout.writeUTF(sendrequest);
			 
		 }catch(Exception e)
		 {
			 
		 }
	}
	
	public void sendordernotpaytoclient()
	{
		String clientipaddr=client.getInetAddress().toString();
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		
		
		try
		 {  
			 DataOutputStream ssdout;
			 
			 ssdout=new DataOutputStream(clientUpdateStreamConnection.getOutputStream());
			 
			 String sendrequest="ON-start";
			 ssdout.writeUTF(sendrequest);
			 ssdout.flush();//refresh to make sure it send to the server
			 
			 
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			 Statement stmt;
			 ResultSet rs;
			 stmt=con.createStatement();  
			 rs=stmt.executeQuery("select custusername,date,totalpayment,orderid from notpayorderhistory");
	    		
			 
			 while(rs.next())
			 {
				 sendrequest="ON-"+rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getDouble(3);
				 
				 
				 //send requestcode to server
				 ssdout.writeUTF(sendrequest);
				 ssdout.flush();//refresh to make sure it send to the server
				 System.out.println("order sent");
				 
				 Statement foodstmt=con.createStatement();  
				 ResultSet foodrs=foodstmt.executeQuery("select foodname,quantity,totalprice from notpayorderedfoodhistory where orderid="+rs.getString(4));
				 
				 while(foodrs.next())
				 {
					 sendrequest="FL-"+foodrs.getString(1)+"-"+foodrs.getInt(2)+"-"+foodrs.getDouble(3);
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();//refresh to make sure it send to the server
				 }
				 sendrequest="FL-END";
				 ssdout.writeUTF(sendrequest);
				 ssdout.flush();//refresh to make sure it send to the server
			 }
			 
			 sendrequest="ON-END";
			 ssdout.writeUTF(sendrequest);
			 ssdout.flush();//refresh to make sure it send to the server
			 
			 
			 con.close();
		 }catch(Exception e)
		 {
			 e.printStackTrace();
		 }
		
	}
	
	public void sendcurrentpaymentrequesttoclient()
	{
		String clientipaddr=client.getInetAddress().toString();//get client ipaddress with socket
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		
		try
		{
			 DataOutputStream ssdout;
			 
			 ssdout=new DataOutputStream(clientUpdateStreamConnection.getOutputStream());
			 
			 String sendrequest="PR-start";
			 ssdout.writeUTF(sendrequest);
			 ssdout.flush();//refresh to make sure it send to the server
			 
			 //code to send payment request to client
			 for(int loop=0;loop<Server.Currentpaymentrequest.size();loop++)
			 {
				 sendrequest="PR-"+Server.Currentpaymentrequest.get(loop).gettableno()+"-"+Server.Currentpaymentrequest.get(loop).getclientusername();
					
				 
				 //send requestcode to server
				 ssdout.writeUTF(sendrequest);
				 ssdout.flush();//refresh to make sure it send to the server
			 }
			 sendrequest="PR-END";
			 ssdout.writeUTF(sendrequest);
			 ssdout.flush();//refresh to make sure it send to the server
		}catch(Exception e)
		{
			
		}
		
	}
	
	public void sendorderhistorytoclient()
	{
		try
		{
			String clientipaddr=client.getInetAddress().toString();//get client ipaddress with socket
			
			StringBuilder ip = new StringBuilder(clientipaddr);
			
			ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
			
			clientipaddr=ip.toString();
			
			Class.forName("com.mysql.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			//here restaurantorderapp is database name, root is username and password 
			
			Statement stmt=con.createStatement();  
			ResultSet rs=stmt.executeQuery("select orderid,totalpayment,date from orderhistory");
			
			
			
			while(rs.next())
			{
				DataOutputStream ssdout;
	   
				
				ssdout=new DataOutputStream(clientUpdateStreamConnection.getOutputStream());
				
				
				String sendrequest="UP-OR-"+rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getString(3);
				
	            //send requestcode to server
	            ssdout.writeUTF(sendrequest);
	            ssdout.flush();//refresh to make sure it send to the server
	            
	            Statement foodstmt=con.createStatement();
	            ResultSet foodrs=foodstmt.executeQuery("select foodname,quantity from orderedfoodhistory where orderid="+rs.getString(1));
				
	            while(foodrs.next())
	            {
	            	sendrequest="FL-"+foodrs.getString(1)+"-"+foodrs.getString(2);
					
		            //send requestcode to server
		            ssdout.writeUTF(sendrequest);
		            ssdout.flush();//refresh to make sure it send to the server
	            }
	            
	            sendrequest="FL-END";
				
	            //send requestcode to server
	            ssdout.writeUTF(sendrequest);
	            ssdout.flush();//refresh to make sure it send to the server

				
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Error while sending order history to client");
		}
		
		
	}
	
	public void sendfeedbacklisttoclient()
	{
		String clientipaddr=client.getInetAddress().toString();
		
		StringBuilder ip = new StringBuilder(clientipaddr);
		
		ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
		
		clientipaddr=ip.toString();
		
		try
    	{  
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		Statement stmt;
    		ResultSet rs;
    		stmt=con.createStatement();  
    		rs=stmt.executeQuery("select username,date,starcount,comment from feedback");
    		

    		while(rs.next())
    		{
    			try {
    				DataOutputStream ssdout;
    				
    				ssdout=new DataOutputStream(clientUpdateStreamConnection.getOutputStream());
    				

    				
    				String sendrequest="UP-FB-"+rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getInt(3)+"-"+rs.getString(4);
    				

    	            //send requestcode to server
    	            ssdout.writeUTF(sendrequest);
    	            ssdout.flush();//refresh to make sure it send to the server
    	            System.out.println("feedback sent");
    	            

    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    		con.close();
    	}catch(Exception e)
		{
    		e.printStackTrace();
		}
		
	}
	
	public String logoutrequest()
	{
		try 
		{
			for(int loop=0;loop<Server.Currentclient.size();loop++)
			{
				if(client.getInetAddress().toString().equals(Server.Currentclient.get(loop).getipaddr()))
				{
					Server.Currentclient.remove(loop);
				}
			}
			
			return "RE-LO-OK";
		}catch(Exception e)
		{
			return "RE-LO-NO";
		}
	}
	
	//this method will handle login function
	//login function format is LG-username-password
	//login reply format:
	//if wrong RE-LG-NO
	//if correct RE-LG-YES-namefromdatabase
	public String loginrequest(String codefromclient)
	{
		String[] requestcode = codefromclient.split("-");
		
		String usernamereceived=requestcode[1];
		String passwordreceived=requestcode[2];
		
		String replycode=null;
		boolean useridmatch=false;
		String privillage=null;
		String nameofaccount=null;
		
		try
    	{  
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		
    		Statement stmt=con.createStatement();  
    		ResultSet rs=stmt.executeQuery("select username,password,name,privillage from userid");
    		
    		
    		
    		
    		while(rs.next())
    		{
    			
    			
    			if(usernamereceived.equals(rs.getString(1)) && passwordreceived.equals(rs.getString(2)))
    			{
    				useridmatch=true;
    				nameofaccount=rs.getString(3);
    				privillage=rs.getString(4);
    				
    				for(int loop=0;loop<Server.Currentclient.size();loop++)
    				{
    					if(Server.Currentclient.get(loop).getusername().equals(usernamereceived))
    					{
    						Server.Currentclient.remove(loop);
    					}
    				}
    				//add login session
    				Server.Currentclient.add(new Currentclientinfo(client.getInetAddress().toString(),usernamereceived,nameofaccount,privillage,client));
    				this.clientUpdateStreamConnection = client;
    			}
    			
    		}
    		
    		con.close();
    		


    	}catch(Exception e)
    	{ 
    		System.out.println(e);
    		System.out.print("Opp!Error occured");
    	}
		
		if(useridmatch==true)
		{
			replycode="RE-LG-YES-"+privillage;
		}else
		{
			replycode="RE-LG-NO";
		}
		
		return replycode;

	}
	
	//this method respond for user that send request to check if username or emailaddr is already in database
	public String checkrequest(String codefromclient)
	{
		String[] requestcode = codefromclient.split("-");
		
		String checkinformation=requestcode[1];
		String username=requestcode[2];
		
		String replycode=null;
		
		//create connection to sql
		
    		
    		//here restaurantorderapp is database name, root is username and password 
    		switch(checkinformation)
    		{
    			case "UN":
    				//check if username already used
    				try
    		    	{  
    		    		Class.forName("com.mysql.jdbc.Driver");
    		    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		    		Statement stmt;
    		    		ResultSet rs;
    		    		stmt=con.createStatement();  
    		    		rs=stmt.executeQuery("select username from userid");
    		    		
    		    		boolean useridused=false;
    		    		
    		    		
    		    		while(rs.next())
    		    		{
    		    			if(username.equals(rs.getString(1)))
    		    			{
    		    				useridused=true;
    		    			}		
    		    		}
    		    		con.close();
    		    		
    		    		if(useridused==true)
    		    		{
    		    			replycode="RE-CK-YES";
    		    		}else
    		    		{
    		    			replycode="RE-CK-NO";
    		    		}

    		    	}catch(Exception e)
    		    	{ 
    		    		System.out.println(e);
    		    		System.out.print("Opp!Error occured");
    		    	}
    				break;
    				
    			case "EM":
    				//check if email already used
    				try
    		    	{  
    		    		Class.forName("com.mysql.jdbc.Driver");
    		    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		    		Statement stmt;
    		    		ResultSet rs;
    		    		stmt=con.createStatement();  
    		    		rs=stmt.executeQuery("select emailaddr from userid");
    		    		
    		    		boolean emailused=false;
    		    		
    		    		
    		    		while(rs.next())
    		    		{
    		    			if(username.equals(rs.getString(1)))
    		    			{
    		    				emailused=true;
    		    			}		
    		    		}
    		    		con.close();
    		    		
    		    		if(emailused==true)
    		    		{
    		    			replycode="RE-CK-YES";
    		    		}else
    		    		{
    		    			replycode="RE-CK-NO";
    		    		}

    		    	}catch(Exception e)
    		    	{ 
    		    		System.out.println(e);
    		    		System.out.print("Opp!Error occured");
    		    	}
    				break;
    			

    		}
    		return replycode;

	}
	
	//This method is to handle register account
	//format RG-username-password-name-email-age-gender
	public String registerrequest(String codefromclient)
	{
		String[] requestcode = codefromclient.split("-");
		
		String username=requestcode[1];
		String password=requestcode[2];
		String name=requestcode[3];
		String email=requestcode[4];
		String age=requestcode[5];
		String gender=requestcode[6].toLowerCase();
		
		
		
		
		
		String replycode=null;
		
		
		try
    	{  
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		
    		Statement stmt=con.createStatement();  
    		stmt.executeUpdate("insert into userid(username,password,name,age,emailaddr,gender,privillage) values('"+username+"','"+password+"','"+name+"',"+age+",'"+email+"','"+gender+"','customer');");
    		
    		
   
    		
    		con.close();
    		


    	}catch(Exception e)
    	{ 
    		System.out.println(e);
    		System.out.print("Opp!Error occured");
    	}
		replycode="RE-RG-YES";
		
		
		return replycode;
	}
	
	//This method is to handle get information
	//format GT-information
	public String getinformationrequest(String codefromclient)
	{
		String[] requestcode = codefromclient.split("-");
		
		String informationrequest=requestcode[1];
		
		String replycode=null;
					
		
		switch(informationrequest)
		{
			case "NM":
				String clientname=null;
				for(int loop=0;loop<Server.Currentclient.size();loop++)
				{
					if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
					{
						clientname=Server.Currentclient.get(loop).getname();
					}
				}
				
				replycode="RE-GT-"+clientname;
				
				break;
			case "PR":
				String privillage=null;
				
				for(int loop=0;loop<Server.Currentclient.size();loop++)
				{
					if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
					{
						privillage=Server.Currentclient.get(loop).getprivillage();
					}
				}
				
				replycode="RE-GT-"+privillage;
				
				break;
			case "PW":
				try {
				
					String password=null;
					String username=null;
					for(int loop=0;loop<Server.Currentclient.size();loop++)
					{
						if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
						{
							username=Server.Currentclient.get(loop).getusername();
						}
					}
					
					Class.forName("com.mysql.jdbc.Driver");
			    	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			    	Statement stmt;
			    	ResultSet rs;
			    	stmt=con.createStatement();  
			    	rs=stmt.executeQuery("select password from userid where username='"+username+"'");
			    		
			    	while(rs.next())
			    	{
			    		password=rs.getString(1);
			    	}
			    	
			    	replycode="RE-GT-"+password;
			    	
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("Error while user request get information-> PW");
				}
				break;
			case "EM":
				try {
				
					String email=null;
					String username=null;
					for(int loop=0;loop<Server.Currentclient.size();loop++)
					{
						if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
						{
							username=Server.Currentclient.get(loop).getusername();
						}
					}
					
					Class.forName("com.mysql.jdbc.Driver");
			    	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			    	Statement stmt;
			    	ResultSet rs;
			    	stmt=con.createStatement();  
			    	rs=stmt.executeQuery("select emailaddr from userid where username='"+username+"'");
			    		
			    	while(rs.next())
			    	{
			    		email=rs.getString(1);
			    	}
			    	
			    	replycode="RE-GT-"+email;
			    	
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("Error while user request get information-> PW");
				}
				break;
			case "GD":
				try {
				
					String gender=null;
					String username=null;
					for(int loop=0;loop<Server.Currentclient.size();loop++)
					{
						if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
						{
							username=Server.Currentclient.get(loop).getusername();
						}
					}
					
					Class.forName("com.mysql.jdbc.Driver");
			    	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			    	Statement stmt;
			    	ResultSet rs;
			    	stmt=con.createStatement();  
			    	rs=stmt.executeQuery("select gender from userid where username='"+username+"'");
			    		
			    	while(rs.next())
			    	{
			    		gender=rs.getString(1);
			    	}
			    	
			    	replycode="RE-GT-"+gender;
			    	
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("Error while user request get information-> PW");
				}
				break;
			case "AG":
				try {
				
					int age=0;
					String username=null;
					for(int loop=0;loop<Server.Currentclient.size();loop++)
					{
						if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
						{
							username=Server.Currentclient.get(loop).getusername();
						}
					}
					
					Class.forName("com.mysql.jdbc.Driver");
			    	Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			    	Statement stmt;
			    	ResultSet rs;
			    	stmt=con.createStatement();  
			    	rs=stmt.executeQuery("select age from userid where username='"+username+"'");
			    		
			    	while(rs.next())
			    	{
			    		age=rs.getInt(1);
			    	}
			    	
			    	replycode="RE-GT-"+age;
			    	
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("Error while user request get information-> PW");
				}
				break;
					
		}
		
		//if replycode still null mean cant found the client information on session
		if(replycode==null)
		{
			replycode="RE-sessiontimeout";
		}
		
		return replycode;
		
	}
	
	//this method help to check if client ipaddr is inside currentclientinfo
	public String checksession() 
	{
		boolean sessionavailable=false;
		String privillage=null;
		
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
			{
				//if found session record
				sessionavailable=true;
				privillage=Server.Currentclient.get(loop).getprivillage();
				Server.Currentclient.get(loop).setClientConnection(client);
				clientUpdateStreamConnection = client;
				
			}
		}
		
		if(sessionavailable==true)
		{
			return "RE-SS-YES-"+privillage;
		}else
		{
			return "RE-SS-NO";
		}
		
	}
	
	public String addorder(String codefromclient)
	{
		String[] requestcode=codefromclient.split("-");
		String replycode = null;
		
		int tableno=Integer.parseInt(requestcode[1]);
		boolean tablealreadyinqueue=false;
		
		for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
		{
			if(Server.Currentorderinqueue.get(loop).gettableno()==tableno)
			{
				tablealreadyinqueue=true;
				replycode="RE-OR-NO";
			}
		}
		
		if(tablealreadyinqueue==false)
		{
			DataOutputStream dos;
			//tell client table available can send the foodorder
			try {
				dos= new DataOutputStream(client.getOutputStream());
				String send="RE-OR-OK";
				
				dos.writeUTF(send);
				dos.flush();
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//keep check for any packet sent from client
			
			ArrayList<foodorderdetail> foodorderlist=new ArrayList<foodorderdetail>();
			
			
			
			boolean keeploop=true;
			while(keeploop==true)
			{
				try {
					disorder=new DataInputStream(client.getInputStream());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try
				{
					
					
					
					
					String  requestfromclient=(String)disorder.readUTF();  
					
					
					System.out.println(client.getInetAddress()+": "+requestfromclient);
			    		
					String[] orderfoodcode=requestfromclient.split("-");
					
					if(orderfoodcode[1].equals("END"))
					{
						keeploop=false;
					}else
					{
						String foodname=orderfoodcode[0];
						int quantity=Integer.parseInt(orderfoodcode[1]);
						String remark=orderfoodcode[2];
						
						foodorderlist.add(new foodorderdetail(foodname,quantity,remark));
					}
					
					
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			String username=null;
			//find username from user logged in ip
			for(int loop=0;loop<Server.Currentclient.size();loop++)
			{
				if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
				{
					username=Server.Currentclient.get(loop).getusername();
				}
			}
			
			
			Server.Currentorderinqueue.add(new orderdetail(tableno,username,foodorderlist));
			
			sendfoodcarttoclient();
			updatecurrentorderlisttoclient();
			
			replycode="RE-OR-OK";
		}
		
		return replycode;
	}
	
	public String editorder(String codefromclient)
	{
		String[] requestcode=codefromclient.split("-");
		String foodname=requestcode[1];
		int quantity=Integer.parseInt(requestcode[2]);
		String remark=requestcode[3];
		
		
		String replycode= null;
		
		String clientusername=null;
		
		int clientorderindex=0;
		//find client username with ipaddress
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			if (client.getInetAddress().toString().equals(Server.Currentclient.get(loop).getipaddr()))
			{
				clientusername=Server.Currentclient.get(loop).getusername();
			}
		}
		
		//find client order with username
		for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
		{
			if (clientusername.equals(Server.Currentorderinqueue.get(loop).getcustomerusername()))
			{
				clientorderindex=loop;
			}
		}
		
		if(Server.Currentorderinqueue.get(clientorderindex).getcookcondition()==true)
		{
			replycode="RE-EO-NO";
		}else
		{
			ArrayList<foodorderdetail> foodlist = Server.Currentorderinqueue.get(clientorderindex).getfoodordered();
			
			for(int loop=0;loop<foodlist.size();loop++)
			{
				if(foodname.equals(foodlist.get(loop).getfoodname()))
				{
					foodlist.get(loop).setquantity(quantity);
					foodlist.get(loop).setremark(remark);
					
					Server.Currentorderinqueue.get(clientorderindex).recalculatetotalprice();				
					replycode="RE-EO-OK";
					
					//update client 
					updatecurrentorderlisttoclient();
				}
			}
			
		}
		
		return replycode;
		
	}
	
	public String deletefoodordered(String codefromclient)
	{
		String[] requestcode=codefromclient.split("-");
		String foodname=requestcode[1];
		
		String replycode= null;
		
		String clientusername=null;
		
		int clientorderindex=0;
		//find client username with ipaddress
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			if (client.getInetAddress().toString().equals(Server.Currentclient.get(loop).getipaddr()))
			{
				clientusername=Server.Currentclient.get(loop).getusername();
			}
		}
		
		//find client order with username
		for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
		{
			if (clientusername.equals(Server.Currentorderinqueue.get(loop).getcustomerusername()))
			{
				clientorderindex=loop;
			}
		}
		
		if(Server.Currentorderinqueue.get(clientorderindex).getcookcondition()==true)
		{
			replycode="RE-DO-NO";
		}else
		{
			ArrayList<foodorderdetail> foodlist = Server.Currentorderinqueue.get(clientorderindex).getfoodordered();
			
			for(int loop=0;loop<foodlist.size();loop++)
			{
				if(foodname.equals(foodlist.get(loop).getfoodname()))
				{
					foodlist.remove(loop);
					Server.Currentorderinqueue.get(clientorderindex).recalculatetotalprice();
					
					replycode="RE-DO-OK";
					
					//update client 
					updatecurrentorderlisttoclient();
				}
			}
			
		}
		
		

		
		return replycode;
		
	}
	
	public String cancelorder()
	{
		String clientusername=null;
		boolean clientordercookcondition=true;
		int clientorderindex=0;
		
		//find client username
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
			{
				clientusername=Server.Currentclient.get(loop).getusername();
			}
		}
		
		//find user order with client's username
		for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
		{
			if(clientusername.equals(Server.Currentorderinqueue.get(loop).getcustomerusername()))
			{
				clientordercookcondition=Server.Currentorderinqueue.get(loop).getcookcondition();
				clientorderindex=loop;
			}
		}
		
		if (clientordercookcondition==false)
		{
			Server.Currentorderinqueue.remove(clientorderindex);
			
			//update client
			updatecurrentorderlisttoclient();
			
			return "RE-CO-OK";
		}else
		{
			return "RE-CO-NO";
		}
	}
	
	public String paymentrequest()
	{
		try {
			String clientusername=null;
			int clientorderindex=0;
			boolean alreadyrequestforpayment=false;
			
			//find client username using ipaddress
			for(int loop=0;loop<Server.Currentclient.size();loop++)
			{
				if(client.getInetAddress().toString().equals(Server.Currentclient.get(loop).getipaddr()))
				{
					clientusername=Server.Currentclient.get(loop).getusername();
				}
			}
			
			//find index of client order inside an arraylist
			for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
			{
				if(clientusername.equals(Server.Currentorderinqueue.get(loop).getcustomerusername()))
				{
					clientorderindex=loop;
				}
			}
			
			for(int loop=0;loop<Server.Currentpaymentrequest.size();loop++)
			{
				if(Server.Currentpaymentrequest.get(loop).getclientusername().equals(clientusername))
				{
					alreadyrequestforpayment=true;
				}
			}
			
			if(alreadyrequestforpayment==false)
			{
				Server.Currentpaymentrequest.add(new currentpaymentrequest(Server.Currentorderinqueue.get(clientorderindex).getcustomerusername()
																			,Server.Currentorderinqueue.get(clientorderindex).gettableno()));
				
			}
			
			//update client
			updatecurrentpaymentrequesttoclient();
			
			return "RE-PO-OK";
			
			
			
		}catch(Exception e)
		{
			return "RE-PO-NO";
		}
	}
	
	/*customer send feedback to us after paying
	 * feedback request format: FB-starcount-comment
	 * feedback request reply code
	 * if feedback recorded successfully in database RE-FB-OK
	 * if error occured                              RE-FB-NO
	 */
	public String recordfeedback(String codefromclient)
	{
		try {
			String[] requestcode=codefromclient.split("-");
				
			int starcount=Integer.parseInt(requestcode[1]);
			String comment=requestcode[2];
			String username=null;
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			String datenow=dateFormat.format(date);
			
			
			for(int loop=0;loop<Server.Currentclient.size();loop++)
			{
				if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
				{
					username=Server.Currentclient.get(loop).getusername();
				}
			}
			
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		Statement stmt;
   		
    		stmt=con.createStatement();
    		stmt.executeUpdate("insert into feedback values('"+username+"',"+starcount+",'"+comment+"','"+datenow+"');");
    		
    		con.close();
    		
    		
    		//update client
			updatefeedbacklisttoclient();
				
			return "RE-FB-OK";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "RE-FB-NO";
		}
	}

	
	public String paymentdone(String codefromclient)
	{
		try {
			String[] requestcode=codefromclient.split("-");
			
			int tableno=Integer.parseInt(requestcode[1]);
			
			for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
			{
				if(Server.Currentorderinqueue.get(loop).gettableno()==tableno)
				{
					Class.forName("com.mysql.jdbc.Driver");
		    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
		    		//here restaurantorderapp is database name, root is username and password 
		    		Statement stmt;
		    		ResultSet rs;
		    		
		    		stmt=con.createStatement();
		    		//find how much order had done
		    		//because order id start from 1 if there are 5 order then the new orderid will be 6
		    		rs=stmt.executeQuery("SELECT COUNT(*) FROM orderhistory");
		    		rs.next();
		    	    int rowcount = rs.getInt(1);
		    	    
		    	    int neworderid= rowcount+1;
		    	    double totalpayment=Server.Currentorderinqueue.get(loop).gettotalpayment();
		    	    String customerusername=Server.Currentorderinqueue.get(loop).getcustomerusername();
		    	    String dateordered=Server.Currentorderinqueue.get(loop).getdateorder();
		    	    
		    		ArrayList<foodorderdetail> foodordered=Server.Currentorderinqueue.get(loop).getfoodordered();
		    		
		    		
		    		stmt=con.createStatement();

		    		stmt.executeUpdate("insert into orderhistory values("+neworderid+","+totalpayment+",'"+customerusername+"','"+dateordered+"');");
		    		
		    		for(int foodloop=0;foodloop<foodordered.size();foodloop++)
		    		{
		    			stmt=con.createStatement();
		    			stmt.executeUpdate("insert into orderedfoodhistory values('"+foodordered.get(foodloop).getfoodname()+"',"+neworderid+","+foodordered.get(foodloop).getquantity()+","+foodordered.get(foodloop).gettotalprice()+");");
		    		}
		    		
		    		
		    		String custusername=Server.Currentorderinqueue.get(loop).getcustomerusername();
		    		//clear it cause already insert to database
		    		Server.Currentorderinqueue.remove(loop);
		    		
		    		for(int paymentrequestloop=0;paymentrequestloop<Server.Currentpaymentrequest.size();paymentrequestloop++)
		    		{
		    			if(Server.Currentpaymentrequest.get(paymentrequestloop).gettableno()==tableno)
		    			{
		    				Server.Currentpaymentrequest.remove(paymentrequestloop);
		    			}
		    		}
		    		
		    		
		    		//update information to client
		    		updatecurrentorderlisttoclient();
		    		updateorderhistorytoclient();
		    		updatecurrentpaymentrequesttoclient();
		    		
		    		tellclientpaymentdone(custusername);
		    		
		    		con.close();
				}
			}
			return "RE-PD-OK";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "RE-PD-OK";
		}

	}
	
	public String paymentnotdone(String codefromclient)
	{
		try {
			String[] requestcode=codefromclient.split("-");
			
			int tableno=Integer.parseInt(requestcode[1]);
			
			for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
			{
				if(Server.Currentorderinqueue.get(loop).gettableno()==tableno)
				{
					Class.forName("com.mysql.jdbc.Driver");
		    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
		    		//here restaurantorderapp is database name, root is username and password 
		    		Statement stmt;
		    		ResultSet rs;
		    		
		    		stmt=con.createStatement();
		    		//find how much order had done
		    		//because order id start from 1 if there are 5 order then the new orderid will be 6
		    		rs=stmt.executeQuery("SELECT COUNT(*) FROM notpayorderhistory");
		    		rs.next();
		    	    int rowcount = rs.getInt(1);
		    	    
		    	    int neworderid= rowcount+1;
		    	    double totalpayment=Server.Currentorderinqueue.get(loop).gettotalpayment();
		    	    String customerusername=Server.Currentorderinqueue.get(loop).getcustomerusername();
		    	    String dateordered=Server.Currentorderinqueue.get(loop).getdateorder();
		    	    
		    		ArrayList<foodorderdetail> foodordered=Server.Currentorderinqueue.get(loop).getfoodordered();
		    		
		    		
		    		stmt=con.createStatement();

		    		stmt.executeUpdate("insert into notpayorderhistory values("+neworderid+","+totalpayment+",'"+customerusername+"','"+dateordered+"');");
		    		
		    		for(int foodloop=0;foodloop<foodordered.size();foodloop++)
		    		{
		    			stmt=con.createStatement();
		    			stmt.executeUpdate("insert into notpayorderedfoodhistory values('"+foodordered.get(foodloop).getfoodname()+"',"+neworderid+","+foodordered.get(foodloop).getquantity()+","+foodordered.get(foodloop).gettotalprice()+");");
		    		}
		    		
		    		con.close();
		    		
		    		//clear the order because already record in database
		    		Server.Currentorderinqueue.remove(loop);
		    		
		    		for(int paymentrequestloop=0;paymentrequestloop<Server.Currentpaymentrequest.size();paymentrequestloop++)
		    		{
		    			if(Server.Currentpaymentrequest.get(paymentrequestloop).gettableno()==tableno)
		    			{
		    				Server.Currentpaymentrequest.remove(paymentrequestloop);
		    			}
		    		}
		    		
		    		//update information to client
		    		updatecurrentorderlisttoclient();
		    		updateordernotpaytoclient();
		    		updatecurrentpaymentrequesttoclient();
		    		
				}
			}
			return "RE-PN-OK";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "RE-PN-NO";
		}
	}
	
	public String ordernotpaiddone(String codefromclient)
	{
		try {
			String[] requestcode=codefromclient.split("-");
			
			String username=requestcode[1];
			String date=requestcode[2];
			
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		Statement stmt;
    		ResultSet rs;
    		
    		stmt=con.createStatement();
    	    
    		rs=stmt.executeQuery("SELECT orderid,totalpayment,custusername,date FROM notpayorderhistory where custusername='"+username+"' and date='"+date+"'");
    		rs.next();
    		
    		int orderid=rs.getInt(1);
    		double totalpayment=rs.getDouble(2);
    		
    		stmt=con.createStatement();
    		rs=stmt.executeQuery("SELECT COUNT(*) FROM orderhistory");
    		rs.next();
    	    int rowcount = rs.getInt(1);
    	    int neworderid=rowcount+1;
    	    
    	    stmt=con.createStatement();

    		stmt.executeUpdate("insert into orderhistory values("+neworderid+","+totalpayment+",'"+username+"','"+date+"');");
    	    
    		stmt=con.createStatement();
    		rs=stmt.executeQuery("SELECT foodname,quantity,totalprice FROM notpayorderedfoodhistory where orderid="+orderid);
    		
    		while(rs.next())
    		{
    			Statement upstmt=con.createStatement();
    			upstmt.executeUpdate("insert into orderedfoodhistory values('"+rs.getString(1)+"',"+neworderid+","+rs.getInt(2)+","+rs.getDouble(3)+");");

    		}
    		
    		stmt=con.createStatement();
    		stmt.executeUpdate("DELETE FROM notpayorderhistory where orderid="+orderid);
    		
    		stmt=con.createStatement();
    		stmt.executeUpdate("DELETE FROM notpayorderedfoodhistory where orderid="+orderid);
    		
    		con.close();
    		
    		//update client
    		updateordernotpaytoclient();		
			
			return "RE-OP-OK";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "RE-OP-NO";
		}
	}
	
	public String setfoodavailability(String codefromclient)
	{
		try {
			String[] requestcode=codefromclient.split("-");
			
			String foodname=requestcode[1];
			String availability=requestcode[2];
			
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		Statement stmt;
    		ResultSet rs;
    		
    		stmt=con.createStatement();    	    
    		
    		stmt.executeUpdate("UPDATE foodinfo SET availableornot='"+availability.charAt(0)+"' WHERE foodname='"+foodname+"';");
    		
    		con.close();
    		
    		//update client
    		updatefoodlisttoclient();
    		
    		
			return "RE-FA-OK";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "RE-FA-NO";
		}
	}
	
	public String cookstart(String codefromclient)
	{
		
		try {
			String[] requestcode=codefromclient.split("-");
			
			int tableno=Integer.parseInt(requestcode[1]);
			
			for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
			{
				if(tableno==Server.Currentorderinqueue.get(loop).gettableno())
				{
					if(Server.Currentorderinqueue.get(loop).getcookcondition()==false)
					{
						Server.Currentorderinqueue.get(loop).setcookcondition(true);
						updatecurrentorderlisttoclient();
						tellclientcookstart(Server.Currentorderinqueue.get(loop).getcustomerusername(),Server.Currentorderinqueue.get(loop).getcookstarttime());
					}
					
					
				}
			}
			
			
    		
			
    		
			return "RE-CS-OK";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "RE-CS-NO";
		}
	}
	
	public String cookdone(String codefromclient)
	{
		try {
			String[] requestcode=codefromclient.split("-");
			
			int tableno=Integer.parseInt(requestcode[1]);
			
			for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
			{
				if(tableno==Server.Currentorderinqueue.get(loop).gettableno())
				{
					Server.Currentorderinqueue.get(loop).setcookdone(true);
					//update client
				    updatecurrentorderlisttoclient();
					tellclientcookdone(Server.Currentorderinqueue.get(loop).getcustomerusername());
				}
			}

			return "RE-CD-OK";
		}catch(Exception e)
		{
			e.printStackTrace();
			return "RE-CD-NO";
		}
	}
	
	public String addfoodmenu(String codefromclient)
	{
		try {
			String[] requestcode=codefromclient.split("-");
			
			String foodname=requestcode[1];
			String fooddesc=requestcode[2];
			double foodprice=Double.parseDouble(requestcode[3]);
			int foodspicy=Integer.parseInt(requestcode[4]);
			int containmeat=Integer.parseInt(requestcode[5]);
			int foodsalty=Integer.parseInt(requestcode[6]);
			int foodsweetness=Integer.parseInt(requestcode[7]);
			int foodsour=Integer.parseInt(requestcode[8]);
			int chefsuggest=Integer.parseInt(requestcode[9]);
			int preparetime=Integer.parseInt(requestcode[10]);
			
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		Statement stmt;
    		ResultSet rs;
    		
    		stmt=con.createStatement();    	   
    		stmt.executeUpdate("INSERT into foodinfo values('"+foodname+"','"+fooddesc+"',"+foodprice+","+foodspicy+","+containmeat+","+foodsalty+","+foodsweetness+","+foodsour+","+chefsuggest+","+preparetime+",'Y');");
    		
    		con.close();
    		
    		//update client
    		updatefoodlisttoclient();
    		
    		return "RE-AF-OK";
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Edit food menu got an error!!");
			return "RE-AF-NO";
		}
	}
	
	
	public String editfoodmenu(String codefromclient)
	{
		//edit food menu request code: EF-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweetness-foodsour-chefsuggest-preparetime
		try {
			String[] requestcode=codefromclient.split("-");
			
			String foodname=requestcode[1];
			String fooddesc=requestcode[2];
			double foodprice=Double.parseDouble(requestcode[3]);
			int foodspicy=Integer.parseInt(requestcode[4]);
			int containmeat=Integer.parseInt(requestcode[5]);
			int foodsalty=Integer.parseInt(requestcode[6]);
			int foodsweetness=Integer.parseInt(requestcode[7]);
			int foodsour=Integer.parseInt(requestcode[8]);
			int chefsuggest=Integer.parseInt(requestcode[9]);
			int preparetime=Integer.parseInt(requestcode[10]);
			
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		Statement stmt;
    		ResultSet rs;
    		
    		stmt=con.createStatement();    	   
    		stmt.executeUpdate("UPDATE foodinfo SET fooddesc='"+fooddesc+"' WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET foodprice="+foodprice+" WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET foodspicy="+foodspicy+" WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET containmeat="+containmeat+" WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET foodsalty="+foodsalty+" WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET foodsweetness="+foodsweetness+" WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET foodsour="+foodsour+" WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET chefsuggest="+chefsuggest+" WHERE foodname='"+foodname+"';");
    		
    		stmt=con.createStatement();    	    
    		stmt.executeUpdate("UPDATE foodinfo SET preparetime="+preparetime+" WHERE foodname='"+foodname+"';");
    		
    		con.close();
    		
    		//update client
    		updatefoodlisttoclient();
    		
    		return "RE-EF-OK";
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Edit food menu got an error!!");
			return "RE-EF-NO";
		}
	}
	
	public String deletefoodmenu(String codefromclient)
	{
		//edit food menu request code: EF-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweetness-foodsour-chefsuggest-preparetime
		try {
			String[] requestcode=codefromclient.split("-");
			
			String foodname=requestcode[1];
			
			Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		//here restaurantorderapp is database name, root is username and password 
    		Statement stmt;
    		ResultSet rs;
    		
    		stmt=con.createStatement();    	   
    		stmt.executeUpdate("DELETE FROM foodinfo WHERE foodname='"+foodname+"';");
    		
    		con.close();
    		
    		//update client
    		updatefoodlisttoclient();
    		
    		return "RE-DF-OK";
			
			
		}catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Delete food menu got an error!!");
			return "RE-DF-NO";
		}
	}
	
	 
	 public String editprofile(String codefromclient)
	 {
		 /*Edit Profile
		  * Edit user profile in database
		  * Edit profile format is PF-name-password-age-email-gender
		  * Edit Profile reply format:
		  * if successful edited in database RE-PF-OK
		  * if error occured                 RE-PF-NO
		  */
		 try {
			 String[] requestcode=codefromclient.split("-");
			 
			 String username=null;
			 String name=requestcode[1];
			 String password=requestcode[2];
			 int age=Integer.parseInt(requestcode[3]);
			 String gender=requestcode[4];
			 
			 for(int loop=0;loop<Server.Currentclient.size();loop++)
			 {
				 if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
				 {
					 username=Server.Currentclient.get(loop).getusername();
				 }
			 }
				
				
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
			 //here restaurantorderapp is database name, root is username and password 
			 Statement stmt;
			 ResultSet rs;
			 
			 stmt=con.createStatement();    	   
			 stmt.executeUpdate("UPDATE userid SET name='"+name+"',password='"+password+"',age="+age+",gender='"+gender+"' WHERE username='"+username+"'");
			 
			 con.close();
			 
			 for(int loop=0;loop<Server.Currentclient.size();loop++)
			 {
				 if(Server.Currentclient.get(loop).getipaddr().equals(client.getInetAddress().toString()))
				 {
					 Server.Currentclient.get(loop).setname(name);
				 }
			 }
			 
			 
			 return "RE-PF-OK";
				
				
		 }catch(Exception e)
		 {
			 e.printStackTrace();
			 System.out.println("Delete food menu got an error!!");
			 return "RE-PF-NO";
		 }
	 }
	 
	
	 public void updatefoodlisttoclient()
	 {
		 /*
		 the code format is : MN-start
	     Then the server will send:   MN-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty
	                                     -foodsweetness-foodsour-chefsugges-preparetime-availableornot
	     and send again until the server send MN-END //indenticate no more foodmenu item to send
	     */
		 String clientipaddr;
		 
		//update currentorder to employee privilage
		 for(int clientloop=0;clientloop<Server.Currentclient.size();clientloop++)
		 {
			 clientipaddr=Server.Currentclient.get(clientloop).getipaddr();
			 Socket clientConnection=Server.Currentclient.get(clientloop).getClientConnection();
				
			 StringBuilder ip = new StringBuilder(clientipaddr);
						
			 ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
						
			 clientipaddr=ip.toString();
			 
			 try {
				 DataOutputStream ssdout;
				 
				 
				 ssdout=new DataOutputStream(clientConnection.getOutputStream());
				 
				 String sendrequest="MN-start";
				 
				 ssdout.writeUTF(sendrequest);
				 ssdout.flush();//refresh to make sure it send to the server
				 
				 Class.forName("com.mysql.jdbc.Driver");
				 
				 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
				 Statement stmt;
				 ResultSet rs;
				 stmt=con.createStatement();  
				 rs=stmt.executeQuery("select * from foodinfo");
		    		
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
				 char availableornot;
					
				 while(rs.next())
				 {
					 foodname=rs.getString(1);
					 fooddesc=rs.getString(2);
					 foodprice=rs.getDouble(3);
					 foodspicy=rs.getInt(4);
					 containmeat=rs.getInt(5);
					 foodsalty=rs.getInt(6);
					 foodsweetness=rs.getInt(7);
					 foodsour=rs.getInt(8);
					 chefsuggest=rs.getInt(9);
					 preparetime=rs.getInt(10);
					 availableornot=rs.getString(11).charAt(0);
		    			
					//format to update client foodmenu : UP-MN-foodname-fooddesc-foodprice-foodspicy-containmeat-foodsalty-foodsweerbess-foodsour-chefsugges-preparetime-availableornot
					sendrequest="MN-"+foodname+"-"+fooddesc+"-"+foodprice+"-" +foodspicy
							 +"-"+containmeat+"-"+foodsalty+"-"+foodsweetness+"-"+foodsour
							 +"-"+chefsuggest+"-"+preparetime+"-"+availableornot;
			         

			            //send requestcode to server
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();//refresh to make sure it send to the server
					 
				 }
				 con.close();
				 
				 sendrequest="MN-END";
				 
				 ssdout.writeUTF(sendrequest);
				 ssdout.flush();//refresh to make sure it send to the server
				
				 
			 }catch(Exception e)
			 {
				 System.out.println("Could not connect to : "+clientipaddr+" When sending foodlist!");
			 }
			 	
		 }
		 
		 
		 
	 }
	 
	 
	 public void updatecurrentorderlisttoclient()
	 {
		 
		 String clientipaddr;
		//update currentorder to employee privilage
		 for(int clientloop=0;clientloop<Server.Currentclient.size();clientloop++)
		 {
			 if(Server.Currentclient.get(clientloop).getprivillage().equals("employee"))
			 {
				 clientipaddr=Server.Currentclient.get(clientloop).getipaddr();
				 Socket clientConenction=Server.Currentclient.get(clientloop).getClientConnection();
					
				 StringBuilder ip = new StringBuilder(clientipaddr);
							
				 ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
							
				 clientipaddr=ip.toString();
				 
				 try {
					 DataOutputStream ssdout;
					
					 ssdout=new DataOutputStream(clientConenction.getOutputStream());
					 
					 String sendrequest="OL-start";
					 
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();//refresh to make sure it send to the server
					 
					 for(int loop=0;loop<Server.Currentorderinqueue.size();loop++)
					 {
						 int tableno=Server.Currentorderinqueue.get(loop).gettableno();
						 String cookcondition="NO";
						 String ordertime=Server.Currentorderinqueue.get(loop).getdateorder();
						 
						 if(Server.Currentorderinqueue.get(loop).getcookdone()==true)
						 {
							 cookcondition="DONE";
						 }else if(Server.Currentorderinqueue.get(loop).getcookcondition()==true)
						 {
							 cookcondition="YES";
						 }
						 
						 sendrequest="OL-"+tableno+"-"+cookcondition+"-"+ordertime;
						 
						 System.out.println(sendrequest);
						 
						 ssdout.writeUTF(sendrequest);
						 ssdout.flush();//refresh to make sure it send to the server
						 
						 for(int foodorderloop=0;foodorderloop<Server.Currentorderinqueue.get(loop).getfoodordered().size();foodorderloop++)
						 {
							 String foodname;
							 int quantity;
							 String remark;
							 
							 foodname=Server.Currentorderinqueue.get(loop).getfoodordered().get(foodorderloop).getfoodname();
							 quantity=Server.Currentorderinqueue.get(loop).getfoodordered().get(foodorderloop).getquantity();
							 remark=Server.Currentorderinqueue.get(loop).getfoodordered().get(foodorderloop).getremark();
									 
							 sendrequest="FL-"+foodname+"-"+quantity+"-"+remark;
							 ssdout.writeUTF(sendrequest);
							 ssdout.flush();
						 }
						 sendrequest="FL-END";
						 ssdout.writeUTF(sendrequest);

					 }
					 
					 sendrequest="OL-END";
					 ssdout.writeUTF(sendrequest);
					 
				 }catch(Exception e)
				 {
					 
				 }
			 }
			 	
		 }
		 
	 }
	 
	 
	 public void updateordernotpaytoclient()
	 {
		 /*
	        if server want to update the ordernotpaylist
	        the code format is: ON-start
	        Then the server will send ON-customerusername-date-totalpayment
	        Then FL-foodname-quantity-totalprice until FL-END
	        then send again until no more ordernotpay and send ON-END
	     */
		 
		 //send only if user privillage is employee
		 for(int clientloop=0;clientloop<Server.Currentclient.size();clientloop++)
		 {
			 if(Server.Currentclient.get(clientloop).getprivillage().equals("employee"))
			 {
				 String clientipaddr=Server.Currentclient.get(clientloop).getipaddr();
				 Socket clientConnection=Server.Currentclient.get(clientloop).getClientConnection();
				 
				 StringBuilder ip = new StringBuilder(clientipaddr);
				 
				 ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
				 
				 clientipaddr=ip.toString();
				 
				 
					
				 try
				 {  
					 DataOutputStream ssdout;		
					 
					 ssdout=new DataOutputStream(clientConnection.getOutputStream());
					 
					 String sendrequest="ON-start";
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();//refresh to make sure it send to the server
					 
					 
					 Class.forName("com.mysql.jdbc.Driver");
					 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
					 Statement stmt;
					 ResultSet rs;
					 stmt=con.createStatement();  
					 rs=stmt.executeQuery("select custusername,date,totalpayment,orderid from notpayorderhistory");
			    		
					 
					 while(rs.next())
					 {
						 sendrequest="ON-"+rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getDouble(3);
						 
						 
						 //send requestcode to server
						 ssdout.writeUTF(sendrequest);
						 ssdout.flush();//refresh to make sure it send to the server
						 System.out.println("order sent");
						 
						 Statement foodstmt=con.createStatement();  
						 ResultSet foodrs=foodstmt.executeQuery("select foodname,quantity,totalprice from notpayorderedfoodhistory where orderid="+rs.getString(4));
						 
						 while(foodrs.next())
						 {
							 sendrequest="FL-"+foodrs.getString(1)+"-"+foodrs.getInt(2)+"-"+foodrs.getDouble(3);
							 ssdout.writeUTF(sendrequest);
							 ssdout.flush();//refresh to make sure it send to the server
						 }
						 sendrequest="FL-END";
						 ssdout.writeUTF(sendrequest);
						 ssdout.flush();//refresh to make sure it send to the server
					 }
					 
					 sendrequest="ON-END";
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();//refresh to make sure it send to the server
					 
					 
					 con.close();
				 }catch(Exception e)
				 {
					 e.printStackTrace();
				 }
			 }
		 }		
	 }
	 
	  
	 public void updatecurrentpaymentrequesttoclient()
	 {
		 /*
	        if server want to update current payment request
	        the code format is : PR-start
	        The the server will send: PR-tableno-username
	        and then send again until no more current payment request and send PR-END
	      */
		
		 
		 //send only if user privillage is employee
		 for(int clientloop=0;clientloop<Server.Currentclient.size();clientloop++)
		 {
			 if(Server.Currentclient.get(clientloop).getprivillage().equals("employee"))
			 {
				 try {
					 String clientipaddr=Server.Currentclient.get(clientloop).getipaddr();
					 Socket clientConnection=Server.Currentclient.get(clientloop).getClientConnection();
					 
					 StringBuilder ip = new StringBuilder(clientipaddr);
					 
					 ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
						
					 clientipaddr=ip.toString();
					 
					 DataOutputStream ssdout;
					 
					 ssdout=new DataOutputStream(clientConnection.getOutputStream());
					 
					 String sendrequest="PR-start";
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();//refresh to make sure it send to the server
					 
					 //code to send payment request to client
					 for(int loop=0;loop<Server.Currentpaymentrequest.size();loop++)
					 {
						 sendrequest="PR-"+Server.Currentpaymentrequest.get(loop).gettableno()+"-"+Server.Currentpaymentrequest.get(loop).getclientusername();
							
						 
						 //send requestcode to server
						 ssdout.writeUTF(sendrequest);
						 ssdout.flush();//refresh to make sure it send to the server
					 }
					 sendrequest="PR-END";
					 ssdout.writeUTF(sendrequest);
					 ssdout.flush();//refresh to make sure it send to the server
					 
				 }catch(Exception e)
				 {
					 
				 }
				 
			 }
		 }		
	 }
	
	 
	 public void updateorderhistorytoclient()
	 {
		 //send only if user privillage is admin
		 for(int clientloop=0;clientloop<Server.Currentclient.size();clientloop++)
		 {
			 if(Server.Currentclient.get(clientloop).getprivillage().equals("admin"))
			 {
				 try
				 {
					 String clientipaddr=Server.Currentclient.get(clientloop).getipaddr();//get client ipaddress with socket
					 Socket clientConnection=Server.Currentclient.get(clientloop).getClientConnection();
					 
					 StringBuilder ip = new StringBuilder(clientipaddr);
					 
					 ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
						
					 clientipaddr=ip.toString();
					 	
					 Class.forName("com.mysql.jdbc.Driver");
					 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
					 //here restaurantorderapp is database name, root is username and password 
						
					 Statement stmt=con.createStatement();  
					 ResultSet rs=stmt.executeQuery("select orderid,totalpayment,date from orderhistory");
						
						
					 
					 while(rs.next())
					 {
						 DataOutputStream ssdout;
				    	
						
						 ssdout=new DataOutputStream(clientConnection.getOutputStream());
							
							
						 String sendrequest="UP-OR-"+rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getString(3);
							
						 //send requestcode to server
						 ssdout.writeUTF(sendrequest);
						 ssdout.flush();//refresh to make sure it send to the server
				            
						 Statement foodstmt=con.createStatement();
						 ResultSet foodrs=foodstmt.executeQuery("select foodname,quantity from orderedfoodhistory where orderid="+rs.getString(1));
							
						 while(foodrs.next())
						 {
							 sendrequest="FL-"+foodrs.getString(1)+"-"+foodrs.getString(2);
								
							 //send requestcode to server
							 ssdout.writeUTF(sendrequest);
							 ssdout.flush();//refresh to make sure it send to the server
						 }
				            
						 sendrequest="FL-END";
							
						 //send requestcode to server
						 ssdout.writeUTF(sendrequest);
						 ssdout.flush();//refresh to make sure it send to the server

							
					 }
				 }catch(Exception e)
				 {
					 e.printStackTrace();
					 System.out.println("Error while sending order history to client");
				 }
			 }
		 }		
	 }
	 
	 public void updatefeedbacklisttoclient()
	 {
		 //send only if user privillage is admin
		 for(int clientloop=0;clientloop<Server.Currentclient.size();clientloop++)
		 {
			 if(Server.Currentclient.get(clientloop).getprivillage().equals("admin"))
			 {
				 String clientipaddr=Server.Currentclient.get(clientloop).getipaddr();
				 Socket clientConnection=Server.Currentclient.get(clientloop).getClientConnection();
					
				 StringBuilder ip = new StringBuilder(clientipaddr);
					
				 ip.deleteCharAt(0);// delete the / because InetAddress will return /192.168.0.100
				 
				 clientipaddr=ip.toString();
				 
				 try
				 {  
					 Class.forName("com.mysql.jdbc.Driver");
					 Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
					 Statement stmt;
					 ResultSet rs;
					 stmt=con.createStatement();  
					 rs=stmt.executeQuery("select username,date,starcount,comment from feedback");
			    				

					 while(rs.next())
					 {
						 try {
							 DataOutputStream ssdout;
							 	
							 ssdout=new DataOutputStream(clientConnection.getOutputStream());
			    				

			    				
							 String sendrequest="UP-FB-"+rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getInt(3)+"-"+rs.getString(4);
			    				

							 //send requestcode to server
							 ssdout.writeUTF(sendrequest);
							 ssdout.flush();//refresh to make sure it send to the server
							 System.out.println("feedback sent");
			    	            
							 
						 } catch (Exception e) {
							 // TODO Auto-generated catch block
							 e.printStackTrace();
						 }
					 }
					 con.close();
				 }catch(Exception e)
				 {
					 e.printStackTrace();
				 }
			 }
		 }	
	 }
	 
	 
		
	
	
}
