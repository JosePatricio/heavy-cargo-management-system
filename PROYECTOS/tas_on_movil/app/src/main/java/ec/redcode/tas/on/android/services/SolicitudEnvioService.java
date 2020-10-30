package ec.redcode.tas.on.android.services;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.SolicitudDTO;
import ec.redcode.tas.on.android.dto.SolicitudEnvioDTO;
import ec.redcode.tas.on.android.dto.requests.OfertUpdateRequestDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

public class SolicitudEnvioService extends UtilService {

    public Observable<String> creaSolicitudEnvio(final SolicitudEnvioDTO solicitudEnvioDTO) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                Gson gson = new Gson();
                String solicitudEnvioJson = gson.toJson(solicitudEnvioDTO);

                String respuesta = post(Constants.URL_SOLICITUD_CREA, solicitudEnvioJson);

                Log.d("respuesta", respuesta);

                OfertResponseDTO response = gson.fromJson(respuesta, OfertResponseDTO.class);

                if (response.getResponse() != null && !response.getResponse().isEmpty()) {
                    return response.getResponse();
                } else {
                    //error en la respuesta
                    return response.getResponseMessage();
                }
            }
        });
    }

    public Observable<String> aceptaSolicitudOferta(final OfertUpdateRequestDTO offerta) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                Gson gson = new Gson();
                String offertaJson = gson.toJson(offerta);

                String respuesta = post(Constants.URL_SOLICITUD_ACEPTA_OFERTA, offertaJson);

                Log.d("respuesta", respuesta);

                OfertResponseDTO response = gson.fromJson(respuesta, OfertResponseDTO.class);

                if (response.getResponse() != null && !response.getResponse().isEmpty()) {
                    return response.getResponse();
                } else {
                    //error en la respuesta
                    return response.getResponseMessage();
                }
            }
        });
    }

    public Observable<SolicitudDTO> getSolicitudByID(final String idSolicitud) {
        return ObservableBuffer.fromCallable(new Callable<SolicitudDTO>() {
            @Override
            public SolicitudDTO call() throws Exception {
                Gson gson = new Gson();
                String respuesta = get(Constants.URL_SOLICITUD_BY_ID.replace("#idSolicitud", idSolicitud));

                Log.d("respuesta", respuesta);

                SolicitudDTO response = gson.fromJson(respuesta, SolicitudDTO.class);

                return response;
            }
        });
    }

    public Observable<List<SolicitudDTO>> getSolicitudOfetada() {
        return ObservableBuffer.fromCallable(new Callable<List<SolicitudDTO>>() {
            @Override
            public List<SolicitudDTO> call() throws Exception {
                Gson gson = new Gson();
                String respuesta = get(Constants.URL_SOLICITUD_OFERTADA);

                Log.d("respuesta", respuesta);

                SolicitudDTO[] response = gson.fromJson(respuesta, SolicitudDTO[].class);

                return Arrays.asList(response);
            }
        });
    }

    public Observable<String> actualizarSolicitudEnvio(final SolicitudEnvioDTO solicitudEnvioDTO) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                Gson gson = new Gson();
                String solicitudEnvioJson = gson.toJson(solicitudEnvioDTO);

                String respuesta = put(Constants.URL_SOLICITUD_EDITA, solicitudEnvioJson);

                Log.d("respuesta", respuesta);

                OfertResponseDTO response = gson.fromJson(respuesta, OfertResponseDTO.class);

                if (response.getResponse() != null && !response.getResponse().isEmpty()) {
                    return response.getResponse();
                } else {
                    //error en la respuesta
                    return response.getResponseMessage();
                }
            }
        });
    }

    public Observable<String> cancelarSolicitudEnvio(final SolicitudEnvioDTO solicitudEnvioDTO) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                Gson gson = new Gson();
                String solicitudEnvioJson = gson.toJson(solicitudEnvioDTO);

                String respuesta = post(Constants.URL_SOLICITUD_CANCELAR, solicitudEnvioJson);

                Log.d("respuesta", respuesta);

                OfertResponseDTO response = gson.fromJson(respuesta, OfertResponseDTO.class);

                if (response.getResponse() != null && !response.getResponse().isEmpty()) {
                    return response.getResponse();
                } else {
                    //error en la respuesta
                    return response.getResponseMessage();
                }
            }
        });
    }
}
