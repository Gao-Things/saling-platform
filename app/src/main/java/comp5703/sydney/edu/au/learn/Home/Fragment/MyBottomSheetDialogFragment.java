package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.Home.Adapter.ProductOfferListAdapter;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.productParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private RecyclerView offerList;

    private ProductOfferListAdapter productOfferListAdapter;
    private TextView emptyText;

    private Integer productId;

    private Integer userId;
    private String token;

    public static MyBottomSheetDialogFragment newInstance() {
        return new MyBottomSheetDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.bottom_sheet_layout, container, false);


        // get global userID
        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        // 获取传递的ProductID
        Bundle args = getArguments();
        if (args != null) {
            productId = args.getInt("productId");

        }

        offerList = view.findViewById(R.id.offerList);
        emptyText = view.findViewById(R.id.emptyText);

        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        offerList.setLayoutManager(layoutManager);

        // 创建并设置RecyclerView的Adapter
        productOfferListAdapter = new ProductOfferListAdapter(getContext(),new ArrayList<Offer>(),clickListener);
        offerList.setAdapter(productOfferListAdapter);

        // get offer data
        setDataByRequest();
        return view;
    }

    private void setDataByRequest(){
        // send request to get the seller item
        productParameter productParameter = new productParameter();
        productParameter.setProductId(productId);
        NetworkUtils.getWithParamsRequest( productParameter, "/normal/getProductOfferList", token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleResponse3(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        Dialog dialog = super.onCreateDialog(savedInstanceState);
//        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
//            @Override
//            public void onShow(DialogInterface dialogInterface) {
//                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
//
//                // 这行代码会防止背景变暗
//                bottomSheetDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            }
//        });
//        return dialog;
//    }


    // 加载seller的商品的offer
    @SuppressLint("NotifyDataSetChanged")
    private void handleResponse3(Response response) {
        // set data to seller adapter
        try {
            if (!response.isSuccessful()) {
                Log.d(TAG, "Request not successful");
                return;
            }
            String responseBody = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            int code = jsonObject.getIntValue("code");

            if (code == 200) {
                JSONArray dataArray = jsonObject.getJSONArray("data");

                if (!dataArray.isEmpty()) {

                    List<Offer> OfferList = dataArray.toJavaList(Offer.class);
                    Activity activity = getActivity();
                    if(activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 更新Adapter的数据
                                productOfferListAdapter.setRecordList(OfferList);
                                offerList.setVisibility(View.VISIBLE);
                                // 在UI线程上更新Adapter的数据
                                productOfferListAdapter.notifyDataSetChanged();
                            }

                        });
                    }

                }else {
                    // seller offer为空在主线程中更新UI
                    Activity activity = getActivity();
                    if(activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                emptyText.setVisibility(View.VISIBLE);
                            }

                        });
                    }
                }


            } else {
                Log.d(TAG, "Error response code: " + code);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "JSONException: " + e.getMessage());
        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    ProductOfferListAdapter.OnItemClickListener clickListener = new ProductOfferListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer itemId) {

        }
    };
}

