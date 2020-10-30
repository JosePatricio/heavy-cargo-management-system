package ec.net.redcode.tas.on.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.lang.reflect.InvocationTargetException;


@Data
@NoArgsConstructor
@XmlRootElement
public class TasOnErrorMessage {
	
	/** contains the same HTTP Status code returned by the server */
	@XmlElement(name = "status")
	private int status;
	/** message describing the error*/
	@XmlElement(name = "message")
    private String message;
	/** extra information that might useful for developers */
	@XmlElement(name = "developerMessage")
    private String developerMessage;


	public TasOnErrorMessage(TasOnException ex){
		try {
			BeanUtils.copyProperties(this, ex);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public TasOnErrorMessage(int status, String message, String developerMessage){
		this.status = status;
		this.message = message;
		this.developerMessage = developerMessage;
	}

}
