package Utils;

/**
 * Created by Nicolas on 07/03/2017.
 * couche qui simplifie la gestion des échanges de message
 */

import java.io.*;
import java.net.*;

public class Message {

    private Socket socket;

    private String MSG_END = "\r\n";

    public Message (Socket socket) {
        this.socket = socket;
    }

    /**
     * Stop the process until the serve receives the endSequence
     * @param endSequence String
     * @return String
     */
    public String read(String endSequence) {
        byte[] messageByte = new byte[1000];
        boolean end = false;
        String messageString = "";
        int bytesRead = 0;
        try
        {
            DataInputStream in = new DataInputStream(socket.getInputStream());

            while(!end)
            {
                // increment read array
                bytesRead = in.read(messageByte);
                messageString += new String(messageByte, 0, bytesRead);

                // test end of message
                if (messageString.contains(endSequence)) {
                    // recept end sequence, end the message
                    end = true;

                    // below, a commented section to test the message received
                    /*System.out.println("full " + messageString);
                    String[] lines = messageString.split(endSequence);
                    for(String s : lines)
                    {
                        System.out.print(" //// " + s);
                    }*/
                }
            }
            System.out.println("Message received: " + messageString);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return messageString;
    }

    /**
     * Send a message to the client
     * @param message String
     */
    public void write(String message) {
        try {
            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            bos.write((message + MSG_END).getBytes());
            bos.flush();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
