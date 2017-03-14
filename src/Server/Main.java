package Server;

import Utils.FileManager;
import Utils.Mail;
import Utils.User;
import Utils.Utils;

import java.util.ArrayList;

public class Main {
    public static void main(String [ ] args)
    {
        String filepath = System.getProperty("user.dir") + "/ressource/server/Nico.txt";
        ArrayList<Mail> mails = Utils.readMailsFromFile(filepath);
        System.out.println(mails.size());

        String userDir = System.getProperty("user.dir");
        System.out.println(userDir);
        User nico = new User("Strelytsia", "nicotheheros@hotmail.fr");
        ArrayList<String> headers = new ArrayList<>();
        headers.add("jdoe@machine.example");
        headers.add("mary@machine.example");
        headers.add("Saying Hello");
        headers.add("21 Nov 1997");
        headers.add("1");
        Mail mail = new Mail(headers, "Hello server, \r\nThis is a message just to say hello.\r\nSo, \"Hello\".", nico);
        FileManager.storeMail(mail);

        // on a mis 2001 et non le port habituel pour des problèmes de disponibilité de port
        Server server = new Server(2001);

        server.launch();
    }
}