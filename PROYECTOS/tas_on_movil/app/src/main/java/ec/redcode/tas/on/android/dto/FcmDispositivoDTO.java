package ec.redcode.tas.on.android.dto;

public class FcmDispositivoDTO {
    private String fcmDispositivoToken;

    public FcmDispositivoDTO() {
    }

    public FcmDispositivoDTO(String fcmDispositivoToken) {
        this.fcmDispositivoToken = fcmDispositivoToken;
    }

    public String getFcmDispositivoToken() {
        return fcmDispositivoToken;
    }

    public void setFcmDispositivoToken(String fcmDispositivoToken) {
        this.fcmDispositivoToken = fcmDispositivoToken;
    }
}
