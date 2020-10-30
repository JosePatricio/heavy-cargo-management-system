package ec.redcode.tas.on.android.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import ec.redcode.tas.on.android.R;
import ec.redcode.tas.on.android.activities.conductor.AceptedFleteDetailActivity;
import ec.redcode.tas.on.android.activities.conductor.GeneraFacturaFleteActivity;
import ec.redcode.tas.on.android.adapters.conductor.RowEmptyFleteAdapter;
import ec.redcode.tas.on.android.adapters.conductor.RowEntregaFacturaAdapter;
import ec.redcode.tas.on.android.adapters.conductor.RowGeneraFacturaFlete;
import ec.redcode.tas.on.android.adapters.conductor.RowWeightTimeAdapter;
import ec.redcode.tas.on.android.dto.InvoicesDTO;
import ec.redcode.tas.on.android.models.FleteShort;
import ec.redcode.tas.on.android.models.MyFletesListState;
import ec.redcode.tas.on.android.services.FacturaService;
import ec.redcode.tas.on.android.services.MyOfertListService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class FleteTabbedContainerFragment extends Fragment {

    //Services
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTableLayout;

    public FleteTabbedContainerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flete_tabbed_container, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = view.findViewById(R.id.container);
        mTableLayout = view.findViewById(R.id.tabs);
        mTableLayout.setupWithViewPager(mViewPager);
        setupViewPager(mViewPager);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        List<MyFletesListState> fletesLists = new ArrayList<>();

        MyFletesListState fletesListState;
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS, getString(R.string.my_oferts));
        fletesLists.add(fletesListState);
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS_APPROVED, getString(R.string.approved));
        fletesLists.add(fletesListState);
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS_TO_RECIEVE, getString(R.string.to_recieving));
        fletesLists.add(fletesListState);
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS_TO_DELIVER, getString(R.string.to_deliver));
        fletesLists.add(fletesListState);
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS_GENERATE_INVOICE, getString(R.string.por_generar_fact));
        fletesLists.add(fletesListState);
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS_DELIVER_INVOICE, getString(R.string.por_entregar_fact));
        fletesLists.add(fletesListState);
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS_COLLECT, getString(R.string.collect));
        fletesLists.add(fletesListState);
        fletesListState = new MyFletesListState(MyOfertListService.MY_OFERTS_FINISHED, getString(R.string.finished));
        fletesLists.add(fletesListState);

        for (MyFletesListState fletesList : fletesLists) {
            PlaceholderFragment frag = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(fletesList.ARG_FLETE_LIST_ID, fletesList.getCode());
            frag.setArguments(args);
            mSectionsPagerAdapter.addFragment(frag, fletesList.getTitle());
        }
        viewPager.setAdapter(mSectionsPagerAdapter);
    }


    public static class PlaceholderFragment extends Fragment implements RowWeightTimeAdapter.ClickListener, RowGeneraFacturaFlete.ClickListener,
            RowEntregaFacturaAdapter.ClickListener {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private RecyclerView mRecyclerView;
        private ProgressBar mProgressBar;
        private SwipeRefreshLayout mSwipeRefreshLayout;
        private MyOfertListService myOfertService;
        private FacturaService facturaService;

        public PlaceholderFragment() {
            myOfertService = new MyOfertListService();
            facturaService = new FacturaService();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_my_fletes, container, false);


            mProgressBar = rootView.findViewById(R.id.progressbar);

            mRecyclerView = rootView.findViewById(R.id.my_fletes_recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            mSwipeRefreshLayout = rootView.findViewById(R.id.refresh_layout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if ((getArguments().getString(MyFletesListState.ARG_FLETE_LIST_ID) != null)) {
                        askForFletes(getArguments().getString(MyFletesListState.ARG_FLETE_LIST_ID));
                    }
                }
            });

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            if ((getArguments().getString(MyFletesListState.ARG_FLETE_LIST_ID) != null)) {
                askForFletes(getArguments().getString(MyFletesListState.ARG_FLETE_LIST_ID));
            }
        }

        private void askForFletes(final String requestType) {
            Log.d("ENTRADA ",""+requestType);
            mSwipeRefreshLayout.setRefreshing(false);
            mProgressBar.setVisibility(View.VISIBLE);
            switch (requestType) {
                case MyOfertListService.MY_OFERTS_GENERATE_INVOICE:
                    RowGeneraFacturaFlete rowGeneraFacturaFlete = new RowGeneraFacturaFlete(this);
                    mRecyclerView.setAdapter(rowGeneraFacturaFlete);
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case MyOfertListService.MY_OFERTS_DELIVER_INVOICE:
                    Observable<List<InvoicesDTO>> invoicesObservable = facturaService.getInvoiceListByState(requestType);
                    invoicesObservable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<List<InvoicesDTO>>() {
                                @Override
                                public void onNext(List<InvoicesDTO> invoices) {
                                    //mProgressBar.setVisibility(View.GONE);
                                    fillAdapter(invoices);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    mProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onComplete() {
                                    //System.out.println("Completo");
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });
                    break;
                default:
                    Observable<List<FleteShort>> fleteShortObservable = myOfertService.getList(requestType);
                    fleteShortObservable
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<List<FleteShort>>() {
                                @Override
                                public void onNext(List<FleteShort> fleteList) {
                                    //mProgressBar.setVisibility(View.GONE);
                                    fillAdapter(fleteList);
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    //e.printStackTrace();
                                    mProgressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onComplete() {
                                    //System.out.println("Completo");
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            });
                    break;
            }
        }

        private void fillAdapter(List<?> list) {
            if (list.size() > 0) {
                if (list.get(0) instanceof FleteShort) {
                    RowWeightTimeAdapter myOfertsAdapter = new RowWeightTimeAdapter((List<FleteShort>) list, this);
                    mRecyclerView.setAdapter(myOfertsAdapter);
                } else if (list.get(0) instanceof InvoicesDTO) {
                    RowEntregaFacturaAdapter myOfertsAdapter = new RowEntregaFacturaAdapter((List<InvoicesDTO>) list, this);
                    mRecyclerView.setAdapter(myOfertsAdapter);
                }
            } else {
                ArrayList<FleteShort> emptyList = new ArrayList<>();
                emptyList.add(new FleteShort("0", "Busqueda no generó resultados...", ""));
                RowEmptyFleteAdapter rowEmptyFleteAdapter = new RowEmptyFleteAdapter(emptyList);
                mRecyclerView.setAdapter(rowEmptyFleteAdapter);
            }
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onClick(Object objEnt) {
            if (objEnt instanceof FleteShort) {
                FleteShort flete = (FleteShort) objEnt;
                if (flete.getId() == null && flete.getOriginCity().equals(RowGeneraFacturaFlete.GEN_FACT)) {
                    Intent intent = new Intent(getContext(), GeneraFacturaFleteActivity.class);
                    if (flete.getDestinationCity().equals(RowGeneraFacturaFlete.FACT_RAPIDA))
                        intent.putExtra(GeneraFacturaFleteActivity.PREFACT, RowGeneraFacturaFlete.FACT_RAPIDA);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getContext(), AceptedFleteDetailActivity.class);
                    intent.putExtra(AceptedFleteDetailActivity.MY_FLETE, flete.getId());
                    startActivity(intent);
                }
            } else if (objEnt instanceof InvoicesDTO) {
                Intent intent = new Intent(getContext(), GeneraFacturaFleteActivity.class);
                intent.putExtra(GeneraFacturaFleteActivity.ID_PREFACT, ((InvoicesDTO) objEnt).getInvoiceId());
                startActivity(intent);
            }
        }

        private List<FleteShort> fillFletesList(int size) {
            List<FleteShort> fletes = new ArrayList<>();

            String[] mTestArray;
            mTestArray = getResources().getStringArray(R.array.cities);
            for (int i = 0; i < size; i++) {
                int n = (int) (Math.random() * 220);
                FleteShort flete = new FleteShort();
                flete.setOriginCity(mTestArray[n]);
                n = (int) (Math.random() * 220);
                flete.setDestinationCity(mTestArray[n]);
                n = (int) (Math.random() * 150);
                flete.setWeight(n);
                //long date = System.currentTimeMillis() + (int) (Math.random() * 100000000);
                //flete.setDeliveryDate(date);
                flete.setDays((int) (Math.random() * 10));
                fletes.add(flete);
            }
            return fletes;
        }

    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        private void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
