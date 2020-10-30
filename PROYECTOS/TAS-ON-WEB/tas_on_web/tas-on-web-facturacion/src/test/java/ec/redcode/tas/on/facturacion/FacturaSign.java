package ec.redcode.tas.on.facturacion;

import ec.redcode.tas.on.facturacion.xmlsign.PrivateKeyData;
import ec.redcode.tas.on.facturacion.xmlsign.XmlSigner;
import ec.redcode.tas.on.facturacion.xmlsign.XmlValidator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.net.URL;

public class FacturaSign extends BaseFirmarTest {

    private final String PATH_CERTIFICATE = "certificado.p12";
    private final String PASS = "Password#1";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private XmlSigner signer;
    private XmlValidator validator;

    @Before
    public void createSignerWithKeyData() throws Exception {
        PrivateKeyData keyData = createKeyData();
        this.signer = new XmlSigner(keyData);
    }

    @Before
    public void createValidatorWithKeyData() throws Exception {
        PrivateKeyData keyData = createKeyData();
        this.validator = new XmlValidator(keyData);
    }

    @Test
    @Ignore
    public void testXML() throws Exception {
        String xmlToInputFile = createXML();
        System.out.println("<-------- XML to Sign -------->");
        System.out.println(xmlToInputFile);
        String xmlSing = sign(xmlToInputFile);
        System.out.println("<-------- XML Sign -------->");
        System.out.println(xmlSing);
        //validate(pathToOutputFile);
    }

    private String  sign(String xmlToUnsignedDocument) throws Exception {
        return signer.sign(xmlToUnsignedDocument);
    }

    private PrivateKeyData createKeyData() {
        String pathToKeystore = getPathToFileOnClasspath(PATH_CERTIFICATE);
        String passphraseForKeystore = PASS;
        String passphraseForKey = PASS;
        return new PrivateKeyData(pathToKeystore, passphraseForKeystore, passphraseForKey);
    }

    private String getPathToFileOnClasspath(String name) {
        URL unsignedXmlUrl = getClass().getClassLoader().getResource(name);
        return unsignedXmlUrl.getFile();
    }

}
