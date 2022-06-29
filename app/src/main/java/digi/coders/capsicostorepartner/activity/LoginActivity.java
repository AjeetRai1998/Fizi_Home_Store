package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.capsicostorepartner.databinding.ActivityLoginBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private SingleTask singleTask;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        //create an account
        binding.createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterationActivity.class));
            }
        });

        //login

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if(valid())
                {
                    progressDialog.show();
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=myApi.login(mobile,password);
                    call.enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            if(response.isSuccessful())
                            {
                                Log.e("sds",response.toString());
                                try {
                                    JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                                    JSONObject jsonObject1= jsonArray.getJSONObject(0);
                                    String res=jsonObject1.getString("res");
                                    String msg=jsonObject1.getString("message");
                                    if(res.equals("success"))
                                    {
                                        progressDialog.hide();
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        Intent in=new Intent(LoginActivity.this,OtpActivity.class);
                                        in.putExtra("mobile",mobile);
                                        startActivity(in);
                                        finish();

                                    }
                                    else
                                    {
                                        progressDialog.hide();
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            progressDialog.hide();
                            Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());

                        }
                    });
                }
            }
        });
        //forget password

        binding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            }
        });

    }

    private String mobile,password;
    private boolean valid() {
        mobile=binding.phone.getEditText().getText().toString();
        password=binding.vpassword.getEditText().getText().toString();
        if(TextUtils.isEmpty(mobile))
        {
            binding.phone.setError("Please Enter mobile no");
            binding.phone.requestFocus();
            return false;
        }
        else if(mobile.length()<10)
        {
            binding.phone.setError("Please Enter 10 digit mobile no");
            binding.phone.requestFocus();
            return false;
        }
        else if(TextUtils.isEmpty(password))
        {
            binding.vpassword.setError("Please Enter password");
            binding.vpassword.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
        
    }
}