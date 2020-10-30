package ec.redcode.tas.on.android.services;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.Callable;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.dto.CatalogoItemDTO;
import ec.redcode.tas.on.android.dto.ClienteDTO;
import ec.redcode.tas.on.android.dto.EmpresaDTO;
import ec.redcode.tas.on.android.dto.LocalidadDTO;
import ec.redcode.tas.on.android.dto.UsuarioDTO;
import ec.redcode.tas.on.android.dto.responses.OfertResponseDTO;
import io.reactivex.Observable;
import io.reactivex.internal.operators.observable.ObservableBuffer;

/**
 * Created by Walter on 20/3/18.
 */

public class PublicService extends UtilService {

    public Observable<ClienteDTO[]> getEmpresasByType(final String idEmpreType) {
        return ObservableBuffer.fromCallable(new Callable<ClienteDTO[]>() {
            @Override
            public ClienteDTO[] call() throws Exception {
                String respuesta = getBasic(Constants.URL_EMPRESA_CLIENT_BY_TYPE.replace("#idTipo", idEmpreType));
                Gson gson = new Gson();
                Log.d("respuesta", respuesta);

                ClienteDTO[] response = gson.fromJson(respuesta, ClienteDTO[].class);

                return response;
            }
        });
    }

    public Observable<LocalidadDTO[]> getLocalidadByPadreAndState(final Integer idPadre, final Integer estado) {
        return ObservableBuffer.fromCallable(new Callable<LocalidadDTO[]>() {
            @Override
            public LocalidadDTO[] call() throws Exception {
                String respuesta = getBasic(Constants.URL_LOCALIDAD_BY_PADRE.replace("#idPadre", String.valueOf(idPadre)).replace("#estado", String.valueOf(estado)));
                Gson gson = new Gson();

                LocalidadDTO[] response = gson.fromJson(respuesta, LocalidadDTO[].class);

                return response;
            }
        });
    }

    public Observable<LocalidadDTO> getLocalidadById(final Integer idLocalidad) {
        return ObservableBuffer.fromCallable(new Callable<LocalidadDTO>() {
            @Override
            public LocalidadDTO call() throws Exception {
                String respuesta = getBasic(Constants.URL_LOCALIDAD_BY_ID.replace("#idLocalidad", String.valueOf(idLocalidad)));
                Gson gson = new Gson();

                LocalidadDTO response = gson.fromJson(respuesta, LocalidadDTO.class);

                return response;
            }
        });
    }

    public Observable<LocalidadDTO[]> getAllCities() {
        return ObservableBuffer.fromCallable(new Callable<LocalidadDTO[]>() {
            @Override
            public LocalidadDTO[] call() throws Exception {
                String respuesta = getBasic(Constants.URL_LOCALIDAD_ALL_CITIES);
                return new Gson().fromJson(respuesta, LocalidadDTO[].class);
            }
        });
    }

    public Observable<CatalogoItemDTO[]> getCatalogoItemByType(final Integer idCataType) {
        return ObservableBuffer.fromCallable(new Callable<CatalogoItemDTO[]>() {
            @Override
            public CatalogoItemDTO[] call() throws Exception {
                String respuesta = getBasic(Constants.URL_CATALOGO_ITEM_BY_TYPE.replace("#idCata", String.valueOf(idCataType)));
                Gson gson = new Gson();

                CatalogoItemDTO[] response = gson.fromJson(respuesta, CatalogoItemDTO[].class);

                return response;
            }
        });
    }

    public Observable<CatalogoItemDTO> getCatalogoItemById(final Integer idCataItem) {
        return ObservableBuffer.fromCallable(new Callable<CatalogoItemDTO>() {
            @Override
            public CatalogoItemDTO call() throws Exception {
                String respuesta = getBasic(Constants.URL_CATALOGO_ITEM_BY_ID.replace("#idCatItem", String.valueOf(idCataItem)));
                Gson gson = new Gson();

                CatalogoItemDTO response = gson.fromJson(respuesta, CatalogoItemDTO.class);

                return response;
            }
        });
    }

    public Observable<String> createUsuario(final UsuarioDTO usuarioDTO) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Gson gson = new Gson();
                String usuarioJson = gson.toJson(usuarioDTO);
                String respuesta = postBasic(Constants.URL_CREA_USUARIO, usuarioJson);

                return respuesta;
            }
        });
    }

    public Observable<String> createEmpresa(final Map<String, Object> data) {
        return ObservableBuffer.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Gson gson = new Gson();
                String usuarioJson = gson.toJson(data);
                String respuesta = postBasic(Constants.URL_CREA_EMPRESA, usuarioJson);

                return respuesta;
            }
        });
    }

    public Observable<EmpresaDTO> readEmpresa(final String ruc) {
        return ObservableBuffer.fromCallable(new Callable<EmpresaDTO>() {
            @Override
            public EmpresaDTO call() throws Exception {
                String respuesta = getBasic(Constants.URL_READ_EMPRESA.replace("#ruc", ruc));
                Gson gson = new Gson();

                EmpresaDTO response = gson.fromJson(respuesta, EmpresaDTO.class);

                return response;
            }
        });
    }

    public Observable<EmpresaDTO> readEmpresaTransporte(final String ruc) {
        return ObservableBuffer.fromCallable(new Callable<EmpresaDTO>() {
            @Override
            public EmpresaDTO call() throws Exception {
                String respuesta = getBasic(Constants.URL_READ_EMPRESA_TRANSP.replace("#ruc", ruc));
                Gson gson = new Gson();

                EmpresaDTO response = gson.fromJson(respuesta, EmpresaDTO.class);

                return response;
            }
        });
    }

    public Observable<UsuarioDTO> readUserByUsername(final String userName) {
        return ObservableBuffer.fromCallable(new Callable<UsuarioDTO>() {
            @Override
            public UsuarioDTO call() throws Exception {
                String respuesta = getBasic(Constants.URL_READ_USUARIO_BY_USERNAME.replace("#username", userName));
                Gson gson = new Gson();

                UsuarioDTO response = gson.fromJson(respuesta, UsuarioDTO.class);

                return response;
            }
        });
    }

    public Observable<UsuarioDTO> readUserByEmail(final String email) {
        return ObservableBuffer.fromCallable(new Callable<UsuarioDTO>() {
            @Override
            public UsuarioDTO call() throws Exception {
                String respuesta = getBasic(Constants.URL_READ_USUARIO_BY_EMAIL.replace("#email", email));
                Gson gson = new Gson();

                UsuarioDTO response = gson.fromJson(respuesta, UsuarioDTO.class);

                return response;
            }
        });
    }

    public Observable<OfertResponseDTO> restablecerPassword(final String email, final String identificador) {
        return ObservableBuffer.fromCallable(new Callable<OfertResponseDTO>() {
            @Override
            public OfertResponseDTO call() throws Exception {
                String respuesta = getBasic(Constants.URL_RESTABLECER_PASSWORD.
                        replace("#email", email).replace("#identificador", identificador));
                Gson gson = new Gson();
                return gson.fromJson(respuesta, OfertResponseDTO.class);
            }
        });
    }
}
