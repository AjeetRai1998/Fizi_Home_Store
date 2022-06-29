package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.BankTransferListAdapter;
import digi.coders.capsicostorepartner.adapter.PaymentMethodAdapter;
import digi.coders.capsicostorepartner.adapter.TransactionAdapter;
import digi.coders.capsicostorepartner.databinding.ActivityWithDrawFormBinding;
import digi.coders.capsicostorepartner.fragment.HomeFragment;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.helper.NotificationService;
import digi.coders.capsicostorepartner.model.PaymentModeImage;
import digi.coders.capsicostorepartner.model.ProofModel.ResponseProof;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Transaction;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WithDrawFormActivity extends AppCompatActivity {

    ActivityWithDrawFormBinding binding;
    private List<PaymentModeImage> list;
    private SingleTask singleTask;
    String wallet="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWithDrawFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)WithDrawFormActivity.this.getApplication();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        loadPaymentMethodOption();
//        loadTransactionList();
        loadProof();
        binding.btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String withdrawAmount=binding.withdrawalMoney.getText().toString();
                if (withdrawAmount.equals("")){
                    binding.withdrawalMoney.setError("Enter Amount");
                    binding.withdrawalMoney.requestFocus();
                    return;
                }
                if (Double.parseDouble(wallet)>Double.parseDouble(withdrawAmount)){
                    withdrawal(withdrawAmount);
                }else {
                    Toast.makeText(getApplicationContext(), "You have not enough amount in your wallet.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void loadProof() {
        String ven = singleTask.getValue("vendor");
        Vendor vendor = new Gson().fromJson(ven, Vendor.class);
        MyApi myApi = singleTask.getRetrofit1().create(MyApi.class);
        Call<ResponseProof> call = myApi.getProof(vendor.getId(), "BankProofMerchant");
        call.enqueue(new Callback<ResponseProof>() {
            @Override
            public void onResponse(Call<ResponseProof> call, Response<ResponseProof> response) {
                if (response.isSuccessful()) {
                    try {
                      if(response.body().getRes().equalsIgnoreCase("success")){
                          binding.prooflist.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                          binding.prooflist.setHasFixedSize(true);
                          binding.prooflist.setAdapter(new BankTransferListAdapter(response.body().getData(),getApplicationContext()));
                        } else {
                          Toast.makeText(getApplicationContext(),response.body().getMsg(),Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseProof> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void loadPaymentMethodOption() {
        list=new ArrayList<>();
        list.add(new PaymentModeImage(R.drawable.phone_pay));
        list.add(new PaymentModeImage(R.drawable.google_pay));
        list.add(new PaymentModeImage(R.drawable.amazon));
        list.add(new PaymentModeImage(R.drawable.paytm));
        list.add(new PaymentModeImage(R.drawable.bank_icon));
        PaymentMethodAdapter adapter=new PaymentMethodAdapter(list);
        binding.paymentMethodOption.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        binding.paymentMethodOption.setAdapter(adapter);
        adapter.findMyPosition(new PaymentMethodAdapter.GetPosition() {
            @Override
            public void click(View v, int position) {
                if(position==0)
                {

                    binding.detailsForm.setVisibility(View.VISIBLE);
                    binding.bankDetails.setVisibility(View.GONE);
                }
                else if(position==1)
                {
                    binding.detailsForm.setVisibility(View.VISIBLE);
                    binding.bankDetails.setVisibility(View.GONE);

                }
                else if(position==2)
                {

                    binding.detailsForm.setVisibility(View.VISIBLE);
                    binding.bankDetails.setVisibility(View.GONE);
                }
                else if(position==3)
                {
                    binding.detailsForm.setVisibility(View.VISIBLE);
                    binding.bankDetails.setVisibility(View.GONE);
                }
                else
                {
                    binding.detailsForm.setVisibility(View.GONE);
                    binding.bankDetails.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void withdrawal(String amount) {
        ShowProgress.getShowProgress(WithDrawFormActivity.this).show();
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.withdrawal(vendor.getId(), amount);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {
                    Log.e("sddde",response.body().toString());

                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject= jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(getApplicationContext()).hide();
                            binding.withdrawalMoney.setText("");
                            finish();
                            Toast.makeText(WithDrawFormActivity.this, msg, Toast.LENGTH_SHORT).show();

                        } else {
                            ShowProgress.getShowProgress(getApplicationContext()).hide();
                            Toast.makeText(WithDrawFormActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        ShowProgress.getShowProgress(getApplicationContext()).hide();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getApplicationContext()).hide();
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}