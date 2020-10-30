package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.FleteDTO;
import com.google.gson.Gson;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by hotak on 14/1/2018.
 */

public class MyOfertService extends UtilService {

    public Observable<FleteDTO> getDetail(final String id) {
        return Observable.fromCallable(new Callable<FleteDTO>() {
            @Override
            public FleteDTO call() throws Exception {

                String respuesta = get(Constants.urlMyOfertDetail, id, Constants.user.getToken());

                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                FleteDTO flete = gson.fromJson(respuesta, FleteDTO.class);

                return flete;
            }
        });
    }

}
