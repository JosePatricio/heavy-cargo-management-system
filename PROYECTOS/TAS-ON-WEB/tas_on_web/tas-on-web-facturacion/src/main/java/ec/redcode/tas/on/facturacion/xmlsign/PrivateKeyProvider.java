package ec.redcode.tas.on.facturacion.xmlsign;

import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface PrivateKeyProvider {

    KeyInfo loadKeyInfo();

    PrivateKey loadPrivateKey();

    X509Certificate loadCertificate();

    KeyStore getKeyStore();
}
