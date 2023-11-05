package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.ProductOffer;
import comp5703.sydney.edu.au.learn.Home.Adapter.MyProductListAdapter;
import comp5703.sydney.edu.au.learn.Home.Adapter.OfferReceivedListAdapter;
import comp5703.sydney.edu.au.learn.Home.DialogFragment.GuideDialogFragment;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.MyProductVO;
import comp5703.sydney.edu.au.learn.VO.OfferParameter;
import comp5703.sydney.edu.au.learn.VO.cancelOfferParameter;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyProductOnSellFragment extends Fragment {
    private View rootView;

    private RecyclerView itemRecyclerView;

    private MyProductListAdapter myProductListAdapter;

    private Integer userId;

    private String token;

    private Integer selectOfferId;

    private Fragment itemDetailFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_product, container, false);
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
        myProductListAdapter = new MyProductListAdapter(getContext(),new ArrayList<Product>(),clickListener);
        itemRecyclerView.setAdapter(myProductListAdapter);



    }

    private void getMyProductList(){
        MyProductVO myProductVO = new MyProductVO(userId, true);

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
                    myProductListAdapter.setRecordList(ProductList);
                    // 在UI线程上更新Adapter的数据
                    myProductListAdapter.notifyDataSetChanged();
                }
            });

        } else {
            Log.d(TAG, "error");
        }
    }


    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    MyProductListAdapter.OnItemClickListener clickListener = (pos, itemId) -> {
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
