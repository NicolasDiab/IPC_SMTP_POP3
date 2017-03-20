package Utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author GregoirePiat
 */
public class FileManager {

    public static final String FILE_STORAGE = System.getProperty("user.dir") + "/tmp/";
    public static final String MAIL_STORAGE = System.getProperty("user.dir") + "/ressource/server/";


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

    public static String formatMailString(Mail mail){

        String mailString = "";
        mailString += mail.getFromHeader() + "\r\n";
        mailString += mail.getToHeader() + "\r\n";
        mailString += mail.getSubjectHeader() + "\r\n";
        mailString += mail.getDateHeader() + "\r\n";
        mailString += "\r\n";
        mailString += mail.getBody() + ".\r\n";

        return mailString;
    }

    /**
     * TODO
     * @param user
     * @return a list of messages
     */
    public static ArrayList<Mail> retrieveMails(User user) {
        /*ArrayList<Mail> list = new ArrayList<Mail>();
        ArrayList<String> headers = new ArrayList<>();
        headers.add("jdoe@machine.example");
        headers.add("mary@machine.example");
        headers.add("Saying Hello");
        headers.add("21 Nov 1997");
        headers.add("1");
        Mail mail = new Mail(headers, "Hello server, \r\nThis is a message just to say hello.\r\nSo, \"Hello\".", user);

        list.add(mail);
        return list;*/

        String filePath = MAIL_STORAGE + user.getName() + ".txt";

        return Utils.readMailsFromFile(filePath);
    }

    /**
     * TODO
     * @param user
     * @return a list of messages
     */
    public static boolean readUserMails(User user) {

        String filePath = MAIL_STORAGE + user.getName() + ".txt";
        user.setMails(Utils.readMailsFromFile(filePath));

        return true;
    }
}
