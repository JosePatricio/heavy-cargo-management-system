package ec.net.redcode.tas.on.common;

import org.apache.cxf.common.util.Base64Utility;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class CipherHashTest {

    private String username = "mauchilan";
    private String password = "TasOn01*";
    private String passwordEncrypt = "yP67AFNBul3ptYuplAFzLA==";
    private String userPassword = Constant.PUBLIC_USER.concat(":").concat(Constant.PUBLIC_PASSWORD);

    @Test
    public void testEncrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        CipherHash cipherHash = new CipherHash(Constant.KEY_SIZE, Constant.ITERATION_COUNT);
        String passEncrypt = cipherHash.encrypt(username, Constant.IV, Constant.PUBLIC_KEY, password);
        Assert.assertNotNull(passEncrypt);
        System.out.println(passEncrypt);
    }

    @Test
    public void testDecrypt() throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        CipherHash cipherHash = new CipherHash(Constant.KEY_SIZE, Constant.ITERATION_COUNT);
        String password = cipherHash.decrypt(username, Constant.IV, Constant.PUBLIC_KEY, passwordEncrypt);
        Assert.assertNotNull(password);
        System.out.println(password);
    }

    @Test
    public void testHash() throws NoSuchAlgorithmException, NoSuchPaddingException {
        CipherHash cipherHash = new CipherHash(Constant.KEY_SIZE, Constant.ITERATION_COUNT);
        String passHash = cipherHash.hashString(password);
        Assert.assertNotNull(password);
        System.out.println(passHash);
    }

    @Test
    public void base64Encode() {
        String data =  Base64Utility.encode(userPassword.getBytes());
        System.out.println(data);
    }

}
