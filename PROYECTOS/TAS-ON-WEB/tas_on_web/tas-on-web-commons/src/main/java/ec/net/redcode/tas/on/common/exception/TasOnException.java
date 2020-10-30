package ec.net.redcode.tas.on.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TasOnException extends Exception {

	private static final long serialVersionUID = -8999932578270387947L;
	
	/**
	 * contains redundantly the HTTP status of the response sent back to the client in case of error, so that
	 * the developer does not have to look into the response headers. If null a default
	 */
	private Integer status;

	/** detailed error description for developers*/
	private String developerMessage;

	public TasOnException(int status, String message,
						  String developerMessage) {
		super(message);
		this.status = status;
		this.developerMessage = developerMessage;
	}

	public TasOnException(int status, String message, Throwable t){
		super(message, t);
		this.status = status;
		this.developerMessage = t.getLocalizedMessage();
	}
					
}
