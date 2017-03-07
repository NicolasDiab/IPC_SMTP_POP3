package Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author GregoirePiat
 */
public class FileManager {

    public static final String FILE_STORAGE = System.getProperty("user.dir") + "/tmp/";


    /**
     * Store mail into user file
     * @param mail Mail
     */
    public static void storeMail(Mail mail){

        String filePath = FILE_STORAGE + mail.getUser().getName();
        String mailString = "";

        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            ArrayList<String> headers = new ArrayList<String>();

            System.out.println(mail.getUser().getName());
            System.out.println(mail.getBody());

            mailString = formatMailString(mail);

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), "utf-8"))) {
                writer.write(mailString);
            }
        }
        catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static String formatMailString(Mail mail){

        String mailString = "";
        mailString += mail.getFromHeader() + "\r\n";
        mailString += mail.getToHeader() + "\r\n";
        mailString += mail.getSubjectHeader() + "\r\n";
        mailString += mail.getDateHeader() + "\r\n";
        mailString += "\r\n";
        mailString += mail.getBody() + ".\r\n";

        return mailString;
    }
}
