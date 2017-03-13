package Server;

import java.io.*;
import java.net.*;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    private User user;

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

            // Initialize timestamp when message is sent
            this.timestampSent = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            user = new User("Nico", "nicolas.diab@etu.univ-lyon1.fr");
            ArrayList<String> headers = new ArrayList<>();
            headers.add("From : JohnDoe <jdoe@machine.example>");
            headers.add("To: Mary Smith <mary@machine.example>");
            headers.add("Subject: Saying Hello");
            headers.add("Date: 21 Nov 1997");
            headers.add("Message-ID <1234@local.machine.example>");
            Mail mail = new Mail(headers, "Hello server, \r\nThis is a message just to say hello.\r\nSo, \"Hello\".", user);
            user.addMail(mail);

            // write a hello message to the client
            this.messageUtils.write(MSG_HELLO + " " + timestampSent);
            System.out.println("message envoyé");

            // Wait for the client response, manage messages received from the client
            // Entering the Listening State
            this.state = STATE_LISTENING;

            while (true){
                String messageReceived = this.messageUtils.read("\r\n");
                String command = messageReceived.split("\\s+")[0];
                String parameter = messageReceived.split("\\s+")[1];

                switch(messageReceived) {
                    case CMD_APOP:
                        switch(this.state){
                            case STATE_AUTHORIZATION:
                                if (apopFunction(parameter)){
                                    int mailsCount = user.getMailsCount();
                                    int bytesSize = user.getMailsSize();
                                    /** Set Transaction state **/
                                    this.state = STATE_TRANSACTION;
                                    this.messageUtils.write(MSG_OK + " maildrop has " + mailsCount + "message (" + bytesSize + " octets");
                                }
                                else
                                    this.messageUtils.write(MSG_ERR);
                                ;
                                // client exists ?
                                // +OK maildrop has 1 message (369 octets)
                                //     change state
                                // -ERR
                                break;
                        }
                        break;
                    case CMD_RETR:
                        switch(this.state){
                            case STATE_TRANSACTION:
                                // message exists and not deleted ?
                                // si non -ERR id_message
                                // si oui +OK xxx octets
                                // -> message
                                break;
                        }
                        break;
                    case CMD_STAT:
                        switch(this.state){
                            case STATE_TRANSACTION:
                                // info messages
                                // +OK nb_messages nb_bytes
                                break;
                        }
                        break;
                    case CMD_QUIT:
                        switch(this.state){
                            case STATE_AUTHORIZATION:
                                // close the TCP connection
                                // +OK POP3 server signing off
                                break;
                            case STATE_TRANSACTION:
                                // delete deleted messages
                                // +OK POP3 server signing off (xx messages left) or (maildrop empty)
                                // -ERR some deleted messages not removed
                                // in all cases, close the TCP connection
                                break;
                        }
                        break;
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
