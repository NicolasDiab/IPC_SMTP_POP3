package Server;

import Utils.FileManager;
import Utils.Mail;
import Utils.User;

import java.util.ArrayList;
import java.util.Date;

public class Main {
    public static void main(String [ ] args)
    {

        String userDir = System.getProperty("user.dir");
        System.out.println(userDir);
        User nico = new User("Strelytsia", "nicotheheros@hotmail.fr");
        ArrayList<String> headers = new ArrayList<>();
        headers.add("From : JohnDoe <jdoe@machine.example>");
        headers.add("To: Mary Smith <mary@machine.example>");
        headers.add("Subject: Saying Hello");
        headers.add("Date: 21 Nov 1997");
        headers.add("Message-ID <1234@local.machine.example>");
        Mail mail = new Mail(headers, "Coucou Jean-Mi, \r\nThis is a message just to say hello.\r\nSo, \"Hello\".", nico);
        FileManager.storeMail(mail);

        // on a mis 2001 et non le port habituel pour des problèmes de disponibilité de port
        //Server server = new Server(2001);

        //server.launch();
    }
}