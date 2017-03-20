package Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author GregoirePiat
 */
public class FileManager {

    public static final String FILE_STORAGE = System.getProperty("user.dir") + "/tmp/";
    public static final String CLIENT_STORAGE = System.getProperty("user.dir") + "/ressource/client/";
    public static final String SERVER_STORAGE = System.getProperty("user.dir") + "/ressource/server/";


    /**
     * Store mail into user file
     * @param mail Mail
     */
    public static void storeMail(Mail mail, boolean forTheClient){

        // store in a different file whether it's for the client or for the server
        String filePath = (forTheClient ? CLIENT_STORAGE : SERVER_STORAGE) + mail.getUser().getName();
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

    /**
     * Store mails into user file
     * @param mail Mail
     */
    public static void storeMails(Mail mail){

    }

    /**
     *
     * @param mail Mail
     * @return String
     */
    public static String formatMailString(Mail mail){

        String mailString = "";
        mailString += mail.getFromHeader() + "\r\n";
        mailString += mail.getToHeader() + "\r\n";
        mailString += mail.getSubjectHeader() + "\r\n";
        mailString += mail.getDateHeader() + "\r\n";
        mailString += "\r\n";
        mailString += mail.getBody();

        return mailString;
    }

    /**
     * @param user User
     * @return a list of messages
     */
    public static ArrayList<Mail> retrieveMails(User user) {
        String filePath = SERVER_STORAGE + user.getName() + ".txt";
        return Utils.readMailsFromFile(filePath);
    }

    /**
     * TODO
     * @param user User
     * @return a list of messages
     */
    public static boolean readUserMails(User user) {

        String filePath = SERVER_STORAGE + user.getName() + ".txt";
        user.setMails(Utils.readMailsFromFile(filePath));

        return true;
    }
}
