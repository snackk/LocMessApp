package pt.cmov.locmess.locmess.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pt.cmov.locmess.locmess.R;

/**
 * Created by snackk on 08/05/2017.
 */

public class MessagesRVAdapter extends RecyclerView.Adapter<MessagesRVAdapter.MessagesAdapterViewHolder> {
    private List<MessageData> messageDatas = new ArrayList<MessageData>();

    public MessagesRVAdapter(List<MessageData> myDataset)
    {
        messageDatas = myDataset;
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

    }

    @Override
    public int getItemCount() {
        return messageDatas.size();
    }

    public static class MessagesAdapterViewHolder extends RecyclerView.ViewHolder {
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
        }
    }
}
