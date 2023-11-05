package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.Home.Adapter.MyProductListAdapter;
import comp5703.sydney.edu.au.learn.Home.Adapter.UnavaliableProductListAdapter;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.MyProductVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyProductUnavaliableFragment extends Fragment {
    private View rootView;

    private RecyclerView itemRecyclerView;

    private UnavaliableProductListAdapter unavaliableProductListAdapter;

    private Integer userId;

    private String token;

    private Integer selectOfferId;

    private Fragment itemDetailFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_unavaliable_product, container, false);
        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        // get offer list from back-end
        getMyProductList();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemRecyclerView = view.findViewById(R.id.list_main);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemRecyclerView.setLayoutManager(layoutManager);

        // 创建并设置RecyclerView的Adapter
        unavaliableProductListAdapter = new UnavaliableProductListAdapter(getContext(),new ArrayList<Product>(),clickListener);
        itemRecyclerView.setAdapter(unavaliableProductListAdapter);



        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete it ？")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 这里写删除数据的代码
                                unavaliableProductListAdapter.deleteItem(viewHolder.getAdapterPosition(), token, userId);

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // 撤消滑动效果
                                unavaliableProductListAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        })
                        .setCancelable(false);  // 这里设置对话框为不可取消;
                builder.create().show();
            }

            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getActivity(), R.color.yellow))
                        .addActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelTextSize(1,17)
                        .setSwipeLeftLabelColor(ContextCompat.getColor(getActivity(), R.color.white)) //设置字体颜色
                        .create()
                        .decorate();
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(itemRecyclerView);


    }

    private void getMyProductList(){
        MyProductVO myProductVO = new MyProductVO(userId, false);

        NetworkUtils.getWithParamsRequest( myProductVO, "/normal/getProductListByUserID", token, new Callback() {
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


    private void handleResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        if (code == 200) {
            // 提取 数组并转换为List
            JSONArray recordsArray = jsonObject.getJSONArray("data");

            List<Product> ProductList = recordsArray.toJavaList(Product.class);

            System.out.println("这是offer list" +  ProductList);
            // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    // 更新Adapter的数据
                    unavaliableProductListAdapter.setRecordList(ProductList);
                    // 在UI线程上更新Adapter的数据
                    unavaliableProductListAdapter.notifyDataSetChanged();
                }
            });

        } else {
            Log.d(TAG, "error");
        }
    }


    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    UnavaliableProductListAdapter.OnItemClickListener clickListener = (pos, itemId) -> {
        ItemDetailFragment itemDetailFragment = new ItemDetailFragment();
        Bundle args = new Bundle();
        args.putInt("productId", itemId);
        itemDetailFragment.setArguments(args);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // 使用兼容库的FragmentManager
        if (fragmentManager != null) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fl_container, itemDetailFragment);
            transaction.addToBackStack(null);
            transaction.commit(); // 如果您了解commitAllowingStateLoss()的风险，可以使用它
        }

        Activity activity = getActivity();
        if (activity instanceof HomeUseActivity) {
            ((HomeUseActivity) activity).updateToolbar(true, "Product Detail");
        }
    };



    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(false, "Home");
    }

}
