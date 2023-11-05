package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;
import static comp5703.sydney.edu.au.learn.util.TimeCalculateUtil.convertTimestampToDate;
import static comp5703.sydney.edu.au.learn.util.TimeCalculateUtil.getTimeDifference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.kyleduo.switchbutton.SwitchButton;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.transformer.RotateDownPageTransformer;
import com.youth.banner.transformer.ScaleInTransformer;
import com.youth.banner.transformer.ZoomOutPageTransformer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.DTO.UserSetting;
import comp5703.sydney.edu.au.learn.Home.Adapter.ProductOfferListAdapter;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.makeAnOfferParameter;
import comp5703.sydney.edu.au.learn.VO.modifyProductStatusParameter;
import comp5703.sydney.edu.au.learn.VO.productDetailParameter;
import comp5703.sydney.edu.au.learn.VO.productOfferParameter;
import comp5703.sydney.edu.au.learn.VO.productParameter;
import comp5703.sydney.edu.au.learn.VO.userIdVO;
import comp5703.sydney.edu.au.learn.util.FormValidator;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import comp5703.sydney.edu.au.learn.util.TimeCalculateUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ItemDetailFragment extends Fragment implements OnBannerListener<String> {
    private Button confirmButton;
    private IOMessageClick listener;
    private Button send_offer_btn;
    LinearLayout hiddenLayout;
    private EditText optionNotes;

    private Integer userId;
    private String token;
    private View rootView;

    private LinearLayout generalView;


    private ImageView itemStatusImg;
    private ImageView itemCloseImg;

    private SwitchButton switchButton;

    private LinearLayout offerHistory;

    private TextView offeredPrice;

    private TextView offerHistoryTime;

    private Integer productId;

    private TextView setPrice;

    private Banner imageBanner;

    private Button sellerGetOfferBtn;
    private LinearLayout sellerHeaderBar;

    private Button editButton;

    private Button contactSellerBtn;

    private ChatFragment chatFragment;

    private Integer sellerId;

    private Boolean checkButtonFlag = true;

    private boolean isClickBlocked = false;


    // 基础的商品信息

    private TextView itemName;
    private TextView itemDescription;
    private TextView itemWeight;
    private TextView itemType;
    private TextView itemPurity;
    private TextView itemPostTime;
    private TextView offerTime;

    private TextInputLayout inputLayoutPrice;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);

        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editButton = view.findViewById(R.id.editButton);
        confirmButton = view.findViewById(R.id.confirm_button);
        send_offer_btn = view.findViewById(R.id.send_offer_btn);
        hiddenLayout = view.findViewById(R.id.hidden_layout);
        optionNotes = view.findViewById(R.id.optionNotes);
        confirmButton.setOnClickListener(this::sendOfferClick);
        imageBanner = view.findViewById(R.id.banner);

        generalView = view.findViewById(R.id.generalView);

        itemStatusImg = view.findViewById(R.id.itemStatusImg);
        itemCloseImg = view.findViewById(R.id.itemCloseImg);
        offerHistory = view.findViewById(R.id.offerHistory);
        offeredPrice = view.findViewById(R.id.offeredPrice);
        inputLayoutPrice = view.findViewById(R.id.inputLayoutPrice);

        sellerGetOfferBtn = view.findViewById(R.id.sellerGetOfferBtn);
        contactSellerBtn = view.findViewById(R.id.contactSellerBtn);

        offerHistoryTime = view.findViewById(R.id.offerHistoryTime);


        // 设置商品基础信息
        itemName = view.findViewById(R.id.itemName);
        itemDescription = view.findViewById(R.id.itemDescription);
        itemWeight = view.findViewById(R.id.itemWeight);
        itemType = view.findViewById(R.id.itemType);
        itemPurity = view.findViewById(R.id.itemPurity);
        itemPostTime = view.findViewById(R.id.itemPostTime);
        offerTime = view.findViewById(R.id.offerTime);


        sellerHeaderBar = view.findViewById(R.id.sellerHeaderBar);
        // 创建并设置RecyclerView的LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());


        setPrice = view.findViewById(R.id.setPrice);


        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        userId = sharedPreferences.getInt("userId", 9999);
        token = sharedPreferences.getString("token", "null");
        
//          userId = sharedPreferences.getInt("userId", 9999);
//         token = sharedPreferences.getString("token", "null");

        // 初始化 switchButton
        switchButton = view.findViewById(R.id.switch_button);

        initializeSwitchListener();

        // 在 ItemDetailFragment 中获取传递的整数值
        Bundle args = getArguments();
        if (args != null) {
             productId = args.getInt("productId");
            getProductInformation(productId);
            getProductOfferHistory(productId);

        }


        send_offer_btn.setOnClickListener(this::submitOffer);
        sellerGetOfferBtn.setOnClickListener(this::showOfferHistory);
        editButton.setOnClickListener(this::editClick);
        contactSellerBtn.setOnClickListener(this::dumpToChatRoom);

    }


    // 跳转到聊天页面
    private void dumpToChatRoom(View view) {
        // 先判断receiverId的用户是否开启了聊天权限

        userIdVO userIdVO = new userIdVO(sellerId);
        // 发送一份offer
        NetworkUtils.getWithParamsRequest(userIdVO,"/normal/getUserSetting",token, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleGetRemoteUserSettingResponse(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
            }
        });

    }

    private void handleGetRemoteUserSettingResponse(Response response) throws IOException {

        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");

        if (code == 200) {

            UserSetting remoteUserSetting = jsonObject.getJSONObject("data").toJavaObject(UserSetting.class);

            if (remoteUserSetting.getMessageReceived() == 1){
                // jump to chat fragment
                if (chatFragment == null){
                    chatFragment = new ChatFragment();
                }
                // 准备要传递的数据
                Bundle args = new Bundle();


                args.putInt("receiverId", sellerId); // 这里的 "key" 是传递数据的键名，"value" 是要传递的值
                args.putInt("userId", userId);
                args.putString("token", token);
                chatFragment.setArguments(args);

                // 执行 Fragment 跳转
                assert getFragmentManager() != null;
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fl_container, chatFragment); // R.id.fragment_container 是用于放置 Fragment 的容器
                transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
                transaction.commitAllowingStateLoss();
            }else {
                Snackbar.make(rootView, "The seller has turned off the chat function", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }


        } else {
            Log.d(TAG, "error");
        }
    }

    private void editClick(View view) {
        // 在 FragmentA 中
        SellFragment sellFragment = new SellFragment();

        // 准备要传递的数据
        Bundle args = new Bundle();
        args.putInt("productId", productId); // 这里的 "key" 是传递数据的键名，"value" 是你要传递的值
        sellFragment.setArguments(args); // 这是关键步骤！
        // 执行 Fragment 跳转
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_container, sellFragment); // R.id.fragment_container 是用于放置 Fragment 的容器
        transaction.addToBackStack(null); // 将 FragmentA 添加到返回栈，以便用户可以返回到它
        transaction.commit();
    }

    private void showOfferHistory(View view) {
        // 展开bottom view
        MyBottomSheetDialogFragment bottomSheet = MyBottomSheetDialogFragment.newInstance();
        Bundle args = new Bundle();
        args.putInt("productId", productId);
        bottomSheet.setArguments(args);
        bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
    }


    // submit a offer
    private void submitOffer(View view){


        if (isClickBlocked) {
            return;
        }
        isClickBlocked = true;

        // 处理点击事件
        boolean isValid =  FormValidator.validateTextInputLayoutAsFloat(
                inputLayoutPrice,
                setPrice.getText().toString(),
                "Null input",
                "Invalid input");

        // 在逻辑执行完毕后重置标志变量，可以使用Handler来延迟重置
        new Handler().postDelayed(() -> isClickBlocked = false, 1000); // 延迟1秒


        if (isValid){
            makeAnOfferParameter makeAnOfferParameter = new makeAnOfferParameter();
            String stringPrice =setPrice.getText().toString();
            String notes = optionNotes.getText().toString();
            double doubleValue = Double.parseDouble(stringPrice);

            makeAnOfferParameter.setToken(token);
            makeAnOfferParameter.setPrice(doubleValue);
            makeAnOfferParameter.setNote(notes);
            makeAnOfferParameter.setProductId(productId);

            // 发送一份offer
            NetworkUtils.postJsonRequest(makeAnOfferParameter,"/normal/makeOrUpdateAnOffer",token, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    handleResponseForMakeOffer(response, view);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    handleFailure(e);
                }
            });
        }


    }

    // make offer
    private void handleResponseForMakeOffer(Response response, View view) {
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
                            offerHistory.setVisibility(View.VISIBLE);

                            // TODO 加上上一次offer的时间
                            offeredPrice.setText("Offered Price: " + setPrice.getText().toString());
                            offerHistoryTime.setText("Offered Time: " + TimeCalculateUtil.getTimeElapsed(System.currentTimeMillis()));

                            Snackbar.make(view, "Make offer successful!", Snackbar.LENGTH_LONG)
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

    // 初始化切换按钮
    private void initializeSwitchListener() {
        SwitchButton.OnCheckedChangeListener listener = new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // 第一次加载页面不会弹出确认框
                if (!checkButtonFlag){
                    showConfirmationDialog(isChecked);
                }
            }
        };
        switchButton.setOnCheckedChangeListener(listener);
    }

    // 显示确认弹窗
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

            // TODO 发送请求，更改商品状态
            modifyProductStatusParameter modifyProductStatusParameter = new modifyProductStatusParameter();
            modifyProductStatusParameter.setProductId(productId);
            if (isChecked){
                modifyProductStatusParameter.setProductStatusNew(0);
            }else {
                modifyProductStatusParameter.setProductStatusNew(1);
            }

            modifyProductStatusParameter.setToken(token);
            // send request to backend
            NetworkUtils.postJsonRequest(modifyProductStatusParameter, "/normal/openOrCloseOrCancelSale",token, new Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    handleModifyProductStatusResponse(response);
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    handleFailure(e);
                }
            });


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

    private void handleModifyProductStatusResponse(Response response) throws IOException {
        String responseBody = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);
        int code = jsonObject.getIntValue("code");
        // 修改商品状态成功
        if (code == 200) {
            // 刷新产品信息
            getProductInformation(productId);
        }
    }

    // 更新按钮颜色
    private void updateButtonColor(boolean isChecked) {
        int backColor;
        if (isChecked) {
            backColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.generalGreen);
        } else {
            backColor = ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.textColorSecondary);
        }
        switchButton.setBackColor(ColorStateList.valueOf(backColor));
    }

    // 获取产品offer历史，如果你是buyer且发过offer
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
    // 获取产品offer历史，如果你是buyer且发过offer
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

                            // TODO 加上上一次offer的时间
                            offeredPrice.setText("Offered Price: " + offer.getPrice());
                            offerHistoryTime.setText("Offered Time: " + TimeCalculateUtil.getTimeElapsed(offer.getTimestamp()));
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

    // 获取产品的详情
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

    // 开始发送offer的点击，更改页面视图
    public void sendOfferClick(View view){
        hiddenLayout.setVisibility(View.VISIBLE); // 设置为 VISIBLE，使其显示
        confirmButton.setVisibility(View.GONE);
        send_offer_btn.setVisibility(View.VISIBLE);
        optionNotes.setVisibility(View.VISIBLE);
        contactSellerBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void OnBannerClick(String data, int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
    }


    public interface IOMessageClick{
        void onClick(String text);
    }

    // 获取产品的详情接口的响应函数
    @SuppressLint("SetTextI18n")
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

                sellerId = product.getOwnerId().intValue();



                // 属于卖家页面
                if (product.getOwnerId().intValue() == userId){

                    if (product.getProductStatus()!=0){
                        // if the product is not in processing
                        loadSellerView(false);
                    }else {
                        loadSellerView(true);
                    }

                }

                // 在主线程中更新UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        /**
                         *  TODO 把基础产品信息显示出来
                         */
                        itemName.setText(product.getProductName());
                        itemDescription.setText(product.getProductDescription());
                        itemPurity.setText(product.getPurity());

                        if (product.getCategory() ==1){
                            itemType.setText("gold");
                        }else {
                            itemType.setText("sliver");
                        }

                        itemWeight.setText(product.getProductWeight() + " g");

                        String formattedDate = convertTimestampToDate(product.getProductCreateTime());
                        itemPostTime.setText(formattedDate);


                        offerTime.setText(getTimeDifference( product.getProductCreateTime()));



                    // 如果产品属于编辑状态就锁死编辑按钮
                    if (product.getProductStatus()==0){
                        editButton.setBackground(getResources().getDrawable(R.drawable.edit_button_disabled));
                        editButton.setTextColor(getResources().getColor(R.color.black));
                        editButton.setEnabled(false);
                    }else {
                        editButton.setBackground(getResources().getDrawable(R.drawable.custom_button_background2));
                        editButton.setEnabled(true);
                    }

                        // 把图片链接字符串转回数组
                        String[] items = product.getProductImage().substring(1, product.getProductImage().length() - 1).split(", ");

                        List<String> imageUrlList = new ArrayList<>();
                        for (String item : items) {
                            imageUrlList.add( imageURL +item);
                        }

                        loadBanner(imageUrlList);
                        // 如果状态不在售卖且不属于卖家家页面
                        if (product.getProductStatus()!=0 && product.getOwnerId().intValue() != userId){
                            itemStatusImg.setVisibility(View.GONE);
                            itemCloseImg.setVisibility(View.VISIBLE);
                            confirmButton.setVisibility(View.INVISIBLE);

                        }


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

    private void loadBanner(List<String> imgList){

        imageBanner.setAdapter(new BannerImageAdapter<String>(imgList) {
                    @Override
                    public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                        //图片加载实现
                        Glide.with(holder.itemView)
                                .load(data)
                                .apply(RequestOptions.bitmapTransform(new RoundedCorners(30)))
                                .into(holder.imageView);

                        holder.imageView.setPadding(0, 40, 0, 40);

                    }


                })
                .addBannerLifecycleObserver(this)//添加生命周期观察者
                .addPageTransformer(new ScaleInTransformer())
                .isAutoLoop(true)
                .setBannerGalleryEffect(40,40,30)
                .setIndicator(new CircleIndicator(getContext()));

    }

    // 加载seller的商品的offer
    private void loadSellerView(boolean isValid){

        // 展示商品的offer
        showOfferHistory(rootView);
        // 在主线程中更新UI
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                itemStatusImg.setVisibility(View.GONE);
                generalView.setVisibility(View.GONE);
                sellerHeaderBar.setVisibility(View.VISIBLE);
                sellerGetOfferBtn.setVisibility(View.VISIBLE);

                if (!isValid){
                    switchButton.setChecked(false);
                    updateButtonColor(false);
                }else {
                    switchButton.setChecked(true);
                    updateButtonColor(true);

                }

                // 第一次加载完swich button后就需要用户确认是否改变状态
                checkButtonFlag = false;


            }
        });


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


    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(true, "Product Detail");
    }


}
