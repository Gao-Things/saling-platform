package comp5703.sydney.edu.au.learn.Home.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.DTO.TopSearch;
import comp5703.sydney.edu.au.learn.Home.Adapter.ItemListAdapter;
import comp5703.sydney.edu.au.learn.Home.Adapter.TopSearchListAdapter;
import comp5703.sydney.edu.au.learn.R;

public class SearchFragment extends Fragment {


    private FlexboxLayout flexboxLayout;

    private RecyclerView topSearch;

    private RecyclerView searchResult;

    private TopSearchListAdapter topSearchListAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        flexboxLayout = view.findViewById(R.id.flexLayout);
        topSearch = view.findViewById(R.id.topSearch);
        searchResult = view.findViewById(R.id.searchResult);

        // 当有新的搜索记录时
        String searchRecord = "New Tag";
        String searchRecord2 = "New Tag testtx";
        for (int i =0; i<5;i++){
            TextView textView = new TextView(getContext());
            textView.setText(searchRecord);
            textView.setBackgroundResource(R.drawable.rounded_search_record);

            // 设置外边距
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 15, 10, 15);  // 设置左、上、右、下的边距

            textView.setLayoutParams(params);
            flexboxLayout.addView(textView);
        }


        for (int i =0; i<5;i++){
            TextView textView = new TextView(getContext());
            textView.setText(searchRecord2);
            textView.setBackgroundResource(R.drawable.rounded_search_record);

            // 设置外边距来增加标签之间的空隙
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(10, 15, 10, 15);  // 设置左、上、右、下的边距

            textView.setLayoutParams(params);

            flexboxLayout.addView(textView);
        }



        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        topSearch.setLayoutManager(layoutManager);




        // 创建并设置RecyclerView的Adapter
        topSearchListAdapter = new TopSearchListAdapter(getContext(),new ArrayList<TopSearch>(),clickListener);
        topSearch.setAdapter(topSearchListAdapter);

        getTopSearchResult();

    }

    @SuppressLint("NotifyDataSetChanged")
    private void getTopSearchResult() {

        List<TopSearch> topSearchList = new ArrayList<>();

        TopSearch topSearch1 = new TopSearch("24K gold cheap", 1000);
        TopSearch topSearch2 = new TopSearch("24K sliver cheap", 933);
        TopSearch topSearch3 = new TopSearch("23K gold cheap", 888);
        TopSearch topSearch4 = new TopSearch("22K gold cheap", 777);
        TopSearch topSearch5 = new TopSearch("23K gold aaa", 345);
        TopSearch topSearch6 = new TopSearch("cascas", 567);
        topSearchList.add(topSearch1);
        topSearchList.add(topSearch2);
        topSearchList.add(topSearch3);
        topSearchList.add(topSearch4);
        topSearchList.add(topSearch5);
        topSearchList.add(topSearch6);

        topSearchList.add(topSearch1);
        topSearchList.add(topSearch2);
        topSearchList.add(topSearch3);
        topSearchList.add(topSearch4);
        topSearchList.add(topSearch5);
        topSearchList.add(topSearch6);

        topSearchListAdapter.setRecordList(topSearchList);
        topSearchListAdapter.notifyDataSetChanged();

    }


    TopSearchListAdapter.OnItemClickListener clickListener = new TopSearchListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, String topContent) {


        }
    };


}
