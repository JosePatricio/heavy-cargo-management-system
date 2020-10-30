package ec.redcode.tas.on.facturacion.processor;

import ec.redcode.tas.on.facturacion.xmlsign.PrivateKeyData;
import ec.redcode.tas.on.facturacion.xmlsign.XmlSigner;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignEBillingXMLProcessor implements Processor {

    private static final Logger logger = LoggerFactory.getLogger(SignEBillingXMLProcessor.class);
    private XmlSigner signer;

    @Override
    public void process(Exchange exchange) throws Exception {
        PrivateKeyData keyData = createKeyData(exchange.getIn().getHeader("claveFirma").toString());
        String signature =  exchange.getIn().getHeader("signature").toString();
        this.signer = new XmlSigner(keyData, signature);
        String xmlToSign = exchange.getIn().getBody(String.class);
        String xmlSign = sign(xmlToSign);
        exchange.getIn().setBody(xmlSign);
    }

    private String sign(String xmlToUnsignedDocument) throws Exception {
        return signer.sign(xmlToUnsignedDocument);
    }

    private PrivateKeyData createKeyData(String claveFirma) {
        return new PrivateKeyData(null, claveFirma, claveFirma);
    }

}
