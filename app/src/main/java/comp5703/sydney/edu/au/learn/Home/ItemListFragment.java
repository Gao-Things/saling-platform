package comp5703.sydney.edu.au.learn.Home;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.productParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ItemListFragment extends Fragment {

    private RecyclerView itemRecyclerView;

    private List<Record> recordList;

    private ItemListAdapter itemListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        // get recordList By request backend
        getRecordList();

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
        itemListAdapter = new ItemListAdapter(getContext(),new ArrayList<Record>(),clickListener);
        itemRecyclerView.setAdapter(itemListAdapter);

        // 添加分隔线装饰
        itemRecyclerView.addItemDecoration(new myDecoration());

    }


    private void getRecordList(){
        productParameter productParameter = new productParameter();
        productParameter.setPageNum(1);
        productParameter.setPageSize(20);

        NetworkUtils.getWithParamsRequest( productParameter, "/public/product/productList", new Callback() {
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
        JSONObject dataObject = jsonObject.getJSONObject("data").getJSONObject("ProductList");


        // 提取 "records" 数组并转换为List
        JSONArray recordsArray = dataObject.getJSONArray("records");

        List<Record> recordsListUse = recordsArray.toJavaList(Record.class);

        if (code == 200) {

           recordList = recordsListUse;

           // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // 更新Adapter的数据
                    itemListAdapter.setRecordList(recordList);
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
        public void onClick(int pos) {
            Toast.makeText(getContext(), "click"+ pos, Toast.LENGTH_SHORT).show();
        }
    };

    class myDecoration extends RecyclerView.ItemDecoration{
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.dividerHeight));
        }
    }
}
