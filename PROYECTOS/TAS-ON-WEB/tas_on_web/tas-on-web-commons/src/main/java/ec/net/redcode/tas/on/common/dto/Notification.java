package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Notification implements Serializable {

    @XmlElement
    private String title;
    @XmlElement
    private String body;

}
