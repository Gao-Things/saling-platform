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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.Common.CustomDividerItemDecoration;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.Search;
import comp5703.sydney.edu.au.learn.DTO.TopSearch;
import comp5703.sydney.edu.au.learn.Home.Adapter.SearchResultListAdapter;
import comp5703.sydney.edu.au.learn.Home.Adapter.TopSearchListAdapter;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.SearchHistoryParameter;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.util.DialogUtil;
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

    private TextView cancelButton;

    private SearchResultListAdapter searchResultListAdapter;

    private LinearLayout searchTopText;
    private LinearLayout searchHistoryText;

    private Integer userId;
    private String token;

    private ItemDetailFragment itemDetailFragment;

    private String keyWord;

    private ImageView deleteHistory;

    private View rootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);

        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);
        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        // 获取用户搜索历史
        getUserSearchHistory();

        return rootView;
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        flexboxLayout = view.findViewById(R.id.flexLayout);
        topSearch = view.findViewById(R.id.topSearch);
        searchResult = view.findViewById(R.id.searchResult);

        searchBox = view.findViewById(R.id.searchBox);
        cancelButton = view.findViewById(R.id.cancelButton);

        searchTopText = view.findViewById(R.id.searchTopText);
        searchHistoryText = view.findViewById(R.id.searchHistoryText);
        deleteHistory = view.findViewById(R.id.deleteHistory);



        searchBox.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (searchBox.getRight() - searchBox.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // 清空文本
                        searchBox.setText("");
                        return true;
                    }
                }
                return false;
            }
        });


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
                    keyWord = s.toString();
                }
            }
        });




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


        /**
         * 获取Top搜索
         */
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

        deleteHistory.setOnClickListener(this::deleteHistory);
        cancelButton.setOnClickListener(this::backToHome);
    }

    private void backToHome(View view) {
        Objects.requireNonNull(getActivity()).onBackPressed();
    }

    private void deleteHistory(View view) {

        DialogUtil.showCustomDialog(
                Objects.requireNonNull(getActivity()),
                getContext(),
                "Do you confirm to delete ?",
                v -> {

                    // 清空历史记录
                    flexboxLayout.removeAllViews();

                    // Handle the logic for delete
                    userIdVO userIdVO = new userIdVO();
                    userIdVO.setUserId(userId);

                    NetworkUtils.getWithParamsRequest(userIdVO, "/search/deleteHistoryByUserId",token, new Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            handleFailure(e);
                        }
                    });

                },null
        );


    }

    // 获取用户搜索历史
    private void getUserSearchHistory() {
        userIdVO userIdVO = new userIdVO();
        userIdVO.setUserId(userId);

        NetworkUtils.getWithParamsRequest(userIdVO, "/search/getSearchHistoryByUserId",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse2(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });
    }

    private void handleResponse2(Response response) throws IOException {

        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        // 提取 product 数组并转换为List
        JSONArray messageArray = jsonObject.getJSONArray("data");

        if (code == 200) {

            List<Search> searchHistoryList = messageArray.toJavaList(Search.class);
            // 通知adapter数据更新
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    if (!searchHistoryList.isEmpty()){

                        for (Search ss: searchHistoryList){
                            TextView textView = new TextView(getContext());
                            textView.setText(ss.getSearchContent());
                            textView.setBackgroundResource(R.drawable.rounded_search_record);

                            // 设置外边距
                            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT, FlexboxLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(10, 15, 10, 15);  // 设置左、上、右、下的边距

                            textView.setLayoutParams(params);


                            // 为TextView设置点击监听器
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // 将TextView的文本内容设置到EditText中
                                    searchBox.setText(((TextView) v).getText().toString());
                                    // 可选：如果您希望EditText获得焦点并弹出键盘
                                    searchBox.requestFocus();
                                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(searchBox, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });



                            flexboxLayout.addView(textView);
                        }
                    }
                }
            });


        } else {
            Log.d(TAG, "errowwwwwww");
        }

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

        if (code == 200) {

            List<Product> productList = messageArray.toJavaList(Product.class);
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

        // 发送请求获取top搜索记录

        NetworkUtils.getWithParamsRequest( null,"/public/product/getProductList/top10Products",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(responseBody);
                int code = jsonObject.getIntValue("code");

                // 提取 product 数组并转换为List
                JSONArray messageArray = jsonObject.getJSONArray("data");

                if (code == 200) {

                    List<Product> productList = messageArray.toJavaList(Product.class);
                    for (Product pp : productList){
                        TopSearch topSearch = new TopSearch(pp.getId(), pp.getProductName(), pp.getSearchCount());
                        topSearchList.add(topSearch);
                    }
                    // 通知adapter数据更新
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void run() {

                            topSearchListAdapter.setRecordList(topSearchList);
                            topSearchListAdapter.notifyDataSetChanged();

                        }
                    });


                } else {
                    Log.d(TAG, "errowwwwwww");
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });


    }


    TopSearchListAdapter.OnItemClickListener clickListener = new TopSearchListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer productId, String productTitle) {

            saveToSearchHistory(productId, productTitle);

            // 跳转到商品详情页面
            // jump to item detail
            if (itemDetailFragment == null){
                itemDetailFragment = new ItemDetailFragment();
            }
            // 准备要传递的数据
            Bundle args = new Bundle();
            args.putInt("productId", productId); // 这里的 "key" 是传递数据的键名，"value" 是要传递的值
            itemDetailFragment.setArguments(args);

            // 执行 Fragment 跳转
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, itemDetailFragment); // R.id.fragment_container 是用于放置 Fragment 的容器
            transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
            transaction.commitAllowingStateLoss();

        }
    };


    SearchResultListAdapter.OnItemClickListener clickListener2 = new SearchResultListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer productId) {

            saveToSearchHistory(productId, keyWord);

            // 跳转到商品详情页面
            // jump to item detail
            if (itemDetailFragment == null){
                itemDetailFragment = new ItemDetailFragment();
            }
            // 准备要传递的数据
            Bundle args = new Bundle();
            args.putInt("productId", productId); // 这里的 "key" 是传递数据的键名，"value" 是要传递的值
            itemDetailFragment.setArguments(args);

            // 执行 Fragment 跳转
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, itemDetailFragment); // R.id.fragment_container 是用于放置 Fragment 的容器
            transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
            transaction.commitAllowingStateLoss();


        }
    };

    private void saveToSearchHistory(Integer productId, String keyWord){
        SearchHistoryParameter searchHistoryParameter = new SearchHistoryParameter();
        searchHistoryParameter.setSearchTime(System.currentTimeMillis());
        searchHistoryParameter.setProductId(productId);
        searchHistoryParameter.setUserId(userId);
        searchHistoryParameter.setSearchContent(keyWord);
        // 存入搜索历史
        NetworkUtils.postJsonRequest(searchHistoryParameter,"/search/saveSearchHistory",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(true, "Search");
    }



}
