package orderdatabase;

public class currentpaymentrequest 
{
	private String clientusername;
	private int tableno;
	
	public currentpaymentrequest(String clientusername,int tableno)
	{
		this.clientusername=clientusername;
		this.tableno=tableno;
	}
	
	public String getclientusername()
	{
		return clientusername;
	}
	
	public int gettableno()
	{
		return tableno;
	}

}
