package Server;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

            // Wait for the client response, manage messages received from the client
            // Entering the Listening State
            this.state = STATE_LISTENING;

            // accept the client connection - stop the processus waiting for the client
            System.out.println("Attente du client");
            Socket connexion = myconnex.accept();
            System.out.println("Nouveau client conecté");

            // initialize a message instance
            this.messageUtils = new Message(connexion);

            // Initialize timestamp when message is sent
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            this.timestampSent = Long.toString(timestamp.getTime());

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

            this.state = STATE_AUTHORIZATION;


            while (!connexion.isClosed()){
                String messageReceived = this.messageUtils.read("\r\n");
                String command = messageReceived.split("\\s+")[0].toUpperCase();
                String[] parameters = messageReceived.split("\\s+");
                String[] parameterArray = Arrays.copyOfRange(parameters, 1, parameters.length);
                System.out.println("Command " + command);

                switch(command) {
                    case CMD_APOP:
                        switch(this.state){
                            case STATE_AUTHORIZATION:
                                if (/*apopFunction(parameterArray[1])*/true){
                                    int mailsCount = user.getMailsCount();
                                    int bytesSize = user.getMailsSize();
                                    /** Set Transaction state **/
                                    this.state = STATE_TRANSACTION;
                                    this.messageUtils.write(MSG_OK + " maildrop has " + mailsCount + "message (" + bytesSize + " octets");
                                }
                                else
                                    this.messageUtils.write(MSG_ERR);
                                break;
                        }
                        break;
                    case CMD_RETR:
                        switch(this.state){
                            case STATE_TRANSACTION:
                                int mailId = Integer.parseInt(parameterArray[0]);
                                // message exists and not deleted ?
                                if (user.hasMail(mailId) && !user.mailDeleted(mailId)) {
                                    this.messageUtils.write(MSG_ERR + " " + mailId);
                                } else {
                                    Mail m = user.getMail(mailId);
                                    this.messageUtils.write(MSG_OK + " " + m.getSize() + " bytes");
                                    // write the message
                                    this.messageUtils.write(FileManager.formatMailString(m));
                                }
                                break;
                        }
                        break;
                    case CMD_STAT:
                        switch(this.state){
                            case STATE_TRANSACTION:
                                // info messages
                                int nb_messages = user.getMailsCount();
                                int nb_bytes = user.getMailsSize();
                                this.messageUtils.write(MSG_OK + " " + nb_messages + " " + nb_bytes);
                                break;
                        }
                        break;
                    case CMD_QUIT:
                        switch(this.state){
                            case STATE_AUTHORIZATION:
                                this.messageUtils.write(MSG_OK + " POP3 server signing off");
                                // close the TCP connection
                                connexion.close();
                                break;
                            case STATE_TRANSACTION:
                                // remove deleted messages
                                if (true) {
                                    int nb_messages = user.getMailsCount();
                                    this.messageUtils.write(MSG_OK +" POP3 server signing off (" + nb_messages +
                                            " messages left)");
                                } else {
                                    this.messageUtils.write(MSG_ERR + " some deleted messages not removed");
                                }
                                // in all cases, close the TCP connection
                                connexion.close();
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

        String checksum = Utils.computeChecksum(Long.parseLong(timestampSent));

        System.out.println("ChecksumReceived -  " + checksumReceived);
        System.out.println("ChecksumComputed -  " + checksum);

        /** If checksum is right, return true **/
        if (checksumReceived.equals(checksum))
            return true;

        return false;

    }
}
