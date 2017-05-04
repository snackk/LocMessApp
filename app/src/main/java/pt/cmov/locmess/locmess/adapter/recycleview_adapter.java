package pt.cmov.locmess.locmess.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pt.cmov.locmess.locmess.R;

/**
 * Created by snackk on 04/05/2017.
 */

public class recycleview_adapter extends RecyclerView.Adapter<recycleview_adapter.ViewHolder> {

    List<String> mFruiteList;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;

        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            //mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(R.id.id_text_view);
        }
    }

    public recycleview_adapter(List<String> items) {

        mFruiteList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_messages_sent, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mTextView.setText(mFruiteList.get(position));
    }

    @Override
    public int getItemCount() {
        return mFruiteList.size();
    }
}
