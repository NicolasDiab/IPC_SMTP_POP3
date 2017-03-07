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

    public Mail(ArrayList<String> headers, String body, User user){

        this.fromHeader = headers.get(0);
        this.toHeader = headers.get(1);
        this.subjectHeader = headers.get(2);
        this.dateHeader = headers.get(3);
        this.messageId = headers.get(4);
        this.body = body;
        this.user = user;
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
}
