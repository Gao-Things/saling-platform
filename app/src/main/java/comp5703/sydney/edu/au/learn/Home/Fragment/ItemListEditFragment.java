package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
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
import java.util.List;

import comp5703.sydney.edu.au.learn.Common.DialogFragment;
import comp5703.sydney.edu.au.learn.DTO.Record;
import comp5703.sydney.edu.au.learn.Home.Adapter.ItemEditAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.productListParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ItemListEditFragment extends Fragment {

    private RecyclerView itemRecyclerView;

    private List<Record> recordList;

    private ItemEditAdapter itemEditAdapter;

    private ItemListFragment itemListFragment;

    private TextView toListClick;

    private Button confirmButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_edit, container, false);
        // get recordList By request backend
        getRecordList();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        super.onViewCreated(view, savedInstanceState);
        itemRecyclerView = view.findViewById(R.id.list_main);
        toListClick = view.findViewById(R.id.toListClick);
        confirmButton = view.findViewById(R.id.confirm_button);
        // 定义进入和退出动画资源文件
        int enterAnimation = R.anim.slide_in_right; // 进入动画
        int exitAnimation = R.anim.slide_out_left; // 退出动画

        toListClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListFragment == null){
                    itemListFragment = new ItemListFragment();
                }

                getFragmentManager().beginTransaction().setCustomAnimations(enterAnimation, exitAnimation).
                        replace(R.id.fl_container, itemListFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new DialogFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                dialogFragment.show(transaction, "dialog_fragment_tag");
            }
        });


        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemRecyclerView.setLayoutManager(layoutManager);


        // 创建并设置RecyclerView的Adapter
        itemEditAdapter = new ItemEditAdapter(getContext(),new ArrayList<Record>(),clickListener);
        itemRecyclerView.setAdapter(itemEditAdapter);

        // 添加分隔线装饰
        itemRecyclerView.addItemDecoration(new ItemListEditFragment.myDecoration());

    }


    private void getRecordList(){
        productListParameter productListParameter = new productListParameter();
        productListParameter.setPageNum(1);
        productListParameter.setPageSize(20);

        NetworkUtils.getWithParamsRequest( productListParameter, "/public/product/productList",null, new Callback() {
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
                    itemEditAdapter.setRecordList(recordList);
                    // 在UI线程上更新Adapter的数据
                    itemEditAdapter.notifyDataSetChanged();
                }
            });


        } else {
            Log.d(TAG, "errowwwwwww");
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }


   ItemEditAdapter.OnItemClickListener clickListener = new ItemEditAdapter.OnItemClickListener() {
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
