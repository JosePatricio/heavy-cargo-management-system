package ec.redcode.tas.on.android.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.redcode.tas.on.android.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndCondtitionsFragment extends Fragment {
    @BindView(R.id.terms_text)
    TextView mTermsText;

    public TermsAndCondtitionsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_and_condtitions, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Required empty public constructor
        Spanned htmlAsSpanned = Html.fromHtml(getString(R.string.Terms_and_conditions_cont));
        mTermsText.setText(htmlAsSpanned);
    }

}
