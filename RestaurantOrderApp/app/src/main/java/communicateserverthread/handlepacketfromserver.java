package communicateserverthread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by OwYeong on 9/14/2017.
 */

public class handlepacketfromserver implements Runnable{
    Socket connection;

    public handlepacketfromserver(Socket connection){
        this.connection =connection;
    }

    @Override
    public void run() {

        serverinstruction serverin=new serverinstruction(connection);


        Thread t = new Thread(serverin);
        t.start();

    }
}
