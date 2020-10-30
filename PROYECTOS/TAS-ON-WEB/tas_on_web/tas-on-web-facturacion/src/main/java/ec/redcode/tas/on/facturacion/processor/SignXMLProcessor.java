package ec.redcode.tas.on.facturacion.processor;

import ec.redcode.tas.on.facturacion.xmlsign.PrivateKeyData;
import ec.redcode.tas.on.facturacion.xmlsign.XmlSigner;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

public class SignXMLProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(SignXMLProcessor.class);

    private final String PATH_CERTIFICATE = "jose_raul_vallejo_espinoza.p12";
    private final String PASS = "Tason1234";

    private XmlSigner signer;

    @Override
    public void process(Exchange exchange) throws Exception {
        PrivateKeyData keyData = createKeyData();
        this.signer = new XmlSigner(keyData);
        String xmlToSign = exchange.getIn().getBody(String.class);
        String xmlSign = sign(xmlToSign);
        exchange.getIn().setBody(xmlSign);
    }

    private String sign(String xmlToUnsignedDocument) throws Exception {
        return signer.sign(xmlToUnsignedDocument);
    }


    private PrivateKeyData createKeyData() {
        return new PrivateKeyData(PATH_CERTIFICATE, PASS, PASS);
    }


    private String getPathToFileOnClasspath(String name) {
        //URL unsignedXmlUrl = this.getClass().getClassLoader().getResource(name);
        URL unsignedXmlUrl = Thread.currentThread().getContextClassLoader().getResource(name);
        return unsignedXmlUrl.getFile();
    }

}
