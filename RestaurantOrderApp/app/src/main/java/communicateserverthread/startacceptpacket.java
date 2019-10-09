package communicateserverthread;

import java.net.Socket;

/**
 * Created by OwYeong on 9/14/2017.
 */

public class startacceptpacket
{

    public static void startacceptpacketfromserver(Socket socket)
    {

        handlepacketfromserver handlep=new handlepacketfromserver(socket);
        Thread td=new Thread(handlep);

        td.start();


    }
}
