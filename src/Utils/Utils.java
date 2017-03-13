package Utils;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String computeChecksum(long timestamp){

        try {
            /** Compute base **/
            String s= Long.toString(timestamp) + getSharedSecret();

            /** Compute MD5 checksum **/
            MessageDigest m= MessageDigest.getInstance("MD5");
            m.update(s.getBytes(),0,s.length());
            String checksum = (new BigInteger(1,m.digest()).toString(16));
            return checksum;

        }
        catch (NoSuchAlgorithmException exc){
            System.out.println(exc.getMessage());
        }
        return null;
    }

    public static String getSharedSecret(){
        return "SharedSecretRandom";
    }
}
