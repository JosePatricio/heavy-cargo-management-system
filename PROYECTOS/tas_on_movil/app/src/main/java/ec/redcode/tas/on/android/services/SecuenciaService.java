package ec.redcode.tas.on.android.services;

import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.Callable;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.SecuenciaDTO;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

public class SecuenciaService extends UtilService {

    public Observable<SecuenciaDTO> obtenerSecuencial(final String rucEmpresa) {
        return ObservableBuffer.fromCallable(new Callable<SecuenciaDTO>() {
            @Override
            public SecuenciaDTO call() throws Exception {
                String respuesta = get(Constants.URL_SECUENCIAL.replace("#rucEmpresa", rucEmpresa));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                SecuenciaDTO response = gson.fromJson(respuesta, SecuenciaDTO.class);

                return response;
            }
        });
    }

    public Observable<SecuenciaDTO> diasHabiles(final String dateFormated) {
        return ObservableBuffer.fromCallable(new Callable<SecuenciaDTO>() {
            @Override
            public SecuenciaDTO call() throws Exception {
                String respuesta = get(Constants.URL_DIAS_HABILES.replace("#fechaCaducidad", dateFormated));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                SecuenciaDTO response = gson.fromJson(respuesta, SecuenciaDTO.class);

                return response;
            }
        });
    }

}
