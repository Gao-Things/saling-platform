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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.Common.DialogFragment;
import comp5703.sydney.edu.au.learn.DTO.ProductUser;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.Home.Adapter.ItemListAdapter;
import comp5703.sydney.edu.au.learn.Home.DialogFragment.FilterDialogFragment;
import comp5703.sydney.edu.au.learn.Home.DialogFragment.SortDialogFragment;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ProductFilter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ItemListFragment extends Fragment{

    private RecyclerView itemRecyclerView;

    private List<Record> recordList;

    private ItemListAdapter itemListAdapter;

    private Fragment itemDetailFragment;

    private Fragment searchFragment;

    private EditText searchBox;

    private ImageView filterIcon;

    private ImageView sortIcon;

    private Integer sortByPurityNew = -1;

    private Integer sortByWeightNew = -1;


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
        sortIcon = view.findViewById(R.id.sortIcon);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemRecyclerView.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        itemListAdapter = new ItemListAdapter(getContext(),new ArrayList<ProductUser>(),clickListener);
        itemRecyclerView.setAdapter(itemListAdapter);

        searchBox.setOnClickListener(this::dumpToSearchFragment);
        filterIcon.setOnClickListener(this::openFilterFragment);
        sortIcon.setOnClickListener(this::showSortFragment);


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

                // 获取 SharedPreferences.Editor 对象以进行修改
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("FilterPreferences", Context.MODE_PRIVATE).edit();

                // 将过滤器的值保存到 SharedPreferences，用于filter
                editor.putInt("category", category);
                editor.putInt("status", status);
                editor.putString("purity", purity);
                editor.putFloat("minPrice", (float) minPrice); // SharedPreferences不支持直接存储double值
                editor.putFloat("maxPrice", (float) maxPrice); // 因此我们将double值转换为float

                // 应用更改
                editor.apply(); // 使用apply()而不是commit()，因为apply()是异步的且不返回成功状态


                getRecordList(category, purity, status, minPrice, maxPrice);

            }
        });

        filterDialogFragment.show(getChildFragmentManager(), "filterDialog");


    }


    // 弹出排序对话框
    private void showSortFragment(View view) {

        // 创建并显示筛选弹窗
        SortDialogFragment sortDialogFragment = new SortDialogFragment();

        // 设置监听器
        sortDialogFragment.setSortDialogListener(new SortDialogFragment.SortDialogListener(){
            @Override
            public void onSortClosed() {

            }

            @Override
            public void onSortApplied(Integer sortByPurity, Integer sortByWeight) {
                SharedPreferences preferences = getActivity().getSharedPreferences("FilterPreferences", Context.MODE_PRIVATE);
                int savedCategoryOld = preferences.getInt("category", -1); // 默认值为-1，表示未选中任何按钮

                String savedPurityOld = preferences.getString("purity", null);

                int saveStatusOld = preferences.getInt("status", -1); // 默认值为-1，表示未选中任何按钮
                double minPriceOld = preferences.getFloat("minPrice", 0f);
                double maxPriceOld = preferences.getFloat("maxPrice", 0f);

                getRecordList(savedCategoryOld, savedPurityOld, saveStatusOld, minPriceOld, maxPriceOld);

                sortByPurityNew = sortByPurity;
                sortByWeightNew = sortByWeight;

            }


        });

        sortDialogFragment.show(getChildFragmentManager(), "filterDialog");

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

        // 更新Activity中的Toolbar
        ((HomeUseActivity) Objects.requireNonNull(getActivity())).updateToolbar(true, "Search");
    }


    private void getRecordList(int category, @Nullable String purity, int status, double minPrice, double maxPrice){

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

        // 定义一个Comparator，它可以处理null值，将它们视为最小值
        Comparator<ProductUser> weightComparator = Comparator
                .comparing(ProductUser::getProductWeight, Comparator.nullsFirst(Double::compare));

        if (sortByWeightNew == 1) {
            // 降序排序，如果需要将null视为最大值则使用nullsLast
            recordsListUse.sort(weightComparator.reversed());
        } else if (sortByWeightNew == 0) {
            // 升序排序，如果需要将null视为最小值则使用nullsFirst
            recordsListUse.sort(weightComparator);
        }


        // 定义一个Comparator，它可以处理null值，将它们视为最小值
        Comparator<ProductUser> TimeComparator = Comparator
                .comparing(ProductUser::getProductCreateTime, Comparator.nullsFirst(Long::compare));

        if (sortByPurityNew == 1) {
            // 降序排序，如果需要将null视为最大值则使用nullsLast
            recordsListUse.sort(TimeComparator.reversed());
        } else if (sortByPurityNew == 0) {
            // 升序排序，如果需要将null视为最小值则使用nullsFirst
            recordsListUse.sort(TimeComparator);
        }




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

            // 更新Activity中的Toolbar
            ((HomeUseActivity) Objects.requireNonNull(getActivity())).updateToolbar(true, "Product Detail");

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        itemRecyclerView = null;
        searchBox = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(true, "Home");
    }



}
