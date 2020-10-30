package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.PasswordDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import com.google.gson.Gson;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

public class AuthService extends UtilService {

    public Observable<String> updatePassword(final PasswordDTO password) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                Gson gson = new Gson();
                String password_ = gson.toJson(password);

                String respuesta = postNoAuth(Constants.urlUpdatePass, password_);

                Log.d("respuesta", respuesta);

                OfertResponseDTO response = gson.fromJson(respuesta, OfertResponseDTO.class);

                if (response.getStatus() != null && !response.getStatus().isEmpty()) {
                    //error en la respuesta
                    return response.getMessage();
                } else {
                    //error en la correcta
                    return response.getResponseMessage();
                }
            }
        });
    }
}
