package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.ProductUser;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.Home.Adapter.ItemListAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ProductFilter;
import comp5703.sydney.edu.au.learn.VO.productListParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ItemListFragment extends Fragment {

    private RecyclerView itemRecyclerView;

    private List<Record> recordList;

    private ItemListAdapter itemListAdapter;

    private Fragment itemDetailFragment;

    private Fragment searchFragment;

    private EditText searchBox;

    private ImageView filterIcon;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        // get recordList By request backend
        getRecordList(-1, null, -1, 0, 0);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        itemRecyclerView = view.findViewById(R.id.list_main);
        searchBox = view.findViewById(R.id.search_box);
        filterIcon = view.findViewById(R.id.filterIcon);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemRecyclerView.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        itemListAdapter = new ItemListAdapter(getContext(),new ArrayList<ProductUser>(),clickListener);
        itemRecyclerView.setAdapter(itemListAdapter);

        searchBox.setOnClickListener(this::dumpToSearchFragment);
        filterIcon.setOnClickListener(this::openFilterFragment);


    }

    private void openFilterFragment(View view) {
        // 创建并显示筛选弹窗
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();

        // 设置监听器
        filterDialogFragment.setFilterDialogListener(new FilterDialogFragment.FilterDialogListener() {
            @Override
            public void onFilterClosed() {

            }

            @Override
            public void onFilterApplied(int category, @Nullable String purity, int status, double minPrice, double maxPrice) {

                getRecordList(category, purity, status, minPrice, maxPrice);

            }
        });

        filterDialogFragment.show(getChildFragmentManager(), "filterDialog");


    }

    private void dumpToSearchFragment(View view) {
        // jump to item detail
        if (searchFragment == null){
            searchFragment = new SearchFragment();
        }

        // 执行 Fragment 跳转
        assert getFragmentManager() != null;
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, searchFragment); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commitAllowingStateLoss();
    }


    private void getRecordList(int category, @Nullable String purity, int status, double minPrice, double maxPrice ){

        ProductFilter productFilter = new ProductFilter();
        if (category != -1){
            productFilter.setCategory(category);
        }

        if (purity!= null){
            productFilter.setPurity(purity);
        }

        if (status != -1){
            productFilter.setStatus(status);
        }

        productFilter.setMinPrice(minPrice);
        productFilter.setMaxPrice(maxPrice);

        NetworkUtils.getWithParamsRequest(productFilter,"/public/product/productList",null, new Callback() {
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


    @SuppressLint("NotifyDataSetChanged")
    private void handleResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");
        JSONArray recordsArray = jsonObject.getJSONArray("data");

        List<ProductUser> recordsListUse = recordsArray.toJavaList(ProductUser.class);

        if (code == 200) {

           // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 更新Adapter的数据
                    itemListAdapter.setRecordList(recordsListUse);
                    // 在UI线程上更新Adapter的数据
                    itemListAdapter.notifyDataSetChanged();
                }
            });


        } else {
           Log.d(TAG, "errowwwwwww");
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }


    ItemListAdapter.OnItemClickListener clickListener = new ItemListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer productID) {
            // jump to item detail
            if (itemDetailFragment == null){
                itemDetailFragment = new ItemDetailFragment();
            }
            // 准备要传递的数据
            Bundle args = new Bundle();
            args.putInt("productId", productID); // 这里的 "key" 是传递数据的键名，"value" 是要传递的值
            itemDetailFragment.setArguments(args);

            // 执行 Fragment 跳转
            assert getFragmentManager() != null;
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_container, itemDetailFragment); // R.id.fragment_container 是用于放置 Fragment 的容器
            transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
            transaction.commitAllowingStateLoss();

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        itemRecyclerView = null;
        searchBox = null;
    }


}
