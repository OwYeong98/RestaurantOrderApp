package orderdatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

//this class store the orderdetail
public class orderdetail 
{
	private int tableno;
	private double totalpayment;
	private final String customerusername;
	private Date date;
	private String dateorder;
	private ArrayList<foodorderdetail> foodordered = new ArrayList<foodorderdetail>();
	private String cookstarttime=null;
	private boolean cookcondition;
	private boolean cookdone;
	
	
	public orderdetail(int tableno,String customerusername, ArrayList<foodorderdetail> foodordered)
	{
		this.tableno=tableno;
		this.customerusername=customerusername;
		this.foodordered=foodordered;
		
		totalpayment=0;
		for(int loop=0;loop<foodordered.size();loop++)
		{
			totalpayment+=foodordered.get(loop).gettotalprice();
		}
		totalpayment+=totalpayment*16/100;//tax
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		date = new Date();
		
		dateorder=dateFormat.format(date);
		
		
		
		cookdone=false;
		
	}
	
	public Date getdate()
	{
		return date;
	}
	
	public String getcustomerusername()
	{
		return customerusername;
	}
	
	public boolean getcookcondition()
	{
		return cookcondition;
	}
	
	public ArrayList<foodorderdetail> getfoodordered()
	{
		return foodordered;
	}
	
	public void setcookcondition(boolean cookcondition)
	{
		this.cookcondition=cookcondition;
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		cookstarttime=dateFormat.format(date);
	}
	
	public int gettableno()
	{
		return tableno;
	}
	
	public double gettotalpayment()
	{
		return totalpayment;
	}
	
	public String getdateorder()
	{
		return dateorder;
	}
	
	public boolean getcookdone()
	{
		return cookdone;
	}
	
	public void setcookdone(boolean cookdone)
	{
		this.cookdone=cookdone;
	}
	
	public String getcookstarttime()
	{
		return cookstarttime;
	}
	
	public void recalculatetotalprice()
	{
		totalpayment=0;
		for(int loop=0;loop<foodordered.size();loop++)
		{
			totalpayment+=foodordered.get(loop).gettotalprice();
		}
		totalpayment+=totalpayment*16/100;//tax
	}
	


}
