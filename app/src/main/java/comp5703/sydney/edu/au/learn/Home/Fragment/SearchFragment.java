package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.flexbox.FlexboxLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.Common.CustomDividerItemDecoration;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.TopSearch;
import comp5703.sydney.edu.au.learn.Home.Adapter.SearchResultListAdapter;
import comp5703.sydney.edu.au.learn.Home.Adapter.TopSearchListAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchFragment extends Fragment {


    private FlexboxLayout flexboxLayout;

    private RecyclerView topSearch;

    private RecyclerView searchResult;

    private TopSearchListAdapter topSearchListAdapter;

    private TextView searchBox;

    private SearchResultListAdapter searchResultListAdapter;

    private LinearLayout searchTopText;
    private LinearLayout searchHistoryText;

    private Integer userId;
    private String token;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);
        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        flexboxLayout = view.findViewById(R.id.flexLayout);
        topSearch = view.findViewById(R.id.topSearch);
        searchResult = view.findViewById(R.id.searchResult);
        searchBox = view.findViewById(R.id.search_box);
        searchTopText = view.findViewById(R.id.searchTopText);
        searchHistoryText = view.findViewById(R.id.searchHistoryText);


        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 此方法会在文本发生变化之前被调用
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 此方法会在文本发生变化时被调用
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 此方法会在文本发生变化之后被调用
                if (s.length() == 0) {
                    // 文本已被清空
                    onTextCleared();
                } else {
                    // 其他情况，如执行搜索逻辑
                    performSearch(s.toString());
                }
            }
        });


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
        topSearch.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        // 创建并设置RecyclerView的Adapter
        topSearchListAdapter = new TopSearchListAdapter(getContext(),new ArrayList<TopSearch>(),clickListener);
        topSearch.setAdapter(topSearchListAdapter);
        CustomDividerItemDecoration decoration1 = new CustomDividerItemDecoration(26); // 16是边距，可以根据需要调整
        topSearch.addItemDecoration(decoration1);


        getTopSearchResult();


        /**
         * 绑定搜索结果的recyclerView
         */

        // 创建并设置RecyclerView的Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        searchResult.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        searchResultListAdapter = new SearchResultListAdapter(getContext(),new ArrayList<Product>(),clickListener2);
        searchResult.setAdapter(searchResultListAdapter);

        CustomDividerItemDecoration decoration = new CustomDividerItemDecoration(26); // 16是边距，可以根据需要调整
        searchResult.addItemDecoration(decoration);



    }

    private void onTextCleared() {
        flexboxLayout.setVisibility(View.VISIBLE);
        topSearch.setVisibility(View.VISIBLE);
        searchTopText.setVisibility(View.VISIBLE);
        searchHistoryText.setVisibility(View.VISIBLE);
        searchResult.setVisibility(View.GONE);
    }

    // 发送请求，获取搜索结果
    private void performSearch(String query){
        flexboxLayout.setVisibility(View.GONE);
        topSearch.setVisibility(View.GONE);
        searchTopText.setVisibility(View.GONE);
        searchHistoryText.setVisibility(View.GONE);
        searchResult.setVisibility(View.VISIBLE);


        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("userInput", query);

        // 传入搜索值
        searchResultListAdapter.setSearchQuery(query);

        NetworkUtils.getWithParamsRequest( requestMap, "/search/getSearchResultByInput",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });


    }

    private void handleFailure(IOException e) {
        Log.d("请求失败", String.valueOf(e));
    }

    private void handleResponse(Response response) throws IOException {

        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        // 提取 product 数组并转换为List
        JSONArray messageArray = jsonObject.getJSONArray("data");

        List<Product> productList = messageArray.toJavaList(Product.class);

        if (code == 200) {

            // 通知adapter数据更新
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    // 更新Adapter的数据
                    searchResultListAdapter.setRecordList(productList);
                    // 在UI线程上更新Adapter的数据
                    searchResultListAdapter.notifyDataSetChanged();
                }
            });


        } else {
            Log.d(TAG, "errowwwwwww");
        }
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


    SearchResultListAdapter.OnItemClickListener clickListener2 = new SearchResultListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, String topContent) {


        }
    };


}
