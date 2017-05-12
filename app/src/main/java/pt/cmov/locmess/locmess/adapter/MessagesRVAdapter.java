package pt.cmov.locmess.locmess.adapter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.fragments.messages.MessagesReadFragment;

/**
 * Created by snackk on 08/05/2017.
 */

public class MessagesRVAdapter extends RecyclerView.Adapter<MessagesRVAdapter.MessagesAdapterViewHolder> {

    private static IRecyclerOnItemClickListener mItemClickListener;
    private List<MessageData> messageDatas = new ArrayList<MessageData>();
    private Fragment context;

    public MessagesRVAdapter(Fragment context, List<MessageData> myDataset)
    {
        messageDatas = myDataset;
        this.context = context;

    }

    @Override
    public MessagesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list_items, parent, false);
        MessagesAdapterViewHolder viewHolder = new MessagesAdapterViewHolder(view);
        return viewHolder;
    }

    public MessageData getMessageDataByPos(int pos){
        return messageDatas.get(pos);
    }

    @Override
    public void onBindViewHolder(MessagesAdapterViewHolder holder, int position) {
        MessageData md = getMessageDataByPos(position);

        holder.user.setText(md.getUser());
        holder.message.setText(md.getMessage());
        holder.timeStamp.setText(md.getTimeStamp());
        holder.location.setText(md.getLocation());

        mItemClickListener = new IRecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View childView, int position) {
                Bundle args = new Bundle();
                args.putInt("message_pos",position);

                Fragment fragment = new MessagesReadFragment();
                fragment.setArguments(args);
                FragmentTransaction fragmentTransaction = context.getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.app_content, fragment).commit();
            }
        };
    }

    @Override
    public int getItemCount() {
        return messageDatas.size();
    }

    public static class MessagesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView user;
        public TextView message;
        public TextView timeStamp;
        public TextView location;

        public MessagesAdapterViewHolder(View view) {
            super(view);
            user = (TextView) view.findViewById(R.id.user);
            message = (TextView) view.findViewById(R.id.message);
            timeStamp = (TextView) view.findViewById(R.id.timestamp);
            location = (TextView) view.findViewById(R.id.location);
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
