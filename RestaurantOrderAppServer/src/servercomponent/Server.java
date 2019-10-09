package servercomponent;
import java.io.*;  
import java.sql.*;  
import java.net.*;
import java.util.Scanner;  
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import orderdatabase.currentpaymentrequest;
import orderdatabase.orderdetail;

public class Server 
{
	//this arraylist is to store client that already logged in so that when they connect 
	//with the same ip they will be logged in if they dint logout and stay for 30min after inactive
	public static ArrayList<Currentclientinfo> Currentclient=new ArrayList<Currentclientinfo>();//store logged in client to check session
	public static ArrayList<currentpaymentrequest> Currentpaymentrequest= new ArrayList<currentpaymentrequest>(); //store current payment request
	public static ArrayList<orderdetail> Currentorderinqueue= new ArrayList<orderdetail>(); //store current order

	
	private static ServerSocket ss;
	public static Thread checktimeoutthread;
	private static boolean keeploop;
	public static boolean serverstarted=false;
	
	public static void startserver()
	{  
		serverstarted=true;
    	Scanner input = new Scanner(System.in);
    	
    	//for check user id timeout
    	
    	checkuserlogintimeout cktimeout=new checkuserlogintimeout();
    	checktimeoutthread=new Thread(cktimeout);
    	
    	checktimeoutthread.start();
    	
    	try
    	{
    		//create a serversocket that listen to port 11000
    		ss=new ServerSocket(11000);
    	} catch (Exception e) 
    	{
    	    System.out.println("port error");
    	}
    	keeploop=true;
    	
    	while(keeploop==true)
    	{
    	    handleclient client;
 
    	    try
    	    {
    	    	Socket clientrequest=ss.accept();
    	    	
    	    	//server.accept returns a client connection
    	    	//if server accept a client check if the client already thread running
    	    	
    	    	client=new handleclient(clientrequest);
    	    	
    	    		
        	    Thread t = new Thread(client);
        	    t.start();
        	    

    	    	
    	    } catch (IOException e) 
    	    {
    	      System.out.println("Server successfully closed!");
    	      
    	    }
    	} 
    } 
	
	public static void stopserver()
	{
		try {
			keeploop=false;
			
			ss.close();
			serverstarted=false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
