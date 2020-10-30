package ec.net.redcode.tas.on.persistence.entities;

import javax.persistence.*;

@Entity
@Table(name = "fcm_dispositivo")
@NamedQueries({
        @NamedQuery(name = "FcmDispositivo.FcmDispositivoAll", query = "select f from FcmDispositivo f")
})
public class FcmDispositivo {

    private String fcmDispositivoToken;

    @Id
    @Column(name = "fcm_dispositivo_token", nullable = false)
    public String getFcmDispositivoToken() {
        return fcmDispositivoToken;
    }

    public void setFcmDispositivoToken(String fcmDispositivoToken) {
        this.fcmDispositivoToken = fcmDispositivoToken;
    }
}
