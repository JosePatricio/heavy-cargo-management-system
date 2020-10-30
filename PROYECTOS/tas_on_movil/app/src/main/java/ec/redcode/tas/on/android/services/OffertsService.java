package ec.redcode.tas.on.android.services;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.dto.OffersDTO;
import ec.redcode.tas.on.android.dto.requests.OfertRequestDTO;
import ec.redcode.tas.on.android.dto.requests.OfertUpdateRequestDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

/**
 * Created by Josue Ortiz on 05/01/017.
 * Servicio de envio de ofertas
 */

public class OffertsService extends UtilService {

    public Observable<Map<String, String>> sendMyOfert(final String fleteId, final Float amount, final String comments) {
        return Observable.fromCallable(new Callable<Map<String, String>>() {
            @Override
            public Map<String, String> call() throws Exception {
                OfertRequestDTO ofert = new OfertRequestDTO();
                ofert.setIdSolicitud(fleteId);
                ofert.setAmount(amount + "");
                ofert.setComments(comments);

                String json = new Gson().toJson(ofert);

                String respuesta = post(Constants.urlMakeAnOfert, json);
                Map<String, String> responseMap = new Gson().fromJson(respuesta, Map.class);
                if(responseMap == null){
                    responseMap = new HashMap<>();
                    responseMap.put("responseMessage", "Ha ocurrido un error");
                }
                return responseMap;
            }
        });
    }

    public Observable<OfertResponseDTO> updateMyOfert(final OfertUpdateRequestDTO request) {
        return Observable.fromCallable(new Callable<OfertResponseDTO>() {
            @Override
            public OfertResponseDTO call() throws Exception {
                String json = new Gson().toJson(request);
                Log.d("  OBJETO   ",Constants.urlUpdateOfert+"  ,   "+json);
                String respuesta = post(Constants.urlUpdateOfert, json);
                return new Gson().fromJson(respuesta, OfertResponseDTO.class);
            }
        });
    }

    public Observable<List<OffersDTO>> getSolicitudOfertada(final String idSolicitud) {
        return ObservableBuffer.fromCallable(new Callable<List<OffersDTO>>() {
            @Override
            public List<OffersDTO> call() throws Exception {
                Gson gson = new Gson();
                String respuesta = get(Constants.URL_OFERTAS_BY_SOLIC_AND_ESTADO.replace("#idSolicitud", idSolicitud).
                        replace("#estado", String.valueOf(Constants.ID_ESTADO_OFERTA_CREADA)));

                Log.d("respuesta", respuesta);

                OffersDTO[] response = gson.fromJson(respuesta, OffersDTO[].class);

                return Arrays.asList(response);
            }
        });
    }

    public Observable<FleteDTO> getdOfertaById(final int idOferta) {
        return ObservableBuffer.fromCallable(new Callable<FleteDTO>() {
            @Override
            public FleteDTO call() throws Exception {
                Gson gson = new Gson();
                String respuesta = get(Constants.URL_OFERTA_BY_ID.replace("#idOferta", String.valueOf(idOferta)));

                Log.d("respuesta", respuesta);

                FleteDTO response = gson.fromJson(respuesta, FleteDTO.class);

                return response;
            }
        });
    }

}
