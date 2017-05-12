package pt.cmov.locmess.locmess.adapter.locations.wifi;

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

public class LocationsWifiRVAdapter extends RecyclerView.Adapter<LocationsWifiRVAdapter.LocationsWifiAdapterViewHolder> {

    private static IRecyclerOnItemClickListener mItemClickListener;
    private List<LocationWifiData> locationsWifiDatas = new ArrayList<LocationWifiData>();
    private Fragment context;

    public LocationsWifiRVAdapter(List<LocationWifiData> myDataset){
        locationsWifiDatas = myDataset;
    }

    public void passFrag(Fragment context){
        this.context = context;
    }

    @Override
    public LocationsWifiAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.locations_wifi_list_items, parent, false);
        LocationsWifiAdapterViewHolder viewHolder = new LocationsWifiAdapterViewHolder(view);
        return viewHolder;
    }

    public LocationWifiData getLocationDataByPos(int pos){
        return locationsWifiDatas.get(pos);
    }

    @Override
    public void onBindViewHolder(LocationsWifiAdapterViewHolder holder, int position) {
        LocationWifiData md = getLocationDataByPos(position);

        holder.name.setText(md.getName());
        holder.macAddress.setText(md.getMacAddress());


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
        return locationsWifiDatas.size();
    }

    public static class LocationsWifiAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView name;
        public TextView macAddress;

        public LocationsWifiAdapterViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            macAddress = (TextView) view.findViewById(R.id.mac_address);

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
