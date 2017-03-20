package Server;

import Utils.FileManager;
import Utils.Mail;
import Utils.User;
import Utils.Utils;

import java.util.ArrayList;

public class Main {
    public static void main(String [ ] args)
    {
        Utils.displayMessage();

        String filepath = System.getProperty("user.dir") + "/ressource/server/Nico.txt";
        ArrayList<Mail> mails = Utils.readMailsFromFile(filepath);
        System.out.println(mails.size());

        // on a mis 2001 et non le port habituel pour des problèmes de disponibilité de port
        Server server = new Server(2001);

        server.launch();
    }
}