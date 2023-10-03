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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.ProductOffer;
import comp5703.sydney.edu.au.learn.Home.Adapter.OfferReceivedListAdapter;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.OfferParameter;
import comp5703.sydney.edu.au.learn.VO.cancelOfferParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ReceivedOfferFragment extends Fragment {
    private View rootView;

    private RecyclerView itemRecyclerView;

    private OfferReceivedListAdapter offerReceivedListAdapter;

    private Integer userId;

    private String token;

    private Fragment itemDetailFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_received_offer, container, false);
        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        // get offer list from back-end
        getOfferList();
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
        offerReceivedListAdapter = new OfferReceivedListAdapter(getContext(),new ArrayList<ProductOffer>(),clickListener, onCancelClickListener);
        itemRecyclerView.setAdapter(offerReceivedListAdapter);

    }

    private void getOfferList(){
        OfferParameter offerParameter = new OfferParameter();
        offerParameter.setUserId(userId);

        NetworkUtils.getWithParamsRequest( offerParameter, "/normal/getReceivedOfferList", token, new Callback() {
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

            List<ProductOffer> OfferList = recordsArray.toJavaList(ProductOffer.class);

            System.out.println("这是offer list" + OfferList);
            // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    // 更新Adapter的数据
                    offerReceivedListAdapter.setRecordList(OfferList);
                    // 在UI线程上更新Adapter的数据
                    offerReceivedListAdapter.notifyDataSetChanged();
                }
            });

        } else {
            Log.d(TAG, "error");
        }
    }


    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    OfferReceivedListAdapter.OnItemClickListener clickListener = new OfferReceivedListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer itemId, double productPrice) {
            // is resent click
            showConfirmationDialog(1, itemId, productPrice);

        }
    };

    OfferReceivedListAdapter.OnCancelClickListener onCancelClickListener = new OfferReceivedListAdapter.OnCancelClickListener() {
        @Override
        public void onClick(int pos, Integer itemId, int i) {

//            showConfirmationDialog(0, itemId);

        }
    };


    private void showConfirmationDialog(int operate, Integer itemId, double productPrice) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_layout, null);
        if (operate == 1){
            TextView title = dialogView.findViewById(R.id.dialogTitle);
            LinearLayout offerPriceLayout = dialogView.findViewById(R.id.offerPriceLayout);
            TextView dialogOfferPrice = dialogView.findViewById(R.id.dialogOfferPrice);
            offerPriceLayout.setVisibility(View.VISIBLE);
            dialogOfferPrice.setText(Double.toString(productPrice));
            title.setText("Do you wish accept the offer ?");
        }

        if (operate == 0){
            TextView title = dialogView.findViewById(R.id.dialogTitle);
            title.setText("Do you wish to cancel the offer ?");
        }

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button confirmButton = dialogView.findViewById(R.id.confirm_button);

        // 确认按钮的点击处理
        confirmButton.setOnClickListener(v -> {
            dialog.dismiss();

            if (operate == 1){
                // 处理接受offer的操作
                cancelOfferParameter cancelOfferParameter = new cancelOfferParameter();
                cancelOfferParameter.setOfferId(itemId);
                cancelOfferParameter.setToken(token);
                NetworkUtils.postJsonRequest(cancelOfferParameter,"/normal/acceptAnOffer",token, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        handleResponseForAcceptOffer(response);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        handleFailure(e);
                    }
                });
            }


        });

        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> {
            // 取消按钮的点击处理
            dialog.dismiss();
        });
    }


    private void handleResponseForAcceptOffer(Response response) {
        try {
            if (!response.isSuccessful()) {
                Log.d(TAG, "Request not successful");
                return;
            }
            String responseBody = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            int code = jsonObject.getIntValue("code");

            if (code == 200) {
                // 在主线程中更新UI
                Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {
                        getOfferList();
                        Snackbar.make(rootView, "Your have accept the offer", Snackbar.LENGTH_LONG)
                                .setAction("NEWS", null).show();
                    }
                });

            } else {
                Log.d(TAG, "Error response code: " + code);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
    }
}
