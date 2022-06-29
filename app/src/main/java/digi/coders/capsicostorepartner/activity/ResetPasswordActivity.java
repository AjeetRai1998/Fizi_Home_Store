package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityResetPasswordBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {


    ActivityResetPasswordBinding  binding;
    private SingleTask singleTask;
    private ProgressDialog progressDialog;
    private String mobile;
    private  String newPassword,confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Waiting");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        mobile=getIntent().getStringExtra("mobile");
        //click on reset btn
        binding.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid())
                {
                    progressDialog.show();
                    MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
                    Call<JsonArray> call=myApi.resetPassword(mobile,newPassword);
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
                                        openBottomSheet(v);
                                    }
                                    else
                                    {
                                        progressDialog.hide();
                                        Toast.makeText(ResetPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            progressDialog.hide();
                            Toast.makeText(ResetPasswordActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                
                

            }
        });

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean valid() {
        newPassword = binding.newpassword.getEditText().getText().toString();
        confirmPassword = binding.confirmpassword.getEditText().getText().toString();
        if (newPassword.isEmpty()) {
            binding.newpassword.setError("Please Enter password");
            binding.newpassword.requestFocus();
            return false;
        } else if (confirmPassword.isEmpty()) {
            binding.confirmpassword.setError("Please Enter Confirm password");
            binding.confirmpassword.requestFocus();
            return false;
        }
        else if(!confirmPassword.equals(newPassword))
        {
            binding.confirmpassword.setError("Password not match");
            binding.confirmpassword.requestFocus();
            return false;


        }
        else
        {
            return  true;
        }

    }

    private void openBottomSheet(View view) {
        final BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this, R.style.myBottomSheetDialogTheme);

        View view1= LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout,(RelativeLayout)findViewById(R.id.bottom_sheet_container),false);
        Button mybotton=view1.findViewById(R.id.ok);
        mybotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                finish();

            }
        });
        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.show();
    }

}