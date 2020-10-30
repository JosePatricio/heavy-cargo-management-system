package ec.redcode.tas.on.facturacion.xmlsign;

public class SignatureNotFound extends RuntimeException {

    public SignatureNotFound() {
        super("Cannot find Signature element.");
    }
}
