package Server;

import Utils.FileManager;
import Utils.Mail;

public class Main {
    public static void main(String [ ] args)
    {
        // on a mis 2001 et non le port habituel pour des problèmes de disponibilité de port
        Server server = new Server(2001);

        server.launch();
    }
}