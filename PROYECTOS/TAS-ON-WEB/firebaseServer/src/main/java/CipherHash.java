
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class CipherHash {

    private int iterationCount = 0;
    private int keySize = 0;
    private Cipher cipher = null;

    public CipherHash(){

    }

    /**
     *
     * @param keySize
     * @param iterationCount
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public CipherHash(int keySize, int iterationCount) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    }




    public String hashString(String str) throws NoSuchAlgorithmException {
        byte[] buffer = str.getBytes();
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        byte[] digest = md.digest();
        String hash = "";
        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }
        return hash;
    }

    private String generateSalt(String user) throws UnsupportedEncodingException {
        return user.concat("TasOnApp");
    }





    private static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    private static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    private static byte[] hex(String str) {
        try {
            return Hex.decodeHex(str.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException(e);
        }
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }

}
