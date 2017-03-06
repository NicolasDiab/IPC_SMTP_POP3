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
    private String STATE_LISTENING = "LISTENING";
    private String STATE_AUTHORIZATION = "AUTHORIZATION";
    private String STATE_TRANSACTION = "TRANSACTION";
    private String STATE_UPDATE = "UPDATE";
    private  String STATE_CLOSED = "CLOSED";

    private String MESSAGE_HELLO = "+OK POP3 server ready";


    private int port;
    private String state;

    public Server (int port) {
        this.port = port;
        this.state = STATE_CLOSED;
    }

    /**
     * Start server
     */
    public void launch(){
        try{
            ServerSocket myconnex = new ServerSocket(port,6);

            int currentByte = -1;

            // acceptation de la connexion du client - méthode bloquante en attendant le client
            Socket connexion = myconnex.accept();

            // Envoie du message d'accueil au client
            OutputStream os = connexion.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            bos.write(MESSAGE_HELLO.getBytes());
            bos.flush();
            System.out.print("envoyé");

            // Attente de réponse du client, gestion des différents messages reçus du client
            // on passe en état LISTENING
            this.state = STATE_LISTENING;
            while (true){

                try {


                    InputStream is = connexion.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bis.read();


                }
                catch (Exception e) {
                    System.err.println(e);
                }
            }
        }
        catch(IOException ex){
            System.err.println(ex);
        }

    }

}
