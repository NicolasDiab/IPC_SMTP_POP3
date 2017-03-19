package Client;

import Utils.FileManager;
import Utils.Mail;
import Utils.Message;
import Utils.User;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

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

            // initialize a message instance
            this.messageUtils = new Message(connexion);
            // read hello message
            this.messageUtils.read("\r\n");

            // Initialise users
            User userNico = new User("Nico", "nicolas.diab@etu.univ-lyon1.fr");
            User userGregoire = new User("Gregoire", "gregoire.piat@etu.univ-lyon1.fr");
            ArrayList<User> users = new ArrayList<User>();
            users.add(userNico);
            users.add(userGregoire);
            User currentUser = null;

            // TODO
            // send APOP
            // Receive Timestamp

            while(!connexion.isClosed()) {
                //scan the client command
                Scanner sc = new Scanner(System.in);
                System.out.println("Tapez une commande");
                String command = sc.nextLine();

                //send the command to the server
                this.messageUtils.write(command);

                //wait for server response
                System.out.println("Réponse du serveur : ");
                String messageReceived = this.messageUtils.read("\r\n");
            }
        }
        catch(Exception ex){
            System.err.println(ex);
        }
    }
}
