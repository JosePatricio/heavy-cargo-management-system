package ec.redcode.tas.on.android.services;

import android.util.Log;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.CatalogoItemDTO;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

/**
 * Created by Walter on 20/3/18.
 */

public class CatalogoService extends UtilService {

    public Observable<List<CatalogoItemDTO>> getCatalogoItemByType(final String idCatalogoType) {
        return ObservableBuffer.fromCallable(new Callable<List<CatalogoItemDTO>>() {
            @Override
            public List<CatalogoItemDTO> call() throws Exception {
                String respuesta = get(Constants.URL_CATALOGO_ITEM_BY_TYPE.replace("#idCat", idCatalogoType));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                CatalogoItemDTO[] response = gson.fromJson(respuesta, CatalogoItemDTO[].class);

                /**
                 List<CatalogoItemDTO> catalogoItem = new ArrayList<>();
                 for (CatalogoItemDTO catalogo : response) {
                 catalogoItem.add(catalogo);
                 }
                 **/
                return Arrays.asList(response);
            }
        });
    }
}
