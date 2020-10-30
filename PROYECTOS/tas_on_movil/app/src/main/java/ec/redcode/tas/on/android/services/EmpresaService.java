package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.EmpresaDTO;
import com.google.gson.Gson;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

/**
 * Created by @rw_r on 01/06/2018.
 */
public class EmpresaService extends UtilService {

    public Observable<EmpresaDTO> readEmpresa(final String rucEmpresa) {
        return ObservableBuffer.fromCallable(new Callable<EmpresaDTO>() {
            @Override
            public EmpresaDTO call() throws Exception {
                String respuesta = get(Constants.urlReadEmpre.replace("#rucEmpresa", rucEmpresa));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                EmpresaDTO response = gson.fromJson(respuesta, EmpresaDTO.class);

                return response;
            }
        });
    }

    public Observable<EmpresaDTO> getClienteByToken() {
        return ObservableBuffer.fromCallable(new Callable<EmpresaDTO>() {
            @Override
            public EmpresaDTO call() throws Exception {
                String respuesta = get(Constants.urlClienteByToken);
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                EmpresaDTO response = gson.fromJson(respuesta, EmpresaDTO.class);

                return response;
            }
        });
    }

}
