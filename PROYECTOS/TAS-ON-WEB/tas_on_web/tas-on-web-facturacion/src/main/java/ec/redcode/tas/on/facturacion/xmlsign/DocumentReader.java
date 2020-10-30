package ec.redcode.tas.on.facturacion.xmlsign;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DocumentReader {

    private String xmlToFile;

    public DocumentReader(String xmlToFile) {
        this.xmlToFile = xmlToFile;
    }

    public Document loadDocument() throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        return documentBuilderFactory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlToFile.getBytes()));
    }
}