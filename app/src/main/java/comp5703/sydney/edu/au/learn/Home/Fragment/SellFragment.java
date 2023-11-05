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
import android.database.Cursor;
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

import androidx.activity.result.PickVisualMediaRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.itheima.wheelpicker.WheelPicker;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
import comp5703.sydney.edu.au.learn.util.FormValidator;
import comp5703.sydney.edu.au.learn.util.NetworkUtils;
import comp5703.sydney.edu.au.learn.util.WeightConverter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SellFragment extends Fragment {

    private EditText editTitle;
    private EditText editDescription;
    private EditText editWeight;
    private EditText editPrice;

    private AppCompatButton btnSubmit;
    private List<String> uploadImageUrls = new ArrayList<>();
    private AutoCompleteTextView autoCompleteTextView;

    private AutoCompleteTextView autoCompleteTextView2;

    private TextInputLayout textInputLayout;
    private TextInputLayout textInputLayout2;

    private Uri photoURI;
    private File photoFile;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;


    private static final int REQUEST_GALLERY_PERMISSION = 3;
    private static final int REQUEST_GALLERY_IMAGE = 4;

    WheelPicker wheelPicker;

    private ImageAdapter imageAdapter;

    private Integer productId;

    private MaterialCheckBox materialCheckBox;

    private TextView chooseUnit;

    private String selectType;

    private Fragment itemDetailFragment;

    private String userChooseUnit = "g";

    private TextInputLayout inputLayoutTitle, inputLayoutDescription, inputLayoutWeight, inputLayoutPrice;


    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_sell, container, false);
        return rootView;
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

        inputLayoutTitle = view.findViewById(R.id.inputLayoutTitle);
        inputLayoutDescription = view.findViewById(R.id.inputLayoutDescription);
        inputLayoutWeight = view.findViewById(R.id.inputLayoutWeight);
        inputLayoutPrice = view.findViewById(R.id.inputLayoutPrice);

        String[] items2 = new String[] {"gold", "silver"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(
                getContext(),
                R.layout.my_dropdown_item,  // 使用自定义布局
                items2
        );

        autoCompleteTextView2 = view.findViewById(R.id.autoCompleteTextView2);
        autoCompleteTextView2.setAdapter(adapter2);


        // 这个布局控制选择商品种类
        autoCompleteTextView = view.findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                Log.d("AutoComplete", "Selected item: " + selectedItem);

                selectType = selectedItem;

                // 设置第二个 AutoCompleteTextView 的提示文本
                if (selectType.equals("gold")){

                    // 用户选择商品类型为金子
                    autoCompleteTextView.setHint("Please select gold type");


                    String[] items = new String[] {"24K","22K", "21K", "18K"};
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getContext(),
                            R.layout.my_dropdown_item,  // 使用自定义布局
                            items
                    );
                    autoCompleteTextView.setAdapter(adapter);
                }else {

                    // 用户选择商品类型为银
                    autoCompleteTextView.setHint("Please select silver type");


                    String[] items = new String[] {"999", "995", "925", "990"};
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getContext(),
                            R.layout.my_dropdown_item,  // 使用自定义布局
                            items
                    );

                    autoCompleteTextView.setAdapter(adapter);
                }

                autoCompleteTextView.setText("");   // 清除文本
                autoCompleteTextView.clearFocus();  // 清除焦点
                textInputLayout.setVisibility(View.VISIBLE);
            }
        });




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

        // 删除图片的逻辑
        imageAdapter.setOnImageLongClickListener(new ImageAdapter.OnImageLongClickListener() {
            @Override
            public void onImageLongClick(int position) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 实际的删除逻辑
                        if (position >= 0 && position < uploadImageUrls.size()) {
                            uploadImageUrls.remove(position);
                            imageAdapter.removeImageUrl(position);
                        }
                    }
                });

            }
        });



        // 设置数据
        List<String> data = Arrays.asList("g", "kg", "oz");
        wheelPicker.setData(data);
        wheelPicker.setItemTextSize(40);
        wheelPicker.setVisibleItemCount(3);  // 假设5是一个合适的可见项数

        wheelPicker.setMaximumWidthTextPosition(2);
        wheelPicker.setItemSpace(20);  // 假设5是一个合适的可见项数
        // 设置选定项目的监听器,111111
        wheelPicker.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                // 处理选定项目的事件
                System.out.println(position +":::"+data);
                userChooseUnit = data.toString();


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
                        autoCompleteTextView.setText(product.getPurity());
                        userChooseUnit = "g";
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


                        // 把图片链接字符串转回数组
                        String[] items = product.getProductImage().substring(1, product.getProductImage().length() - 1).split(", ");

                        List<String> imageNameList = new ArrayList<>();
                        for (String item : items) {
                            // 将图片数组插入adapter
                            imageAdapter.addImageUrl(imageURL + item);
                            imageNameList.add(item);
                        }


                        uploadImageUrls = imageNameList;

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


    public void openCameraClick(){
        CharSequence[] items = {"Take picture", "Select from gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please choose the picture source");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // 拍照
                        requestCameraPermission();
                        break;
                    case 1: // 从相册选择
                        requestGalleryPermission();
                        break;
                }
            }
        });
        builder.show();

    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{ Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            launchCamera();
        }
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions( new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION);
        } else {
            launchGallery();
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
                Snackbar.make(rootView, "You need to enable camera permissions for subsequent operations", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }

        // 请求相册时候结果的处理
        if (requestCode == REQUEST_GALLERY_PERMISSION) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                launchGallery();
//            } else {
//                // 权限被拒绝，添加额外的处理逻辑。
//                Snackbar.make(rootView, "You need to enable album permissions to proceed.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
            launchGallery();
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

    private void launchGallery() {
//        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(pickPhoto, REQUEST_GALLERY_IMAGE);

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        // pass the constant to compare it
        // with the returned requestCode
        startActivityForResult(Intent.createChooser(i, "Select Picture"), REQUEST_GALLERY_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            uploadPhoto(photoFile);

        }else if (requestCode == REQUEST_GALLERY_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data != null) {

                Uri selectedImageUri = data.getData();
                uploadPhotoFromUri(selectedImageUri);
            }
        }
    }

    private void uploadPhotoFromUri(Uri imageUri) {
        try {
            InputStream inputStream = getActivity().getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                // 创建一个临时文件来存储图片
                File photoFile = new File(getActivity().getCacheDir(), "temp_image.jpg");
                try (OutputStream outputStream = new FileOutputStream(photoFile)) {
                    // 将输入流的内容复制到文件
                    byte[] buffer = new byte[4 * 1024]; // or other buffer size
                    int read;
                    while ((read = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, read);
                    }
                    outputStream.flush();
                }
                // 调用现有的上传方法
                uploadPhoto(photoFile);
            }
        } catch (IOException e) {
            Log.e(TAG, "File upload failed: " + e.getMessage(), e);
        }
    }


    private void uploadPhoto(File photoFile){
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
                    String useImageUrl = jsonObject.getString("data");

                    // 往image数组里添加图片链接
                    uploadImageUrls.add(useImageUrl);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 使用ImageAdapter的实例添加新的URL
                            imageAdapter.addImageUrl(imageURL + useImageUrl);
                        }
                    });

                }
            }
        });

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

        boolean isValid = FormValidator.validateTextInputLayout(inputLayoutTitle, editTitle.getText().toString(), "Title can`t be empty")
                & FormValidator.validateTextInputLayout(inputLayoutDescription,  editDescription.getText().toString(), "Description can`t be empty")
                & FormValidator.validateTextInputLayoutAsFloat(
                        inputLayoutWeight,
                editWeight.getText().toString(),
                "Weight can`t be empty",
                "Please enter a valid number")
                & FormValidator.validateTextInputLayoutAsFloat(
                        inputLayoutPrice,
                editPrice.getText().toString(),
                "price can`t be empty",
                "Please enter a valid number");

        if (uploadImageUrls.isEmpty()){
            isValid = false;
            Snackbar.make(rootView, "You need upload at least one picture for the product", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }

        if (isValid){
            // title value
            String itemTitle = editTitle.getText().toString();

            // description
            String itemDescription = editDescription.getText().toString();

            // weight
            String itemWeight = editWeight.getText().toString();


            // 根据单元进行重量转化
            double standWeight;
            if (userChooseUnit.equals("oz")){
                standWeight = WeightConverter.ozToGrams(Double.parseDouble(itemWeight));
            }else if (userChooseUnit.equals("kg")){
                standWeight = WeightConverter.kgToGrams(Double.parseDouble(itemWeight));

            }else {
                standWeight = Double.parseDouble(itemWeight);
            }

            // drop box value
            String selectedCategory = autoCompleteTextView2.getText().toString();  // 获取选择的种类，比如“gold”或“silver”
            String selectedPurity = autoCompleteTextView.getText().toString();     // 获取对应的纯度，如"24K"、"999"等

            String itemPrice = editPrice.getText().toString();

            boolean hiddenPrice = materialCheckBox.isChecked();

            // get SharedPreferences instance
            SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("comp5703", Context.MODE_PRIVATE);

            // get global userID
            Integer userId = sharedPreferences.getInt("userId", 9999);


            /**
             *
             * 上传product的表单
             */
            ItemVO itemVO = new ItemVO();

            if (productId !=null){
                itemVO.setProductId(productId);
            }
            itemVO.setItemTitle(itemTitle);
            itemVO.setItemDescription(itemDescription);
            itemVO.setItemWeight(standWeight);
            itemVO.setItemPrice(Double.parseDouble(itemPrice));
            itemVO.setItemPurity(selectedPurity);

            if (selectedCategory.equals("gold")){
                itemVO.setCategory(1);
            }else {
                itemVO.setCategory(2);
            }

            if (hiddenPrice){
                itemVO.setHiddenPrice(1);
            }else {
                itemVO.setHiddenPrice(0);
            }

            // 将图片url数组转为字符串
            itemVO.setImageUrl(uploadImageUrls.toString());
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

    @Override
    public void onResume() {
        super.onResume();
        // 当Fragment重新变为活动状态时更新Toolbar
        ((HomeUseActivity) getActivity()).updateToolbar(true, "Selling An Item");
    }

}
