package ec.redcode.tas.on.facturacion.xmlsign;

import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static java.util.Collections.singletonList;

public class Pkcs12KeyProvider implements PrivateKeyProvider {

    public static final String Keystore_Type_Pkcs12 = "pkcs12";
    private final XMLSignatureFactory factory;
    private final KeyStore.PrivateKeyEntry keyEntry;

    public KeyStore getKeyStore() {
        return keyStore;
    }

    private KeyStore keyStore;
    private PrivateKeyData keyData;

    public Pkcs12KeyProvider(XMLSignatureFactory factory, PrivateKeyData keyData) throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableEntryException {
        this.factory = factory;
        this.keyData = keyData;
        this.keyStore = loadKeystore();
        this.keyEntry = loadKeyEntry();
    }

    public Pkcs12KeyProvider(XMLSignatureFactory factory, PrivateKeyData keyData, String signature) throws IOException, NoSuchAlgorithmException, KeyStoreException, CertificateException, UnrecoverableEntryException {
        this.factory = factory;
        this.keyData = keyData;
        this.keyStore = loadKeystore(signature);
        this.keyEntry = loadKeyEntry();
    }

    public KeyInfo loadKeyInfo() {
        X509Certificate certificate = loadCertificate();
        return createKeyInfoFactory(certificate);
    }

    public PrivateKey loadPrivateKey() {
        return keyEntry.getPrivateKey();
    }

    public PublicKey loadPublicKey() {
        return loadCertificate().getPublicKey();
    }

    public X509Certificate loadCertificate() {
        return (X509Certificate) keyEntry.getCertificate();
    }

    private KeyInfo createKeyInfoFactory(X509Certificate certificate) {
        KeyInfoFactory keyInfoFactory = factory.getKeyInfoFactory();
        List<Serializable> x509Content = new ArrayList<>();
        x509Content.add(certificate.getSubjectX500Principal().getName());
        x509Content.add(certificate);
        X509Data data = keyInfoFactory.newX509Data(x509Content);
        return keyInfoFactory.newKeyInfo(singletonList(data));
    }

    private KeyStore loadKeystore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(Keystore_Type_Pkcs12);
        //FileInputStream keystoreStream = new FileInputStream(keyData.pathToKeystore);
        String p12File = getJbossHome() + File.separator + "standalone" + File.separator + "configuration" + File.separator + "tas-on" + File.separator + keyData.pathToKeystore;
        File configFile = new File(p12File);
        FileInputStream keystoreStream = new FileInputStream(configFile);
        char[] passphrase = keyData.passphraseForKeystore;
        keyStore.load(keystoreStream, passphrase);
        return keyStore;
    }

    private KeyStore loadKeystore(String signature) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(Keystore_Type_Pkcs12);
        byte[] decodedBytes = Base64.getDecoder().decode(signature);
        InputStream keystoreStream = new ByteArrayInputStream(decodedBytes);
        char[] passphrase = keyData.passphraseForKeystore;
        keyStore.load(keystoreStream, passphrase);
        return keyStore;
    }

    private KeyStore.PrivateKeyEntry loadKeyEntry() throws NoSuchAlgorithmException, UnrecoverableEntryException, KeyStoreException {
        char[] passphrase = keyData.passphraseForKey;
        String alias = keyStore.aliases().nextElement();
        return (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, new KeyStore.PasswordProtection(passphrase));
    }

    private String getJbossHome(){
        String jbossHome = System.getenv("jboss.home");
        if (jbossHome == null)
            jbossHome = System.getenv("JBOSS_HOME");
        return jbossHome;
    }
}