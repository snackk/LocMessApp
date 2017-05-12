package pt.cmov.locmess.locmess.adapter.locations.gps;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.IRecyclerOnItemClickListener;

/**
 * Created by snackk on 12/05/2017.
 */

public class LocationsGpsRVAdapter extends RecyclerView.Adapter<LocationsGpsRVAdapter.LocationsGpsAdapterViewHolder> {

    private static IRecyclerOnItemClickListener mItemClickListener;
    private List<LocationGpsData> locationsGpsDatas = new ArrayList<LocationGpsData>();
    private Fragment context;

    public LocationsGpsRVAdapter(List<LocationGpsData> myDataset){
        locationsGpsDatas = myDataset;
    }

    public void passFrag(Fragment context){
        this.context = context;
    }

    @Override
    public LocationsGpsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_gps_list_items, parent, false);
        LocationsGpsAdapterViewHolder viewHolder = new LocationsGpsAdapterViewHolder(view);
        return viewHolder;
    }

    public LocationGpsData getLocationDataByPos(int pos){
        return locationsGpsDatas.get(pos);
    }

    @Override
    public void onBindViewHolder(LocationsGpsAdapterViewHolder holder, int position) {
        LocationGpsData md = getLocationDataByPos(position);

        holder.name.setText(md.getName());
        holder.latitude.setText(md.getLatitude());
        holder.longitude.setText(md.getLongitude());
        holder.radius.setText(md.getRadius() + "");

        mItemClickListener = new IRecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                 /* Bundle args = new Bundle();
                args.putInt("location_pos", position);

                Fragment fragment = new MessagesReadFragment();
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = context.getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_content, fragment).commit();*/
            }
        };
    }

    @Override
    public int getItemCount() {
        return locationsGpsDatas.size();
    }

    public static class LocationsGpsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView latitude;
        public TextView longitude;
        public TextView radius;

        public LocationsGpsAdapterViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            latitude = (TextView) view.findViewById(R.id.latitude);
            longitude = (TextView) view.findViewById(R.id.longitude);
            radius = (TextView) view.findViewById(R.id.radius);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null)
            {
                mItemClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }
}
