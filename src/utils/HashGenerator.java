package blockchain.utils;

import java.util.Random;
import java.time.Instant;
import java.security.MessageDigest;

class StringUtil {

    /* Applies Sha256 to a string and returns a hash. */
    public static String generateSha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}

public class HashGenerator {

    private final static Random randomizer = new Random();

    public static HashResult generate() {
        final var magicNumber = String.valueOf(randomizer.nextInt(0, Integer.MAX_VALUE));
        final var hashSum     = StringUtil.generateSha256(magicNumber);

        return new HashResult(hashSum, magicNumber, Instant.now());
    }

}