package Utils;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Utils {

    public static String computeChecksum(long timestamp){

        try {
            /** Compute base **/
            String s = Long.toString(timestamp) + getSharedSecret();

            System.out.println("SecretToHash:" + Long.toString(timestamp) + getSharedSecret());

            /** Compute MD5 checksum **/
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(s.getBytes(),0,s.length());
            String checksum = (new BigInteger(1, m.digest()).toString(16));
            return checksum;

        }
        catch (NoSuchAlgorithmException exc){
            System.out.println(exc.getMessage());
        }
        return null;
    }

    public static ArrayList<Mail> readMailsFromFile(String filepath){
        ArrayList<Mail> mails = new ArrayList<Mail>();
        System.out.println(filepath);
        try{
            InputStream is = new FileInputStream(filepath);
            BufferedReader buf = new BufferedReader(new InputStreamReader(is));

            String line = buf.readLine();
            StringBuilder sb = new StringBuilder();

            while(line != null){
                sb.append(line).append("\n");
                line = buf.readLine();
            }

            String fileAsString = sb.toString();
            System.out.println("Contents : " + fileAsString);

            /* Split each line */
            String[] splittedMessage = fileAsString.split("\\r\\n|\\n|\\r");
            ArrayList<String> newMail = new ArrayList<String>();

            for (String split : splittedMessage){
                if (!split.equals("")){
                    newMail.add(split);
                    if (split.startsWith(".")){
                    /* Check that headers are enough */
                        if (newMail.size() > 5){
                            ArrayList<String> headers = new ArrayList<String>();
                            headers.add(newMail.get(0));
                            headers.add(newMail.get(1));
                            headers.add(newMail.get(2));
                            headers.add(newMail.get(3));
                            headers.add(newMail.get(4));

                            String body = "";
                            for (int i = 5; i < newMail.size(); ++ i)
                                body += newMail.get(i);

                        /* Create a new message */
                            Mail mail = new Mail(headers, body, new User("null", "null"));
                            mails.add(mail);
                            newMail.clear();

                        }
                    }
                }

            }


        }
        catch (Exception e){
            e.printStackTrace();
        }


        return mails;
    }


    public static String getSharedSecret(){
        return "SharedSecretRandom";
    }
}
