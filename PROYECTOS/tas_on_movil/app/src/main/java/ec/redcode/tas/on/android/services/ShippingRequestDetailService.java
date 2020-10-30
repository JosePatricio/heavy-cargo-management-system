package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.FleteDTO;
import ec.redcode.tas.on.android.mappers.FleteMapper;
import ec.redcode.tas.on.android.models.Flete;
import com.google.gson.Gson;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by mauchilan on 3/21/18.
 */

public class ShippingRequestDetailService extends UtilService {

    public Observable<Flete> getShippingRequestDetail(final String idSolicitud) {
        return Observable.fromCallable(new Callable<Flete>() {
            @Override
            public Flete call() throws Exception {
                String respuesta = getBasic(Constants.URL_SHIPPING_DETAIL + idSolicitud);
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                FleteDTO fleteDTO = gson.fromJson(respuesta, FleteDTO.class);
                Log.d("respuesta", fleteDTO.toString());

                return FleteMapper.fleteDTOtoFlete(fleteDTO);
            }
        });
    }

}
