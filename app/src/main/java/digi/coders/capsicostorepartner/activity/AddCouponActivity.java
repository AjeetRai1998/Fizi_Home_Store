package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityAddCouponBinding;
import digi.coders.capsicostorepartner.helper.FilePath;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.Coupon;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCouponActivity extends AppCompatActivity {

    ActivityAddCouponBinding binding;
    private SingleTask singleTask;
    private String icon,couponCode,couponType,discount,minimumPurchase,maxAmount,expiryDate;
    public static Coupon coupon;
    public static  int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddCouponBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
           singleTask=(SingleTask)getApplication();

        binding.expiryDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar newCalendar = Calendar.getInstance();
                DatePickerDialog  datePickerDialog  = new DatePickerDialog(AddCouponActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        binding.expiryDate.getEditText().setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }

                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();

            }

        });

        setData();
        binding.addCouponPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();

            }
        });

        //add Coupon here...
        binding.addCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid())
                {
                    String ven=singleTask.getValue("vendor");
                    Vendor vendor=new Gson().fromJson(ven,Vendor.class);
                    ShowProgress.getShowProgress(AddCouponActivity.this).show();
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=null;
                    if(status==1)
                    {
                        call=myApi.addCoupon(coupon.getId(),vendor.getId(),couponCode,couponType,discount,minimumPurchase,maxAmount,expiryDate,icon);
                    }
                    else
                    {
                        call=myApi.addCoupon("",vendor.getId(),couponCode,couponType,discount,minimumPurchase,maxAmount,expiryDate,icon);
                    }

                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if(response!=null)
                            {
                                try {
                                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String res= jsonObject.getString("res");
                                    String msg=jsonObject.getString("msg");
                                    Log.e("sd",response.toString());
                                    if(res.equals("success"))
                                    {
                                        ShowProgress.getShowProgress(AddCouponActivity.this).hide();
                                        Toast.makeText(AddCouponActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        ShowProgress.getShowProgress(AddCouponActivity.this).hide();
                                        Toast.makeText(AddCouponActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            ShowProgress.getShowProgress(AddCouponActivity.this).hide();
                            Toast.makeText(AddCouponActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });



    }

    private void setData() {
        if(coupon!=null)
        {
            binding.couponCode.getEditText().setText(coupon.getCouponCode());
            binding.couponDiscount.getEditText().setText(coupon.getDiscount());
            binding.minimumPurchase.getEditText().setText(coupon.getMinimumPurchase());
            binding.maxAmount.getEditText().setText(coupon.getMaxAmountuse());
            binding.expiryDate.getEditText().setText(coupon.getExpiryDate());
            if(coupon.getType().equals("amount"))
            {
                binding.couponType.setSelection(2);
            }
            else if(coupon.getType().equals("percentage"))
            {
                binding.couponType.setSelection(1);
            }
            else
            {
                binding.couponType.setSelection(0);
            }
            binding.addCoupon.setText("Update Coupon");
        }
    }


    private boolean valid() {
        couponCode=binding.couponCode.getEditText().getText().toString();
        couponType=binding.couponType.getSelectedItem().toString();
        discount=binding.couponDiscount.getEditText().getText().toString();
        minimumPurchase=binding.minimumPurchase.getEditText().getText().toString();
        maxAmount=binding.maxAmount.getEditText().getText().toString();
        expiryDate=binding.expiryDate.getEditText().getText().toString();
        if(TextUtils.isEmpty(couponCode))
        {
            binding.couponCode.setError("Please Enter Coupon Code");
            binding.couponCode.requestFocus();
            return false;
        }
        else if(couponType.equals("Choose Discount Type"))
        {
            Toast.makeText(this, "Please Choose Discount Type", Toast.LENGTH_SHORT).show();

            return false;
        }
        else if(TextUtils.isEmpty(discount))
        {
            binding.couponDiscount.setError("Please Enter Coupon Discount");
            binding.couponDiscount.requestFocus();
            return false;
        }

        else if(TextUtils.isEmpty(minimumPurchase))
        {
            binding.minimumPurchase.setError("Please Enter Minimum Purchae");
            binding.minimumPurchase.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(maxAmount))
        {

            binding.maxAmount.setError("Please Enter Max Amount");
            binding.maxAmount.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(expiryDate))
        {
             Toast.makeText(AddCouponActivity.this, "Please Choose Date", Toast.LENGTH_SHORT).show();

            return false;
        }
        else if(TextUtils.isEmpty(icon)|| icon==null)
        {
            Toast.makeText(AddCouponActivity.this, "Please Choose Coupon Icon", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return  true;
        }


    }

    private void openGallery() {
        if(checkCallingOrSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
        else
        {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1001);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK && data != null) {
            binding.couponPhoto.setImageURI(data.getData());
            try {
                InputStream iStream=getContentResolver().openInputStream(data.getData());
                Bitmap bit= BitmapFactory.decodeStream(iStream);
                ByteArrayOutputStream b=new ByteArrayOutputStream();
                bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                byte[] bb=b.toByteArray();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    icon= Base64.getEncoder().encodeToString(bb);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            binding.couponPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(AddCouponActivity.this, FullImageViewActivity.class);
                    Uri uri = data.getData();
                    in.putExtra("data", uri.toString());
                    startActivity(in);
                }
            });

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {

            openGallery();
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);

        }
    }



}