package ec.redcode.tas.on.android.dto;

import lombok.Data;

@Data
public class LoginError {
    private String status;
    private String response;
    private String responseMessage;
}
