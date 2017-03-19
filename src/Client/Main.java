package Client;

import Utils.Mail;
import Utils.Utils;
import java.util.ArrayList;

public class Main {
    public static void main(String [ ] args)
    {
        // on a mis 2002 et non le port habituel pour des problèmes de disponibilité de port sur MAC
        Client client = new Client(2001, "localhost");

        client.launch();
    }
}
