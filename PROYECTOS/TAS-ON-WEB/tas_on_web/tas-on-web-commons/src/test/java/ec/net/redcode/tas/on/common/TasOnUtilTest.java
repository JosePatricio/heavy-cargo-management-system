package ec.net.redcode.tas.on.common;

import ec.net.redcode.tas.on.common.util.TasOnUtil;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class TasOnUtilTest {

    @Test
    public void testSecuenciaFactura(){
        int number = 999999999;
        System.out.println(TasOnUtil.getDigitsToSecuence(number) + number);
    }

    @Test
    public void testEncrypt(){
        String text = "hola mundo";
        String encryptedText = encrypt(text);
        String decryptedText = TasOnUtil.decrypt(encryptedText);
        System.out.println(encryptedText);
        Assert.assertEquals(text,decryptedText);
    }

    private static String encrypt(String texto) {
        String base64EncryptedString = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digestOfPassword = md.digest(Constant.KEY.getBytes(StandardCharsets.UTF_8));
            byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
            SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            Cipher cipher = Cipher.getInstance("DESede");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainTextBytes = texto.getBytes(StandardCharsets.UTF_8);
            byte[] buf = cipher.doFinal(plainTextBytes);
            byte[] base64Bytes = Base64.getEncoder().encode(buf);
            base64EncryptedString = new String(base64Bytes);
        } catch (Exception ex) {
        }
        return base64EncryptedString;
    }
}
