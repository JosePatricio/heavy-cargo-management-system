package ec.redcode.tas.on.android.fragments.ebilling;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.Utils;
import ec.redcode.tas.on.android.adapters.ebilling.MyEbillingsAdapter;
import ec.redcode.tas.on.android.dto.requests.MyEBillingDTO;
import ec.redcode.tas.on.android.services.EBillingService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyEbillingsFragment extends Fragment {

    @BindView(R.id.myEbillingsListView)
    ListView myEbillingsListView;
    @BindView(R.id.loading_my_ebilings)
    LinearLayout loadingMyEbillings;
    @BindView(R.id.label_no_result)
    TextView labelNoResult;

    EBillingService eBillingService;
    MyEbillingsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_ebillings, container, false);
        ButterKnife.bind(this, view);
        eBillingService = new EBillingService();
        consultarDatos();
        return view;
    }

    private void consultarDatos(){
        loadingMyEbillings.setVisibility(View.VISIBLE);
        final Observable<MyEBillingDTO[]> ebillingServiceMyEBillings = eBillingService.getMyEBillings();
        ebillingServiceMyEBillings
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<MyEBillingDTO[]>() {
                    @Override
                    public void onNext(MyEBillingDTO[] myEBillingDTOS) {
                        if(getContext()== null)return;
                        adapter = new MyEbillingsAdapter(getContext(), myEBillingDTOS);
                        myEbillingsListView.setAdapter(adapter);
                        if(myEBillingDTOS.length == 0){
                            labelNoResult.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getContext(), "ERROR: "+e.getMessage(), Toast.LENGTH_LONG).show();
                        loadingMyEbillings.setVisibility(View.GONE);
                    }

                    @Override
                    public void onComplete() {
                        loadingMyEbillings.setVisibility(View.GONE);
                        setActionListenerListView();
                    }
                });

    }

    private void setActionListenerListView(){
        myEbillingsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.getMenuInflater().inflate(R.menu.menu_facturacion_electronica_item_seleccionado, popup.getMenu());
                final MyEBillingDTO myEBillingDTO = (MyEBillingDTO) myEbillingsListView.getAdapter().getItem(position);
                if(myEBillingDTO.getState() != null && myEBillingDTO.getState().equals("AUTORIZADO")){
                    //popup.getMenu().findItem(R.id.enviar_SRI).setVisible(false); TODO si en el futuro se habilita re enviar al sri desde el celular
                } else {
                    popup.getMenu().findItem(R.id.enviar_RIDE).setVisible(false);
                }

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.enviar_RIDE) {
                            if(myEBillingDTO.getState() != null && myEBillingDTO.getState().equals("AUTORIZADO")){
                                popUpEnviarCorreo(myEBillingDTO.getClaveAcceso());
                            } else{
                                Toast.makeText(getContext(), "No se puede enviar correo porque no se encuentra en estado autorizado", Toast.LENGTH_LONG).show();
                            }

                        }
                        else if (item.getItemId() == R.id.enviar_SRI) {
                            //Toast.makeText(getContext(), "Enviar SRI", Toast.LENGTH_LONG).show(); TODO si en el futuro se habilita re enviar al sri desde el celular
                        }
                        return true;
                    }
                });

                popup.show();
                return true;
            }
        });
    }

    private void popUpEnviarCorreo(final String claveAcceso){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ingrese el correo electrónico");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText() == null || input.getText().toString().isEmpty() || !Utils.emailCheck(input.getText().toString())) {
                    Toast.makeText(getContext(), "Ingrese un correo válido", Toast.LENGTH_LONG).show();
                    dialog.cancel();
                    return;
                }

                    final Observable<Map<String, String>> getAdquiriente = eBillingService.sendMail(claveAcceso, input.getText().toString());
                getAdquiriente
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<Map<String, String>>() {
                            @Override
                            public void onNext(Map<String, String> response) {
                                if(getContext()== null)return;
                                Toast.makeText(getContext(), response.get("responseMessage"), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {}
                        });
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}
