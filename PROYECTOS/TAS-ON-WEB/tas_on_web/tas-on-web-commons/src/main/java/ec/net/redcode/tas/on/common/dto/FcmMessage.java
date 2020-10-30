package ec.net.redcode.tas.on.common.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Data
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FcmMessage implements Serializable {

    @XmlElement
    private List<String> registration_ids;
    @XmlElement
    private Notification notification;

}
