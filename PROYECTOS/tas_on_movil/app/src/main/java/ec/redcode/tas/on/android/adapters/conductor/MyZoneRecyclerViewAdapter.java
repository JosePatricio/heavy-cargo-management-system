package ec.redcode.tas.on.android.adapters.conductor;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import ec.redcode.tas.on.android.models.City;
import ec.redcode.tas.on.android.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyZoneRecyclerViewAdapter extends RecyclerView.Adapter<MyZoneRecyclerViewAdapter.ViewHolder> {

    private final List<City> mValues;
    private Context myContext;
    private myZoneAdapterListener mListener;

    public MyZoneRecyclerViewAdapter(List<City> cities, Context context, myZoneAdapterListener listener) {
        mValues = cities;
        myContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_zone, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Glide.with(myContext).load(mValues.get(holder.getAdapterPosition()).getFlagUrl().toString()).into(holder.mImagFlag);
        holder.mZoneName.setText(mValues.get(position).getName());
        holder.mImageButtonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu( holder.mImageButtonMore, mValues.get(holder.getAdapterPosition()));
            }
        });

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

    private void showPopupMenu(View view, final City city) {
        PopupMenu popup = new PopupMenu(myContext, view);
        popup.getMenuInflater().inflate(R.menu.my_zone_menu, popup.getMenu());
        popup.show();
        popup.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                mListener.onClickDelete(city);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private CircleImageView mImagFlag;
        private TextView mZoneName;

//        private CheckBox mCheckBox;
        private ImageButton mImageButtonMore;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImagFlag = view.findViewById(R.id.img_flag);
            mZoneName = view.findViewById(R.id.zone_name);
//            mCheckBox = view.findViewById(R.id.checkbox);
            mImageButtonMore = view.findViewById(R.id.imagebutton_more);
        }
    }

    public interface myZoneAdapterListener{
        void onClickDelete (City city);
    }
}
