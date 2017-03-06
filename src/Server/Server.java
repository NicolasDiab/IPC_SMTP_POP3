package Server;

import java.io.*;
import java.net.*;
import java.util.Arrays;

/**
 * @author NicolasDiab
 * @author GregoirePiat
 */
public class Server {

    /**
     * State constants
     */
    private const String STATE_LISTENING = "LISTENING";
    private const String STATE_AUTHORIZATION = "AUTHORIZATION";
    private const String STATE_TRANSACTION = "TRANSACTION";
    private const String STATE_UPDATE = "UPDATE";
    private const String STATE_CLOSED = "CLOSED";

    private const String MESSAGE_HELLO = "+OK POP3 server ready";


    private int ip;
    private int port;
    private String state;



    public Server (int ip, int port) {
        this.ip = ip;
        this.port = port;
        this.state = STATE_CLOSED;
    }

    /**
     * Start server
     * @return
     */
    public int launch(){
        try{
            ServerSocket myconnex = new ServerSocket(port,6);
            this.state = STATE_LISTENING;
            int currentByte = -1;
            while (true){
                Socket connexion = server.accept();
                try {
                    InputStream is = connexion.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bis.write(MESSAGE_HELLO.getBytes());


                }
            }
        }
        catch(IOException ex){
            system.err.println(ex);
        }

    }

}
