package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.productDetailParameter;
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


//        confirmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listener.onClick("fragment往activity传值");
//            }
//        });



        // 在 ItemDetailFragment 中获取传递的整数值
        Bundle args = getArguments();
        if (args != null) {
            int productId = args.getInt("productId");
            // 在这里可以使用 productId 进行操作
            productDetailParameter productDetailParameter = new productDetailParameter();
            productDetailParameter.setProductId(productId);
            // send request to backend
            NetworkUtils.getWithParamsRequest(productDetailParameter, "/public/product/getProductDetail", new Callback() {
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

                // 在主线程中更新UI
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        itemDetailPrice.setText(Double.toString(product.getProductPrice()));
                        Picasso.get()
                                .load(product.getProductImage()) // 网络图片的URL
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



}
