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
import android.widget.CompoundButton;
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
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.Home.Adapter.MyOfferListAdapter;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.OfferParameter;
import comp5703.sydney.edu.au.learn.VO.cancelOfferParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MyOfferFragment extends Fragment {
    private View rootView;

    private RecyclerView itemRecyclerView;

    private MyOfferListAdapter myOfferListAdapter;

    private Integer userId;

    private String token;

    private Fragment itemDetailFragment;


    MaterialCheckBox checkBoxAccepted;
    MaterialCheckBox checkBoxRejected;
    MaterialCheckBox checkBoxCancelled;
    MaterialCheckBox checkBoxPending;
    MaterialCheckBox checkBoxExpired;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_offer, container, false);
        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        checkBoxAccepted = view.findViewById(R.id.checkBoxAccepted);
        checkBoxRejected = view.findViewById(R.id.checkBoxRejected);
        checkBoxCancelled = view.findViewById(R.id.checkBoxCancelled);
        checkBoxPending = view.findViewById(R.id.checkBoxPending);
        checkBoxExpired = view.findViewById(R.id.checkBoxExpired);


        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                requestBackend();
            }
        };

        checkBoxAccepted.setOnCheckedChangeListener(listener);
        checkBoxRejected.setOnCheckedChangeListener(listener);
        checkBoxCancelled.setOnCheckedChangeListener(listener);
        checkBoxPending.setOnCheckedChangeListener(listener);
        checkBoxExpired.setOnCheckedChangeListener(listener);



        itemRecyclerView = view.findViewById(R.id.list_main);
        itemRecyclerView.setNestedScrollingEnabled(false);
        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        itemRecyclerView.setLayoutManager(layoutManager);

        // 创建并设置RecyclerView的Adapter
        myOfferListAdapter = new MyOfferListAdapter(getContext(),new ArrayList<Offer>(),clickListener, onCancelClickListener);
        itemRecyclerView.setAdapter(myOfferListAdapter);

        // get offer list from back-end
        requestBackend();

    }


    private void requestBackend() {

        boolean accepted = checkBoxAccepted.isChecked();
        boolean rejected = checkBoxRejected.isChecked();
        boolean cancelled = checkBoxCancelled.isChecked();
        boolean pending = checkBoxPending.isChecked();
        boolean expired = checkBoxExpired.isChecked();

        // send request to filter the offer
        getOfferList(accepted,rejected,cancelled,pending,expired);
    }


    private void getOfferList(Boolean accepted,
                              Boolean rejected,
                              Boolean cancelled,
                              Boolean pending,
                              Boolean expired){
        OfferParameter offerParameter = new OfferParameter();
        offerParameter.setUserId(userId);
        offerParameter.setAccepted(accepted);
        offerParameter.setCancelled(cancelled);
        offerParameter.setPending(pending);
        offerParameter.setRejected(rejected);
        offerParameter.setExpired(expired);

        NetworkUtils.getWithParamsRequest( offerParameter, "/normal/getMyOfferList", token, new Callback() {
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

            List<Offer> OfferList = recordsArray.toJavaList(Offer.class);

            System.out.println("这是offer list" + OfferList);
            // 通知adapter数据更新
            getActivity().runOnUiThread(new Runnable() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void run() {
                    // 更新Adapter的数据
                    myOfferListAdapter.setRecordList(OfferList);
                    // 在UI线程上更新Adapter的数据
                    myOfferListAdapter.notifyDataSetChanged();
                }
            });

        } else {
            Log.d(TAG, "error");
        }
    }


    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }

    MyOfferListAdapter.OnItemClickListener clickListener = new MyOfferListAdapter.OnItemClickListener() {
        @Override
        public void onClick(int pos, Integer itemId,Integer productId) {
                // is resent click
                showConfirmationDialog(1, itemId, productId);

        }
    };

    MyOfferListAdapter.OnCancelClickListener onCancelClickListener = new MyOfferListAdapter.OnCancelClickListener() {
        @Override
        public void onClick(int pos, Integer itemId, int i, Integer productId) {

            showConfirmationDialog(0, itemId, productId);

        }
    };


    private void showConfirmationDialog(int operate, Integer itemId, Integer productId) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_layout, null);
        if (operate == 1){
         TextView title = dialogView.findViewById(R.id.dialogTitle);
         title.setText("Do you wish resent the offer ?");
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

            // resent offer
            if (operate == 1){
                // dump to detail page

                // 获取HomeActivity上下文的引用
                HomeUseActivity homeActivity = (HomeUseActivity) getActivity();
                if (homeActivity != null) {
                    int containerId = homeActivity.getContainerId();

                    // 准备要传递的数据
                    itemDetailFragment = new ItemDetailFragment();
                    Bundle args = new Bundle();
                    args.putInt("productId", productId); // 这里的 "key" 是传递数据的键名，"value" 是要传递的值
                    itemDetailFragment.setArguments(args);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(containerId, itemDetailFragment); // containerId 是用于放置 Fragment 的容器
                    transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
                    transaction.commitAllowingStateLoss();
                }

                return;

            }

            if (operate == 0){
                // 取消offer的点击
                cancelOfferParameter cancelOfferParameter = new cancelOfferParameter();
                cancelOfferParameter.setOfferId(itemId);
                cancelOfferParameter.setToken(token);
                NetworkUtils.postJsonRequest(cancelOfferParameter,"/normal/cancelAnOffer",token, new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        handleResponseForCancelOffer(response);
                    }

                    @Override
                    public void onFailure(Call call, IOException e) {
                        handleFailure(e);
                    }
                });

                return;
            }

        });

        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> {
            // 取消按钮的点击处理
            dialog.dismiss();
        });
    }


    private void handleResponseForCancelOffer(Response response) {
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
                        requestBackend();
                        Snackbar.make(rootView, "Your offer has been canceled", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    }
                });

            } else {
                Log.d(TAG, "Error response code: " + code);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(false, "Home");
    }

}
