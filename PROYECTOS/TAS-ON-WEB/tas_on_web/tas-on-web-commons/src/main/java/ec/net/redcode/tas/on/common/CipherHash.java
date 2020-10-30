package ec.net.redcode.tas.on.common;

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

    /**
     *
     * @param username
     * @param iv
     * @param passphrase
     * @param plaintext
     * @return
     * @throws UnsupportedEncodingException
     */
    public String encrypt(String username, String iv, String passphrase, String plaintext) throws UnsupportedEncodingException {
        String salt = generateSalt(username);
        String hexString = Hex.encodeHexString(salt.getBytes());
        SecretKey key = generateKey(hexString, passphrase);
        byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes("UTF-8"));
        return base64(encrypted);
    }

    /**
     *
     * @param username
     * @param iv
     * @param passphrase
     * @param ciphertext
     * @return
     * @throws UnsupportedEncodingException
     */
    public String decrypt(String username, String iv, String passphrase, String ciphertext) throws UnsupportedEncodingException {
        String salt = generateSalt(username);
        String hexString = Hex.encodeHexString(salt.getBytes());
        SecretKey key = generateKey(hexString, passphrase);
        byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
        return new String(decrypted, "UTF-8");
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
        return user.concat(Constant.PUBLIC_KEY);
    }

    private SecretKey generateKey(String salt, String passphrase) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw fail(e);
        }
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) {
        try {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
        } catch (InvalidKeyException
                | InvalidAlgorithmParameterException
                | IllegalBlockSizeException
                | BadPaddingException e) {
            throw fail(e);
        }
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
