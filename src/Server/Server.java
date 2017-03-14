package Server;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
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

            // Wait for the client response, manage messages received from the client
            // Entering the Listening State
            this.state = STATE_LISTENING;
            System.out.println("Attente du client");
            Socket connexion = myconnex.accept();

            // accept the client connection - stop the processus waiting for the client
            System.out.println("New client connected");

            // initialize a message instance
            this.messageUtils = new Message(connexion);

            // Initialize timestamp when message is sent
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            this.timestampSent = Long.toString(timestamp.getTime());

            // Initialise users
            User userNico = new User("Nico", "nicolas.diab@etu.univ-lyon1.fr");
            User userGregoire = new User("Gregoire", "gregoire.piat@etu.univ-lyon1.fr");
            ArrayList<User> users = new ArrayList<User>();
            users.add(userNico);
            users.add(userGregoire);
            User currentUser = null;

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
                                // Checksum
                                if (/*apopFunction(parameterArray[1])*/true){
                                    // does the user exists ?
                                    String username = parameterArray[0];
                                    for (User u : users) {
                                        if (u.getName().equals(username)) {
                                            currentUser = u;
                                        }
                                    }

                                    if (currentUser == null) {
                                        this.messageUtils.write(MSG_ERR + " This user does not exist");
                                    } else {
                                        // retrieve the user's mails
                                        currentUser.setMails(FileManager.retrieveMails(currentUser));
                                        // get and write informations
                                        int mailsCount = currentUser.getMailsCount();
                                        int bytesSize = currentUser.getMailsSize();
                                        /** Set Transaction state **/
                                        this.state = STATE_TRANSACTION;
                                        this.messageUtils.write(MSG_OK + " maildrop has " + mailsCount + " message(s) (" + bytesSize + " octets)");
                                    }
                                }
                                else
                                    this.messageUtils.write(MSG_ERR + " Incorrect checksum");
                                break;
                            case STATE_TRANSACTION:
                                this.messageUtils.write(MSG_ERR + " You need to be TCP connected first");
                                break;
                        }
                        break;
                    case CMD_RETR:
                        switch(this.state){
                            case STATE_AUTHORIZATION:
                                this.messageUtils.write(MSG_ERR + " You need to be connected first");
                                break;
                            case STATE_TRANSACTION:
                                try {
                                    int mailId = Integer.parseInt(parameterArray[0]);
                                    // message exists and not deleted ?
                                    if (currentUser.hasMail(mailId) && !currentUser.mailDeleted(mailId)) {
                                        Mail m = currentUser.getMail(mailId);
                                        this.messageUtils.write(MSG_OK + " " + m.getSize() + " bytes");
                                        // write the message
                                        this.messageUtils.write(FileManager.formatMailString(m));
                                    } else {
                                        this.messageUtils.write(MSG_ERR + " " + mailId + " The message you want to retrieve does not exists or has been deleted");
                                    }
                                } catch (Exception e) {
                                    this.messageUtils.write(MSG_ERR + " wrong format. Please enter an integer for the message's id you want to retrieve");
                                }
                                break;
                        }
                        break;
                    case CMD_STAT:
                        switch(this.state){
                            case STATE_AUTHORIZATION:
                                this.messageUtils.write(MSG_ERR + " You need to be connected first");
                                break;
                            case STATE_TRANSACTION:
                                // info messages
                                int nb_messages = currentUser.getMailsCount();
                                int nb_bytes = currentUser.getMailsSize();
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
                                    int nb_messages = currentUser.getMailsCount();
                                    this.messageUtils.write(MSG_OK +" POP3 server signing off (" + nb_messages +
                                            " messages left)");
                                } else {
                                    this.messageUtils.write(MSG_ERR + " some deleted messages were not removed");
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

        System.out.println(Long.parseLong(timestampSent));

        String checksum = Utils.computeChecksum(Long.parseLong(timestampSent));

        System.out.println("ChecksumReceived -  " + checksumReceived);
        System.out.println("ChecksumComputed -  " + checksum);

        /** If checksum is right, return true **/
        if (checksumReceived.equals(checksum))
            return true;

        return false;

    }
}
