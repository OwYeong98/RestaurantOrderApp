package servercomponent;
//this class handle current logged in user

import java.net.*;
import java.util.Date;
import java.io.*; 

public class Currentclientinfo {
	
	private String ipaddr;
	private String username;
	private String privillage;
	private String name;
	private Date lastactive;
	private Socket clientConnection;

	
	
	public Currentclientinfo(String ipaddr,String username,String name,String privillage,Socket clientConnection)
	{
		this.username=username;
		this.ipaddr=ipaddr;
		this.privillage=privillage;
		this.name=name;
		this.clientConnection = clientConnection;
		lastactive=new Date();//set last active to Date now

	}
	
	public void setname(String name)
	{
		this.name=name;
	}
	
	
	public Date getlastactive()
	{
		return lastactive;
	}
	
	
	public String getipaddr() 
	{
		return ipaddr.toString();
	}
	
	public String getusername()
	{
		return username;
	}
	
	public String getname()
	{
		return name;
	}
	
	public String getprivillage() 
	{
		return privillage;
	}
	
	public void updateDate()
	{
		lastactive=new Date();
	}
	
	public Socket getClientConnection() {
		return clientConnection;
	}

	public void setClientConnection(Socket clientConnection) {
		this.clientConnection = clientConnection;
	}

	

	
	
	
	
	

}
