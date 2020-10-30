package ec.net.redcode.tas.on.common.util;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.node.TextNode;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que permite setear correctamente la fecha que viene desde json
 *
 */
public class JsonDateDeserializer extends JsonDeserializer<Timestamp> {


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");

    @Override
    public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        ObjectCodec oc = jp.getCodec();
        TextNode node = (TextNode) oc.readTree(jp);
        String dateString = node.getTextValue();
        Date date = null;
        try {
            date = dateTimeFormat.parse(dateString);
        } catch (Exception e) {
            try{
                date = dateFormat.parse(dateString);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }
}
