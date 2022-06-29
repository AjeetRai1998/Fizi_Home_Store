package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.capsicostorepartner.databinding.ActivityOtpBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivity extends AppCompatActivity {

    ActivityOtpBinding binding;
    private int key;
    private String mobile;
    private SingleTask singleTask;
    private ProgressDialog progressDialog;
    private String otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        mobile=getIntent().getStringExtra("mobile");
        binding.mobileNo.setText("+91 "+mobile);
        binding.otpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                key=getIntent().getIntExtra("key",0);
                if(key==1)
                {

                    verifyOtp();

                }
                else
                {


                    verifyOtp();

                    //startActivity(new Intent(OtpActivity.this, DashboardActivity.class));
                }

            }
        });


        //get Key


    binding.goBack.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

    binding.resendOtp.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressDialog.show();
            MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
            Call<JsonArray> call=myApi.resendOtp(mobile);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if(response.isSuccessful())
                    {
                        try {
                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1= jsonArray.getJSONObject(0);
                            String res=jsonObject1.getString("res");
                            String msg=jsonObject1.getString("message");
                            if(res.equals("success"))
                            {
                                progressDialog.hide();
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressDialog.hide();
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {
                    progressDialog.hide();
                    Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    });

    }

    private void verifyOtp() {
        otp = binding.otpView.getText().toString();
        if (otp.isEmpty()) {
            Toast.makeText(OtpActivity.this, "Please Enter your otp", Toast.LENGTH_SHORT).show();

        } else if (otp.length() < 6) {
            Toast.makeText(OtpActivity.this, "Please Enter 6 digit otp", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.show();
            MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
            Call<JsonArray> call=myApi.otpVerification(mobile,otp);
            call.enqueue(new Callback<JsonArray>() {
                @Override
                public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                    if(response.isSuccessful()) {
                        try {
                            JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                            JSONObject jsonObject1=jsonArray.getJSONObject(0);
                            String res=jsonObject1.getString("res");
                            String msg=jsonObject1.getString("message");
                            if(res.equals("success")) {
                                if(key==1)
                                {

                                    progressDialog.hide();
                                    Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    Intent in=new Intent(OtpActivity.this,ResetPasswordActivity.class);
                                    in.putExtra("mobile",mobile);
                                    startActivity(in);
                                }
                                else {
                                    progressDialog.hide();
                                    JSONArray jsonArray1 = jsonObject1.getJSONArray("data");
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                    singleTask.addValue("vendor", jsonObject2);
                                    startActivity(new Intent(OtpActivity.this, DashboardActivity.class));
                                    finish();
                                    Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {

                                progressDialog.hide();
                                Toast.makeText(OtpActivity.this, msg, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }

                @Override
                public void onFailure(Call<JsonArray> call, Throwable t) {

                    progressDialog.hide();
                    Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


    }

