package pt.cmov.locmess.locmess.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

import pt.cmov.locmess.locmess.R;
import pt.cmov.locmess.locmess.adapter.recycleview_adapter;

public class MessagesSentFragment extends Fragment {

    public static final String[] FruiteList = {"Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate","Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate","Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate","Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate","Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate","Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate",
            "Apple", "Orange", "Mango", "Grapes", "Jackfruit","pomegranate" };

    List fruiteslist = Arrays.asList(FruiteList);
    public MessagesSentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_messages_sent, container, false);
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_messages_sent, container, false);
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));

        rv.setAdapter(new recycleview_adapter(fruiteslist));
        return rv;
    }
}
