package Server;

import java.io.*;
import java.net.*;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import Utils.*;

/**
 * @author NicolasDiab
 * @author GregoirePiat <gregoire.piat@etu.univ-lyon1.fr>
 */
public class Server {

    /**
     * State constants
     */
    private final String STATE_LISTENING = "LISTENING";
    private final String STATE_AUTHORIZATION = "AUTHORIZATION";
    private final String STATE_TRANSACTION = "TRANSACTION";
    private final String STATE_UPDATE = "UPDATE";
    private final String STATE_CLOSED = "CLOSED";

    /**
     * Client's commands constants
     */
    private final String CMD_APOP = "APOP";
    private final String CMD_STAT = "STAT";
    private final String CMD_RETR = "RETR";
    private final String CMD_QUIT = "QUIT";

    /**
     * Server's messages constants
     */
    private String MSG_OK = "+OK";
    private String MSG_ERR = "-ERR";

    private String MSG_HELLO = this.MSG_OK + " POP3 server ready";

    /**
     * Properties
     */
    private int port;
    private String state;
    private String checksumSent;
    private String timestampSent;

    // couche qui simplifie la gestion des échanges de message avec le client
    private Message messageUtils;

    public Server (int port) {
        this.port = port;
        this.state = STATE_CLOSED;
    }

    /**
     * Start server
     */
    public void launch(){
        try {
            ServerSocket myconnex = new ServerSocket(port,6);

            // accept the client connection - stop the processus waiting for the client
            System.out.println("Attente du client");
            Socket connexion = myconnex.accept();
            System.out.println("Nouveau client conecté");

            // initialize a message instance
            this.messageUtils = new Message(connexion);

            // write a hello message to the client
            this.messageUtils.write(MSG_HELLO);
            System.out.println("message envoyé");

            // Wait for the client response, manage messages received from the client
            // Entering the Listening State
            this.state = STATE_LISTENING;

            while (true){
                String messageReceived = this.messageUtils.read("\r\n");

                switch(messageReceived) {
                    /*switch(this.state){

                    }*/
                }
            }
        }
        catch(IOException ex){
            System.err.println(ex);
        }
    }


    public boolean apopFunction(String checksumReceived){

        String checksum = Utils.computeChecksum(Integer.parseInt(timestampSent));

        /** If checksum is right, return true **/
        if (checksumReceived.equals(checksum))
            return true;

        return false;

    }
}
