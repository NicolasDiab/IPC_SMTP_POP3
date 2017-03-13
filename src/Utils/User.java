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

<<<<<<< HEAD
    public int getMailsCount(){
        return mails.size();
=======
    public int getMailsSize() {
        int size = 0;
        for (Mail mail : mails) {
            size += mail.getSize();
        }
        return size;
>>>>>>> 3f6ebdb62f49c7ef1ebbccd7c01d4a817f3bffa4
    }
}
