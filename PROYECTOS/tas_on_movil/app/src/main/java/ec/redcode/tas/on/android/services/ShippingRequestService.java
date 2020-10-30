package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.FleteListDTO;
import ec.redcode.tas.on.android.mappers.FleteListMapper;
import ec.redcode.tas.on.android.models.FleteShort;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by mauchilan on 3/20/18.
 */

public class ShippingRequestService extends UtilService {

    public Observable<List<FleteShort>> getShippingRequestList() {
        return Observable.fromCallable(new Callable<List<FleteShort>>() {
            @Override
            public List<FleteShort> call() throws Exception {
                String respuesta = getBasic(Constants.URL_SHIPPING.concat(Constants.SHIPPING_REQUEST_CREATED));
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

}
