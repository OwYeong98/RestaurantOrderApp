package orderdatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class foodorderdetail 
{
	private String foodname;
	private int quantity;
	private double totalprice;
	private String remark;

	public foodorderdetail(String foodname,int quantity,String remark)
	{
		this.foodname=foodname;
		this.quantity=quantity;
		this.remark=remark;
		
		try
    	{  
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		Statement stmt;
    		ResultSet rs;
    		stmt=con.createStatement();  
    		rs=stmt.executeQuery("select foodprice,preparetime from foodinfo where foodname='"+foodname+"'");
    		
    		
    		while(rs.next()) {
    			totalprice=rs.getDouble(1) * this.quantity;
    		}
    		
    		
    		con.close();
    	}catch(Exception e)
    	{ 
    		System.out.println(e);
    		System.out.print("Opp!Error occured");
    	}
	}
	
	public String getfoodname()
	{
		return foodname;
	}
	
	public int getquantity()
	{
		return quantity;
	}
	
	public Double gettotalprice()
	{
		return totalprice;
	}
	
	public String getremark()
	{
		return remark;
	}
	
	public void setquantity(int quantity)
	{
		this.quantity=quantity;
		
		try
    	{  
    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/restaurantorderapp","root","");  
    		Statement stmt;
    		ResultSet rs;
    		stmt=con.createStatement();  
    		rs=stmt.executeQuery("select foodprice from foodinfo where foodname='"+foodname+"'");
    		
    		while(rs.next()) {
    			totalprice=rs.getDouble(1) * quantity;
    		}
    		
    		con.close();
    	}catch(Exception e)
    	{ 
    		System.out.println(e);
    		e.printStackTrace();
    		System.out.print("Opp!sql Error occured at class foodorderdetail");
    	}
	}
	
	public void setremark(String remark)
	{
		this.remark=remark;
	}
	

}
