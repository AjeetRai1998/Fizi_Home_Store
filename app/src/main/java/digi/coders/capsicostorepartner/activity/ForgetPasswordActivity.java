package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.capsicostorepartner.databinding.ActivityForgetPasswordBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity {
            
    ActivityForgetPasswordBinding binding;
    private SingleTask singleTask;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);



        //next
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobile=binding.phone.getEditText().getText().toString();
                if(mobile.isEmpty())
                {
                    binding.phone.setError("Please Enter Mobile no");
                    binding.phone.requestFocus();
                }
                else if(mobile.length()<10)
                {
                    binding.phone.setError("Please Enter 10 digit Mobile no");
                    binding.phone.requestFocus();
                }
                else
                {
                    progressDialog.show();
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=myApi.forgetPassword(mobile);
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
                                        Toast.makeText(ForgetPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        Intent in=new Intent(ForgetPasswordActivity.this, OtpActivity.class);
                                        in.putExtra("mobile",mobile);
                                        in.putExtra("key",1);
                                        startActivity(in);

                                    }
                                    else
                                    {
                                        progressDialog.hide();
                                        Toast.makeText(ForgetPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            progressDialog.hide();
                            Toast.makeText(ForgetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("error",t.getMessage());

                        }
                    });

                }







            }
        });
        binding.goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}