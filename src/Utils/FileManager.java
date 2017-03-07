package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author GregoirePiat
 */
public class FileManager {

    public static final String FILE_STORAGE = System.getProperty("user.dir") + "../tmp/";

    public static void storeMessage(Mail mail){

        /** @TODO  Format mail **/

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_STORAGE));
            writer.write(mail.getBody());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }



}
