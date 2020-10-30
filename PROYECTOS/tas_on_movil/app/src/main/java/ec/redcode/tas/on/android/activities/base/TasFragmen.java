package ec.redcode.tas.on.android.activities.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ec.redcode.tas.on.android.Constants;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.adapters.CatalogoItemAdapter;
import ec.redcode.tas.on.android.adapters.LocalidadAdapter;
import ec.redcode.tas.on.android.dto.LocalidadDTO;
import ec.redcode.tas.on.android.services.PublicService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class TasFragmen extends Fragment implements Serializable {

    public static final String SEL_PESO = "Seleccione unidad peso";
    public static final String SEL_VOLUMEN = "Seleccione volumen";
    public static final String SEL_PROVINCIA = "Seleccione provincia";
    public static final String SEL_CANTON = "Seleccione canton";


    public static void setDefValSpinLocale(Spinner spnr, long idLocalidad) {
        LocalidadAdapter adapter = (LocalidadAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItem(position).getLocalidadId() == idLocalidad) {
                spnr.setSelected(true);
                spnr.setSelection(position);
                return;
            }
        }
    }

    public static void setDefValSpinCatalogItem(Spinner spnr, String catalogDescrip) {
        CatalogoItemAdapter adapter = (CatalogoItemAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItem(position).getCatalogoItemDescripcion().equalsIgnoreCase(catalogDescrip)) {
                spnr.setSelected(true);
                spnr.setSelection(position);
                return;
            }
        }
    }

    public static void setDefValSpinCatalogItem(Spinner spnr, int idCatalog) {
        CatalogoItemAdapter adapter = (CatalogoItemAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if (adapter.getItem(position).getCatalogoItemId() == idCatalog) {
                spnr.setSelected(true);
                spnr.setSelection(position);
                return;
            }
        }
    }

    /**
     * public static void setDefValSpinLocale(Spinner spnr, ArrayAdapter<?> spinnerAdapter, long idLocalidad) {
     * for (int position = 0; position < spinnerAdapter.getCount(); position++) {
     * if (((LocalidadDTO) spinnerAdapter.getItem(position)).getLocalidadId() == idLocalidad) {
     * spnr.setSelection(position);
     * return;
     * }
     * }
     * }
     **/

    public void loadCantones(Integer idLocalidad, final Spinner spinner) {
        loadCantones(idLocalidad, spinner, null);
    }

    public void loadCantones(Integer idLocalidad, final Spinner spinner, final Integer idLocalidadSelec) {
        PublicService publicService = new PublicService();
        final Observable<LocalidadDTO[]> catalogoProv = publicService.getLocalidadByPadreAndState(idLocalidad, Constants.ID_ESTADO_ACTIVO);
        catalogoProv
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LocalidadDTO[]>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        progress();
                    }

                    @Override
                    public void onNext(LocalidadDTO[] localidadDTOS) {
                        if (checkContext(getContext())) {
                            List<LocalidadDTO> dtos = new ArrayList<>();
                            dtos.add(new LocalidadDTO(SEL_CANTON));
                            dtos.addAll(Arrays.asList(localidadDTOS));
                            LocalidadDTO[] stockArr = new LocalidadDTO[dtos.size()];
                            localidadDTOS = dtos.toArray(stockArr);

                            LocalidadAdapter adapter = new LocalidadAdapter(getContext(), android.R.layout.simple_spinner_item, localidadDTOS);
                            spinner.setAdapter(adapter);
                            spinner.refreshDrawableState();

                            if (idLocalidadSelec != null)
                                setDefValSpinLocale(spinner, idLocalidadSelec);
                        }
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });
    }

    public void spinProvCant(final Spinner spinProv, final Spinner spinCanton, final int idCanton) {
        PublicService publicService = new PublicService();
        final Observable<LocalidadDTO> catalogoProv = publicService.getLocalidadById(idCanton);
        catalogoProv
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<LocalidadDTO>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        progress();
                    }

                    @Override
                    public void onNext(LocalidadDTO localidadDTO) {
                        if (checkContext(getContext())) {
                            setDefValSpinLocale(spinProv, localidadDTO.getLocalidadLocalidadPadre());
                            loadCantones(localidadDTO.getLocalidadLocalidadPadre(), spinCanton, idCanton);
                        }
                        done();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.w("Error", e);
                        done();
                    }

                    @Override
                    public void onComplete() {
                        done();
                    }
                });
    }

    public void showShortMessage(String message) {
        buildMessage(message, Toast.LENGTH_SHORT).show();
    }

    public void showLongMessage(String message) {
        buildMessage(message, Toast.LENGTH_LONG).show();
    }

    private Toast buildMessage(String message, int length) {
        return Toast.makeText(getContext(), message, length);
    }

    public void launchFragment(Fragment fragment) {
        checkStateSupport(R.id.content, fragment);
    }

    public void checkStateSupport(int containerViewId, Fragment fragment) {
        if (!getFragmentManager().isStateSaved())
            getFragmentManager().beginTransaction().replace(containerViewId, fragment).addToBackStack(null).commit();
    }

    public boolean checkContext(Context context) {
        if (context != null)
            return true;
        else
            return false;
    }

    /**
     * Override methods
     **/
    protected abstract void progress();

    protected abstract void done();

    protected void progress(Button btnDisable) {
        btnDisable.setClickable(false);
        btnDisable.setEnabled(false);
        progress();
    }

    protected void done(Button btnEnable) {
        btnEnable.setClickable(true);
        btnEnable.setEnabled(true);
        done();
    }

}
