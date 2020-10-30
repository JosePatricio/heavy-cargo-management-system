package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.FleteListDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by User on 22/01/2018.
 */

public class ImageService extends UtilService {

    public Observable<List<String>> getImage(final String ofertId, final int state) {
        return Observable.fromCallable(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                String respuesta = get(Constants.URL_GET_IMAGE + ofertId + "/" + state, Constants.user.getToken());
                return new Gson().fromJson(respuesta, List.class);
            }
        });
    }

    public Observable<Boolean> checkLogin(final String token) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                String respuesta = get(Constants.urlFletesList.replace("#idState",
                        String.valueOf(Constants.ID_CHECK)), token);
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                Gson gsonCkeck = new Gson();

                try {
                    FleteListDTO[] listDTO = gson.fromJson(respuesta, FleteListDTO[].class);
                    return true;
                } catch (Exception e) {
                    OfertResponseDTO response = gsonCkeck.fromJson(respuesta, OfertResponseDTO.class);

                }
                return false;
            }
        });
    }

}
