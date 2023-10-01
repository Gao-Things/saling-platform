package comp5703.sydney.edu.au.learn.Home.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.Home.Adapter.MyOfferListAdapter;
import comp5703.sydney.edu.au.learn.R;

public class MyOfferFragment extends Fragment {

    private RecyclerView itemRecyclerView;


    private MyOfferListAdapter myOfferListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_offer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemRecyclerView = view.findViewById(R.id.list_main);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemRecyclerView.setLayoutManager(layoutManager);

        // 创建并设置RecyclerView的Adapter
        myOfferListAdapter = new MyOfferListAdapter(getContext(),new ArrayList<Record>(),clickListener);
        itemRecyclerView.setAdapter(myOfferListAdapter);


    }

    MyOfferListAdapter.OnItemClickListener clickListener = new MyOfferListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer itemId) {

        }
    };
}
