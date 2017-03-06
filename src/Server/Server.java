package Server;

import javax.sound.midi.SysexMessage;
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
    private String STATE_LISTENING = "LISTENING";
    private String STATE_AUTHORIZATION = "AUTHORIZATION";
    private String STATE_TRANSACTION = "TRANSACTION";
    private String STATE_UPDATE = "UPDATE";
    private String STATE_CLOSED = "CLOSED";

    private String MESSAGE_HELLO = "+OK POP3 server ready";

    private int port;
    private String state;

    public Server(int port) {
        this.port = port;
        this.state = STATE_CLOSED;
    }

    /**
     * Start server
     */
    public void launch(){
        try{
            ServerSocket myconnex = new ServerSocket(port,6);
            this.state = STATE_LISTENING;
            int currentByte = -1;
            while (true){
                Socket connexion = myconnex.accept();
                try {
                    InputStream is = connexion.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    //bis.write(MESSAGE_HELLO.getBytes());


                }
                catch(IOException ex){
                    System.err.println(ex);
                }
            }
        }
        catch(IOException ex){
            System.err.println(ex);
        }
    }

}
