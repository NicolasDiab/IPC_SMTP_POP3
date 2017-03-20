package Utils;

import java.util.ArrayList;

/**
 * @author GregoirePiat
 */
public class Mail {

    private String fromHeader;
    private String toHeader;
    private String subjectHeader;
    private String dateHeader;
    private String messageId;
    private String body;
    private User user;

    private Boolean deleted;

    /**
     * Mail constructor
     * @param headers   ArrayList<String>
     * @param body      String
     * @param user      User
     */
    public Mail(ArrayList<String> headers, String body, User user){

        this.fromHeader = headers.get(0);
        this.toHeader = headers.get(1);
        this.subjectHeader = headers.get(2);
        this.dateHeader = headers.get(3);
        this.messageId = headers.get(4);
        this.body = body;
        this.user = user;
        this.deleted = false;
    }

    public Mail (String string, User user) {
        String[] splittedMessage = string.split("\\r\\n|\\n|\\r");
        ArrayList<String> newMail = new ArrayList<String>();
        for (String split : splittedMessage) {
            if (!split.equals("")) {
                newMail.add(split);
                if (newMail.size() > 5) {
                    ArrayList<String> headers = new ArrayList<String>();
                    headers.add(newMail.get(0));
                    headers.add(newMail.get(1));
                    headers.add(newMail.get(2));
                    headers.add(newMail.get(3));
                    headers.add(newMail.get(4));

                    String body = "";
                    for (int i = 5; i < newMail.size(); ++i)
                        body += newMail.get(i) + "\n";

            /* Create a new message */
                    Mail mail = new Mail(headers, body, user);
                }
            }
        }
    }


    public String getFromHeader() {
        return fromHeader;
    }

    public void setFromHeader(String fromHeader) {
        this.fromHeader = fromHeader;
    }

    public String getToHeader() {
        return toHeader;
    }

    public void setToHeader(String toHeader) {
        this.toHeader = toHeader;
    }

    public String getSubjectHeader() {
        return subjectHeader;
    }

    public void setSubjectHeader(String subjectHeader) {
        this.subjectHeader = subjectHeader;
    }

    public String getDateHeader() {
        return dateHeader;
    }

    public void setDateHeader(String dateHeader) {
        this.dateHeader = dateHeader;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getSize() { return FileManager.formatMailString(this).getBytes().length; }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
