package Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author GregoirePiat
 */
public class FileManager {

    public static final String FILE_STORAGE = "../tmp/fileStorage.txt";


    public static void storeMessage(Message message){

        /** @TODO  Format message **/

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(FILE_STORAGE));
            writer.write( message.getBody());
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }



}
