package digi.coders.capsicostorepartner.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityKycactivityBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KYCActivity extends AppCompatActivity {

    ActivityKycactivityBinding binding;
    private SingleTask singleTask;
    private String aadharFront="",aadharBack="",panPhoto="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityKycactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        //handle next button
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid())
                {
                    ShowProgress.getShowProgress(KYCActivity.this).show();
                    String ven=singleTask.getValue("vendor");
                    Vendor vendor=new Gson().fromJson(ven,Vendor.class);
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);

                    Call<JsonArray> call=myApi.kyc(vendor.getId(),bankName,ifscCode,panNo,aadahrNo,bankAccoutnNo,acHolderName,branch,panPhoto,binding.vpa.getEditText().getText().toString(),aadharFront);
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if(response.isSuccessful())
                            {
                                try {
                                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject=jsonArray.getJSONObject(0);
                                    String res=jsonObject.getString("res");
                                    String msg=jsonObject.getString("message");
                                    if(res.equals("success"))
                                    {
                                        ShowProgress.getShowProgress(KYCActivity.this).hide();
                                        Toast.makeText(KYCActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(KYCActivity.this,KYCStatusActivity.class));
                                        finish();
                                    }
                                    else
                                    {
                                        ShowProgress.getShowProgress(KYCActivity.this).hide();
                                        Toast.makeText(KYCActivity.this, msg, Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            ShowProgress.getShowProgress(KYCActivity.this).hide();
                            Toast.makeText(KYCActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

            }
        });



        //handle back button
        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //handle adhar card proof
        binding.addAdharFrontPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery(1);
            }
        });
        
        binding.addAadharBackPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gallery(2);
            }
        });


        //handle bank proof
        /*binding.addBankProof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallery(3);
            }
        });*/

        //handle pancard
        binding.addFrotPan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gallery(4);
            }
        });
    }

    private String bankName,bankAccoutnNo,acHolderName,ifscCode,aadahrNo,panNo,branch;
    private boolean valid() {
        bankName=binding.bankName.getEditText().getText().toString();
        bankAccoutnNo=binding.bankAccountNo.getEditText().getText().toString();
        acHolderName=binding.accountHolderName.getEditText().getText().toString();
        ifscCode=binding.ifscCode.getEditText().getText().toString();
        aadahrNo=binding.aadharNo.getEditText().getText().toString();
        panNo=binding.panNo.getEditText().getText().toString();
        branch=binding.bankBranch.getEditText().getText().toString();
        if(TextUtils.isEmpty(bankName))
        {
            binding.bankName.setError("Please Enter Bank Name");
            binding.bankName.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(bankAccoutnNo))
        {
            binding.bankAccountNo.setError("Please Enter Bank Account no");
            binding.bankAccountNo.requestFocus();
            return  false;

        }
        else if(TextUtils.isEmpty(acHolderName))
        {
            binding.accountHolderName.setError("Please Enter Account Holder Name");
            binding.accountHolderName.requestFocus();
            return false;

        }
        else if(TextUtils.isEmpty(branch))
        {
            binding.bankBranch.setError("Please Enter your Branch");
            binding.bankBranch.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(ifscCode))
        {
            binding.ifscCode.setError("Please Enter Ifsc Code");
            binding.ifscCode.requestFocus();
            return false;

        }
        else if(TextUtils.isEmpty(aadahrNo))
        {
            binding.aadharNo.setError("Please Enter aadhar No");
            binding.aadharNo.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(panNo))
        {
            binding.panNo.setError("Please Enter Pan No");
            binding.panNo.requestFocus();
            return  false;

        }
        else if(aadharFront.equals(""))
        {
            Toast.makeText(KYCActivity.this, "Please Upload  Front Photo of Aadhar", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(aadharBack.equals(""))
        {
            Toast.makeText(KYCActivity.this, "Please Upload  Back Photo of Aadhar", Toast.LENGTH_SHORT).show();
            return false;

        }
        else if(panPhoto.equals(""))
        {
            Toast.makeText(KYCActivity.this, "Please Upload  Pan Photo ", Toast.LENGTH_SHORT).show();
            return false;

        }
        else
        {
            return true;
        }
    }

    private void gallery(int i) {
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
        else
        {
            openGallery(i);
        }
    }

    private void openGallery(int i) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),i);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && data!=null)
        {
            handleGalleryPic(requestCode,data);
        }
    }

    private void handleGalleryPic(int i, Intent data) {
        switch (i)
        {
            case 1:
                binding.aadharFrontPhoto.setImageURI(data.getData());
                try {
                    InputStream iStream=getContentResolver().openInputStream(data.getData());
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        aadharFront= Base64.getEncoder().encodeToString(bb);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                binding.aadharFrontPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(KYCActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });

                break;

            case 2:
                binding.aadharBackPhoto.setImageURI(data.getData());
                try {
                    InputStream iStream=getContentResolver().openInputStream(data.getData());
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        aadharBack= Base64.getEncoder().encodeToString(bb);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                binding.aadharBackPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(KYCActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });
                break;
            case 3:
/*                binding.bankProof.setImageURI(data.getData());
                binding.bankProof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(KYCActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });*/

                break;
            case 4:
                binding.frontPan.setImageURI(data.getData());
                try {
                    InputStream iStream=getContentResolver().openInputStream(data.getData());
                    Bitmap bit= BitmapFactory.decodeStream(iStream);
                    ByteArrayOutputStream b=new ByteArrayOutputStream();
                    bit.compress(Bitmap.CompressFormat.JPEG,50,b);
                    byte[] bb=b.toByteArray();
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        panPhoto= Base64.getEncoder().encodeToString(bb);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                binding.frontPan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent in=new Intent(KYCActivity.this,FullImageViewActivity.class);
                        Uri uri=data.getData();
                        in.putExtra("data",uri.toString());
                        startActivity(in);
                    }
                });

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100 && grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},100);
        }
    }
}