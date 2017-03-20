package Client;

import Utils.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static Utils.Utils.computeChecksum;

/**
 * @author NicolasDiab
 * @author GregoirePiat
 */
public class Client {

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

    private int port;
    private String ip;


    private List<Mail> mails;

    // couche qui simplifie la gestion des échanges de message avec le serveur
    private Message messageUtils;

    public Client(int port, String ip) {
        this.port = port;
        this.ip = ip;
    }

    /**
     * Connect to server
     * @return Socket
     */
    public Socket connectToServer() {
        System.out.println("Connecting to server");
        Socket connexion = null;
        try {
            connexion = new Socket(InetAddress.getByName(this.ip), this.port);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return connexion;
    }

    /**
     * Start server
     */
    public void launch(){
        try {
            Socket connexion = this.connectToServer();

            mails = new ArrayList<Mail>();

            // initialize a message instance
            this.messageUtils = new Message(connexion);

            // read hello message
            String msgHello = this.messageUtils.read("\r\n");

            // get the timestamp from the hello message
            String timestamp = msgHello.split("\\s+")[4];

            Scanner sc = new Scanner(System.in);

            User user = null;

            // apop command
            String apopResponse = MSG_ERR;

            while (apopResponse.split("\\s+")[0].toUpperCase().equals(MSG_ERR)) {

                /* Ask for username */
                System.out.println("Tapez le nom d'utilisateur voulu");
                String userName = sc.nextLine();
                user = new User(userName, "");

                // send APOP command with timestamp and sharesecret encrypted in md5
                String msgMd5 = computeChecksum(Long.parseLong(timestamp));
                String apopCommand = CMD_APOP + " " + userName + " " + msgMd5;
                this.messageUtils.write(apopCommand);

                /* Read APOP response */
                apopResponse = this.messageUtils.read("\r\n");
                System.out.println(apopResponse);
            }

            // Transaction
            while(!connexion.isClosed()) {
                //scan the client command
                System.out.println("Tapez une commande");
                String command = sc.nextLine();

                //send the command to the server
                this.messageUtils.write(command);

                //wait for server response
                System.out.println("Réponse du serveur : ");
                String messageReceived = this.messageUtils.read("\r\n");

                if (command.split("\\s+")[0].toUpperCase().equals(CMD_RETR)) {
                    // if command sent is RETR
                    // Check server response : +OK or -ERR
                    // if +OK, read the full mail
                    if (messageReceived.split("\\s+")[0].toUpperCase().equals(MSG_OK)) {
                        System.out.println("Mail reçu : ");
                        String mailReceived = this.messageUtils.read("\r\n");
                        // create a Mail object linked to the connected user
                        Mail mail = new Mail(mailReceived, user);

                        boolean store = true;
                        /* Store mail if not in user file */
                        for (Mail m : this.mails) {
                            if (mail.getMessageId().equals(m.getMessageId()))
                                store = false;
                        }

                        if (store){
                            // store the mail in the client file
                            FileManager.storeMail(mail, true);
                            this.mails.add(mail);
                        }
                    }
                }
            }
        }
        catch(Exception ex){
            System.err.println(ex);
        }
    }
}
