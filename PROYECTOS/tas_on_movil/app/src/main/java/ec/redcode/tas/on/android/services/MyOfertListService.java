package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.responses.OfertListDTO;
import ec.redcode.tas.on.android.mappers.FleteListMapper;
import ec.redcode.tas.on.android.models.FleteShort;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by Josue Ortiz on 11/01/2018.
 * Servicio, pregunta por Ofertas
 */

public class MyOfertListService extends UtilService {

    public static final String MY_OFERTS = "31";
    public static final String MY_OFERTS_APPROVED = "32";
    public static final String MY_OFERTS_TO_RECIEVE = "24";
    public static final String MY_OFERTS_TO_DELIVER = "54";
    public static final String MY_OFERTS_GENERATE_INVOICE = "62";
    public static final String MY_OFERTS_DELIVER_INVOICE = "70";
    public static final String MY_OFERTS_FINISHED = "36";
    public static final String MY_OFERTS_CANCELED = "34";
    public static final String MY_OFERTS_COLLECT = "35";

    public static final String MY_OFERTS_IN_CURSE = "24";

    public Observable<List<FleteShort>> getList(final String requestType) {
        return Observable.fromCallable(new Callable<List<FleteShort>>() {
            @Override
            public List<FleteShort> call() throws Exception {

                String respuesta = get(Constants.urlMyOferts, requestType, Constants.user.getToken());

                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                OfertListDTO[] ofertList = gson.fromJson(respuesta, OfertListDTO[].class);

                List<FleteShort> list = new ArrayList<>();

                for (OfertListDTO ofert : ofertList) {
                    FleteShort flete = FleteListMapper.responseToFleteList(ofert);
                    list.add(flete);
                }

                return list;
            }
        });
    }

}
