package Utils;

import java.util.ArrayList;

/**
 * @author GregoirePiat
 */
public class User {

    private String name;

    private String mailAddress;

    private ArrayList<Mail> mails;

    /**
     * User constructor
     * @param name String
     * @param mailAddress String
     */
    public User(String name, String mailAddress){
        this.name = name;
        this.mailAddress = mailAddress;
        this.mails = new ArrayList<Mail>();
    }

    public void addMail (Mail mail){
        this.mails.add(mail);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public ArrayList<Mail> getMails() {
        return mails;
    }

    public void setMails(ArrayList<Mail> mails) {
        this.mails = mails;
    }
}
