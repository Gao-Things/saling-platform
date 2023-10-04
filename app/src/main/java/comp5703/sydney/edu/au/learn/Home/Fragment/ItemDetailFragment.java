package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kyleduo.switchbutton.SwitchButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.Home.Adapter.ProductOfferListAdapter;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.OfferParameter;
import comp5703.sydney.edu.au.learn.VO.productDetailParameter;
import comp5703.sydney.edu.au.learn.VO.productOfferParameter;
import comp5703.sydney.edu.au.learn.VO.productParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ItemDetailFragment extends Fragment {
    private TextView itemDetailPrice;
    private Button confirmButton;
    private IOMessageClick listener;
    private Button send_offer_btn;
    private ImageView ItemImage;
    LinearLayout hiddenLayout;
    private EditText optionNotes;

    private Integer userId;
    private String token;

    private LinearLayout generalView;

    private RecyclerView offerList;

    private ProductOfferListAdapter productOfferListAdapter;


    private ImageView itemStatusImg;

    private SwitchButton switchButton;

    private LinearLayout offerHistory;

    private TextView offeredPrice;

    private Integer productId;

    private TextView emptyText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemDetailPrice = view.findViewById(R.id.itemDetailPrice);
        confirmButton = view.findViewById(R.id.confirm_button);
        send_offer_btn = view.findViewById(R.id.send_offer_btn);
        hiddenLayout = view.findViewById(R.id.hidden_layout);
        ItemImage = view.findViewById(R.id.ItemImage);
        optionNotes = view.findViewById(R.id.optionNotes);
        confirmButton.setOnClickListener(this::sendOfferClick);

        generalView = view.findViewById(R.id.generalView);
        offerList = view.findViewById(R.id.offerList);
        itemStatusImg = view.findViewById(R.id.itemStatusImg);
        offerHistory = view.findViewById(R.id.offerHistory);
        offeredPrice = view.findViewById(R.id.offeredPrice);
        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        offerList.setLayoutManager(layoutManager);

        // 创建并设置RecyclerView的Adapter
        productOfferListAdapter = new ProductOfferListAdapter(getContext(),new ArrayList<Offer>(),clickListener);
        offerList.setAdapter(productOfferListAdapter);
        emptyText = view.findViewById(R.id.emptyText);
        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");
//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick("fragment往activity传值");
//            }
//        })

        // 在 ItemDetailFragment 中获取传递的整数值
        Bundle args = getArguments();
        if (args != null) {
             productId = args.getInt("productId");
            getProductInformation(productId);
            getProductOfferHistory(productId);

        }

        // 初始化 switchButton
        switchButton = view.findViewById(R.id.switch_button);
        switchButton.setChecked(true);
        initializeSwitchListener();



    }
    private void initializeSwitchListener() {
        SwitchButton.OnCheckedChangeListener listener = new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchButton.setOnCheckedChangeListener(null);
                showConfirmationDialog(isChecked);
            }
        };
        switchButton.setOnCheckedChangeListener(listener);
    }

    private void showConfirmationDialog(boolean isChecked) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_custom_layout, null);
        TextView title = dialogView.findViewById(R.id.dialogTitle);

        if (isChecked){
            title.setText("Do you wish to open the offer ?");
        }else {
            title.setText("Do you wish to close the offer ?");
        }

        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.show();

        Button confirmButton = dialogView.findViewById(R.id.confirm_button);

        // 确认按钮的点击处理
        confirmButton.setOnClickListener(v -> {
            dialog.dismiss();

            // User confirmed, so update the button color
            updateButtonColor(isChecked);
            // Re-set the OnCheckedChangeListener
            initializeSwitchListener();

        });

        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(v -> {
            // 取消按钮的点击处理
            dialog.dismiss();
            // User cancelled, so revert the button state and re-set the OnCheckedChangeListener
            switchButton.setChecked(!isChecked);
            initializeSwitchListener();
        });
    }

    private void updateButtonColor(boolean isChecked) {
        int backColor;
        if (isChecked) {
            backColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.generalGreen);
        } else {
            backColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.red);
        }
        switchButton.setBackColor(ColorStateList.valueOf(backColor));
    }

    private void getProductOfferHistory(Integer productId){
        // 在这里可以使用 productId 进行操作
        productOfferParameter productOfferParameter = new productOfferParameter();
        productOfferParameter.setProductId(productId);
        productOfferParameter.setUserId(userId);
        // send request to backend
        NetworkUtils.getWithParamsRequest(productOfferParameter, "/normal/getOfferByUserAndProductId",token, new Callback() {
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

    private void handleResponse2(Response response) {

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
                    Offer offer = dataArray.getJSONObject(0).toJavaObject(Offer.class);
                    // 在主线程中更新UI
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            // 如果后端返回的不为空值就更新历史记录
                            offerHistory.setVisibility(View.VISIBLE);
                            offeredPrice.setText(Double.toString(offer.getPrice()));
                        }
                    });
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

    private void getProductInformation(Integer productId) {
        // 在这里可以使用 productId 进行操作
        productDetailParameter productDetailParameter = new productDetailParameter();
        productDetailParameter.setProductId(productId);
        // send request to backend
        NetworkUtils.getWithParamsRequest(productDetailParameter, "/public/product/getProductDetail",null, new Callback() {
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


    public void sendOfferClick(View view){
        hiddenLayout.setVisibility(View.VISIBLE); // 设置为 VISIBLE，使其显示
        confirmButton.setVisibility(View.GONE);
        send_offer_btn.setVisibility(View.VISIBLE);
        optionNotes.setVisibility(View.VISIBLE);
    }

    public interface IOMessageClick{
        void onClick(String text);
    }


    private void handleResponse(Response response) {
        try {
            if (!response.isSuccessful()) {
                Log.d(TAG, "Request not successful");
                return;
            }

            String responseBody = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(responseBody);
            int code = jsonObject.getIntValue("code");

            if (code == 200) {
                Product product = jsonObject.getJSONObject("data").toJavaObject(Product.class);

                if (product.getOwnerId().intValue() == userId){
                    // if the login is the seller
                    loadSellerView();
                }

                // 在主线程中更新UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemDetailPrice.setText(Double.toString(product.getProductPrice()));
                        Picasso.get()
                                .load(imageURL+ product.getProductImage()) // 网络图片的URL
                                .into(ItemImage);

                    }
                });
            } else {
                Log.d(TAG, "Error response code: " + code);
            }
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (JSONException e) {
            Log.e(TAG, "JSONException: " + e.getMessage());
        }
    }


    // 加载seller的商品的offer
    private void loadSellerView(){

        // 在主线程中更新UI
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                generalView.setVisibility(View.GONE);
                switchButton.setVisibility(View.VISIBLE);
                itemStatusImg.setVisibility(View.GONE);

            }
        });

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
                    // 更新Adapter的数据
                    productOfferListAdapter.setRecordList(OfferList);

                    // 在主线程中更新UI
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            offerList.setVisibility(View.VISIBLE);
                            // 在UI线程上更新Adapter的数据
                            productOfferListAdapter.notifyDataSetChanged();
                        }
                    });
                }else {
                    // seller offer为空在主线程中更新UI
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            emptyText.setVisibility(View.VISIBLE);
                        }
                    });
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



    // 出现时候触发的事件
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ItemDetailFragment.IOMessageClick) context;
        }catch (ClassCastException e){
            throw new ClassCastException("Activity必须实现 IOMessageClick接口");
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
