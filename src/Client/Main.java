package Client;

import Utils.Mail;
import Utils.Utils;
import java.util.ArrayList;

public class Main {
    public static void main(String [ ] args)
    {
        // on a mis 2001 et non le port habituel pour des problèmes de disponibilité de port
        Client client = new Client(2001, "localhost");
        //greg 192.168.43.131

        client.launch();
    }
}
