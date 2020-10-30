package ec.redcode.tas.on.android.services;

import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.FleteListDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import ec.redcode.tas.on.android.mappers.FleteListMapper;
import ec.redcode.tas.on.android.models.FleteShort;
import io.reactivex.Observable;

/**
 * Created by Josue Ortiz on 28/12/2017.
 * Servicio. pregunta por listado de fletes
 */

public class FleteListService extends UtilService {

    public Observable<List<FleteShort>> getList() {
        return Observable.fromCallable(new Callable<List<FleteShort>>() {
            @Override
            public List<FleteShort> call() throws Exception {
                String respuesta = get(Constants.urlFletesList.replace("#idState",
                        String.valueOf(Constants.ID_SOLICITUD_ENVIO_ACTIVA)));

                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                FleteListDTO[] listDTO = gson.fromJson(respuesta, FleteListDTO[].class);

                List<FleteShort> list = new ArrayList<>();

                for (FleteListDTO fleteInList : listDTO) {
                    FleteShort flete = FleteListMapper.responseToFleteList(fleteInList);
                    list.add(flete);
                }

                return list;
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
