package ec.redcode.tas.on.android.adapters;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import ec.redcode.tas.on.android.models.City;
import ec.redcode.tas.on.android.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyAddCityRecyclerViewAdapter extends RecyclerView.Adapter<MyAddCityRecyclerViewAdapter.ViewHolder> {

    private Context myContext;
    private List<City> mValues;
    private ClickListener clickListener;

    public MyAddCityRecyclerViewAdapter(Context context, List<City> items, ClickListener clickListener) {
        myContext = context;
        mValues = items;
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_add_city, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues.get(holder.getAdapterPosition()).isLoading()){
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.mImageAdd.setVisibility(View.GONE);
        }
        else {
            holder.progressBar.setVisibility(View.GONE);
            holder.mImageAdd.setVisibility(View.VISIBLE);
        }

        holder.mCity.setText(mValues.get(holder.getAdapterPosition()).getName());
        Glide.with(myContext).load(mValues.get(holder.getAdapterPosition()).getFlagUrl().toString()).into(holder.mFlagImage);

        holder.mCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.progressBar.setVisibility(View.VISIBLE);
                clickListener.onClick(mValues.get(holder.getAdapterPosition()));
            }
        });



        if (mValues.get(holder.getAdapterPosition()).isInteresting()){
            holder.mImageAdd.setBackground(ContextCompat.getDrawable(myContext, R.drawable.ic_check_circle_black_24dp));
        }
        else {
            holder.mImageAdd.setBackground(ContextCompat.getDrawable(myContext, R.drawable.ic_add_circle_red_24dp));
        }
    }

    public void setItemLoading (String cityId, boolean isLoading){
        for (int i = 0; i < mValues.size() ; i++ ){
            if (mValues.get(i).getCode().equals(cityId)){
                mValues.get(i).setLoading(isLoading);
                notifyItemChanged(i);
                return;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mCityLayout;
        private CircleImageView mFlagImage;
        private TextView mCity;
        private ImageView mImageAdd;
        private ProgressBar progressBar;

        public ViewHolder(View view) {
            super(view);
            mCityLayout = view.findViewById(R.id.linear_layout_city_row);
            mFlagImage = view.findViewById(R.id.img_city);
            mCity = view.findViewById(R.id.city_name);
            mImageAdd = view.findViewById(R.id.img_add);
            progressBar = view.findViewById(R.id.progress_add_city);
        }
    }

    public void updateList(List<City> list){
        mValues = list;
        notifyDataSetChanged();
    }

    public interface ClickListener {
        void onClick(City city);
    }
}

