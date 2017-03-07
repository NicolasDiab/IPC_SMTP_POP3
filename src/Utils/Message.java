package Utils;

/**
 * Created by Nicolas on 07/03/2017.
 */

import java.io.*;
import java.net.*;

public class Message {

    private Socket socket;

    public Message (Socket socket) {
        this.socket = socket;
    }

    public String read() {

        return "";
    }

    public void send(String message) {
        try {
            OutputStream os = socket.getOutputStream();
            BufferedOutputStream bos = new BufferedOutputStream(os);
            bos.write(message.getBytes());
            bos.flush();
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }
}
