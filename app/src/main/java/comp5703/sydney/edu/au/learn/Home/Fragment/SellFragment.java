package comp5703.sydney.edu.au.learn.Home.Fragment;

import static android.content.ContentValues.TAG;

import static comp5703.sydney.edu.au.learn.util.NetworkUtils.imageURL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.itheima.wheelpicker.WheelPicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import comp5703.sydney.edu.au.learn.Common.DialogFragment;
import comp5703.sydney.edu.au.learn.DTO.Offer;
import comp5703.sydney.edu.au.learn.DTO.Product;
import comp5703.sydney.edu.au.learn.Home.Adapter.ImageAdapter;
import comp5703.sydney.edu.au.learn.Home.HomeUseActivity;
import comp5703.sydney.edu.au.learn.MainActivity;
import comp5703.sydney.edu.au.learn.R;
import comp5703.sydney.edu.au.learn.VO.ItemVO;
import comp5703.sydney.edu.au.learn.VO.productDetailParameter;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SellFragment extends Fragment {

    private EditText editTitle;
    private EditText editDescription;
    private EditText editWeight;
    private EditText editPrice;

    private AppCompatButton btnSubmit;
    private String uploadImageUrl;
    private AutoCompleteTextView autoCompleteTextView;

    private AutoCompleteTextView autoCompleteTextView2;

    private TextInputLayout textInputLayout;
    private TextInputLayout textInputLayout2;

    private Uri photoURI;
    private File photoFile;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    WheelPicker wheelPicker;

    private ImageAdapter imageAdapter;

    private Integer productId;

    private MaterialCheckBox materialCheckBox;

    private TextView chooseUnit;

    private String selectType;

    private Fragment itemDetailFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sell, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTitle = view.findViewById(R.id.editTitle);
        editDescription = view.findViewById(R.id.editDescription);
        editWeight = view.findViewById(R.id.editWeight);
        editPrice = view.findViewById(R.id.editPrice);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        wheelPicker =  view.findViewById(R.id.wheel1);
        chooseUnit = view.findViewById(R.id.chooseUnit);
        materialCheckBox =view.findViewById(R.id.checkBoxHiddenPrice);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        textInputLayout2 = view.findViewById(R.id.textInputLayout2);

        String[] items2 = new String[] {"gold", "silver"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                getContext(),
                R.layout.my_dropdown_item,  // 使用自定义布局
                items2
        );

        autoCompleteTextView2 = view.findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView2.setAdapter(adapter2);
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.d("AutoComplete", "Selected item: " + selectedItem);

                selectType = selectedItem;

                // 设置第二个 AutoCompleteTextView 的提示文本
                if (selectType.equals("gold")){

                    autoCompleteTextView.setHint("Please select gold type");
                }else {

                    autoCompleteTextView.setHint("Please select silver type");
                }

                textInputLayout.setVisibility(View.VISIBLE);
            }
        });

        String[] items = new String[] {"24K", "21K", "18K"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.my_dropdown_item,  // 使用自定义布局
                items
        );

        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setAdapter(adapter);




        // 初始化画廊
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));  // 3 for 3 columns
        imageAdapter = new ImageAdapter(new ArrayList<>());  // initially empty list
        recyclerView.setAdapter(imageAdapter);

        imageAdapter.setOnAddImageClickListener(new ImageAdapter.OnAddImageClickListener() {
            @Override
            public void onAddImageClick() {
                // 这里处理添加图片的操作
                openCameraClick();
            }
        });


        // 设置数据
        List<String> data = Arrays.asList("g", "kg", "oz");
        wheelPicker.setData(data);
        wheelPicker.setItemTextSize(40);
        wheelPicker.setVisibleItemCount(3);  // 假设5是一个合适的可见项数

        wheelPicker.setMaximumWidthTextPosition(2);
        wheelPicker.setItemSpace(20);  // 假设5是一个合适的可见项数
        // 设置选定项目的监听器
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                // 处理选定项目的事件
                System.out.println(position +":::"+data);
            }
        });

        // submit the form
        btnSubmit.setOnClickListener(this::submitClick);


        // set form if is edit
        // 在 ItemDetailFragment 中获取传递的整数值
        Bundle args = getArguments();
        if (args != null) {
            Integer productId = args.getInt("productId");
            setEditProductForm(productId);

        }

    }

    private void setEditProductForm(Integer productId) {
        // 在这里可以使用 productId 进行操作
        productDetailParameter productDetailParameter = new productDetailParameter();
        productDetailParameter.setProductId(productId);
        // send request to backend
        NetworkUtils.getWithParamsRequest(productDetailParameter, "/public/product/getProductDetail",null, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handleEditResponse(response);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                handleFailure(e);
            }
        });

    }

    private void handleEditResponse(Response response) {
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
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void run() {

                        editTitle.setText(product.getProductName());
                        editDescription.setText(product.getProductDescription());
                        editWeight.setText( Double.toString( product.getProductWeight()));
                        editPrice.setText(Double.toString(product.getProductPrice()));


                        //TODO 先暂时设置一个  ，欺骗客户
                        autoCompleteTextView.setText("Gold 24K");

                        materialCheckBox.setChecked(true);

                        // 禁用这些值，不让用户编辑
                        editWeight.setEnabled(false);

                        editPrice.setEnabled(false);
                        autoCompleteTextView.setEnabled(false);
                        autoCompleteTextView2.setEnabled(false);
                        textInputLayout.setVisibility(View.VISIBLE);
                        textInputLayout.setEnabled(false);

                        textInputLayout2.setEnabled(false);

                        wheelPicker.setVisibility(View.INVISIBLE);
                        chooseUnit.setVisibility(View.VISIBLE);

                        // 使用ImageAdapter的实例添加新的URL
                        imageAdapter.addImageUrl(imageURL + product.getProductImage());
                        imageAdapter.addImageUrl(imageURL + product.getProductImage());
                        imageAdapter.addImageUrl(imageURL + product.getProductImage());
                        imageAdapter.addImageUrl(imageURL + product.getProductImage());

                        uploadImageUrl = product.getProductImage();
                        productId = product.getId();
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


    private void openCameraClick(){
        requestCameraPermission();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchCamera();
            } else {
                // 权限被拒绝，添加额外的处理逻辑。
            }
        }
    }

    private void launchCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // 错误处理代码
            }
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(), "comp5703.sydney.edu.au.learn.fileProvider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

            NetworkUtils.postFormDataRequest(photoFile, "xxx", new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "Exception: " + e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseBody = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(responseBody);
                    int code = jsonObject.getIntValue("code");
                    if (code == 200){
                        String imageUrl = jsonObject.getString("data");
                        uploadImageUrl = imageUrl;

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // 使用ImageAdapter的实例添加新的URL
                                imageAdapter.addImageUrl(imageURL + uploadImageUrl);
                            }
                        });

                    }
                }
            });


        }
    }



    private File createImageFile() throws IOException {
        // 创建一个唯一的文件名
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        return image;
    }


    private void submitClick(View view){
        // title value
        String itemTitle = editTitle.getText().toString();

        // description
        String itemDescription = editDescription.getText().toString();

        // weight
        String itemWeight = editWeight.getText().toString();

        // drop box value
        String selectedValue = autoCompleteTextView.getText().toString();

        // get SharedPreferences instance
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

        // get global userID
        Integer userId = sharedPreferences.getInt("userId", 9999);

        ItemVO itemVO = new ItemVO();

        if (productId !=null){
            itemVO.setProductId(productId);
        }
        itemVO.setItemTitle(itemTitle);
        itemVO.setItemDescription(itemDescription);
        itemVO.setItemWeight(Double.parseDouble(itemWeight));
        itemVO.setSelectedValue(selectedValue);
        itemVO.setImageUrl(uploadImageUrl);
        itemVO.setUserId(userId);

        NetworkUtils.postJsonRequest(itemVO, "/public/product/uploadProduct", null, new Callback() {
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
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    LayoutInflater inflater = LayoutInflater.from(getContext());
                    View customView = inflater.inflate(R.layout.dialog_custom_layout2, null);

                    new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogCustomTheme)
                            .setView(customView)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 这是用户点击“确定”按钮后要执行的代码

                                    // 跳转到商品详情页面
                                    Integer productId = jsonObject.getInteger("data");

                                    // jump to item detail
                                    if (itemDetailFragment == null){
                                        itemDetailFragment = new ItemDetailFragment();
                                    }
                                    // 准备要传递的数据
                                    Bundle args = new Bundle();
                                    args.putInt("productId", productId);
                                    itemDetailFragment.setArguments(args);

                                    // 执行 Fragment 跳转
                                    assert getFragmentManager() != null;
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.fl_container, itemDetailFragment);
                                    transaction.addToBackStack(null);
                                    transaction.commitAllowingStateLoss();
                                }
                            })
                            .show();



                }
            });



        } else {

        }
    }

    private void handleFailure(IOException e) {
        Log.e(TAG, "Exception: " + e.getMessage());
    }



}
