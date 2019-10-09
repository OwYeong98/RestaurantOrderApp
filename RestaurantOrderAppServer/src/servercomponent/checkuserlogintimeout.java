package servercomponent;
import java.util.Date;

public class checkuserlogintimeout implements Runnable
{
	@Override
	public void run() 
	{
		
		//loop times according to how many client is logged in right now 
		for(int loop=0;loop<Server.Currentclient.size();loop++)
		{
			Date datenow=new Date();
			//check difference between user lastactive time and time now
			long differencebetween=Server.Currentclient.get(loop).getlastactive().getTime()-datenow.getTime();
			
			//if more than 2 minutes
			if(differencebetween>120000)
			{
				//delete the logged in session
				Server.Currentclient.remove(loop);
			}
		}
		
		System.out.println("Checked for timeout user!!");
		
		//sleep the thread for 1 minutes to reduce the load of processor
		//check every 1 minute
		try {
			Server.checktimeoutthread.sleep(60000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
