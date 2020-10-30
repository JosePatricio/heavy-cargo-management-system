package ec.redcode.tas.on.android.services;

import com.google.gson.Gson;

import java.util.concurrent.Callable;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.FcmDispositivoDTO;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

public class FcmService extends UtilService {
    public Observable<String> registraToken(final String token) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Gson gson = new Gson();
                FcmDispositivoDTO dispositivo = new FcmDispositivoDTO(token);
                String fcmToken = gson.toJson(dispositivo);
                return postBasic(Constants.URL_FCM_REGISTRO, fcmToken);
            }
        });
    }
}
