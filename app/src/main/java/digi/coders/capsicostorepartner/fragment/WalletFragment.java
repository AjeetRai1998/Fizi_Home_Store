package digi.coders.capsicostorepartner.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.razorpay.Checkout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.KYCActivity;
import digi.coders.capsicostorepartner.activity.KYCStatusActivity;
import digi.coders.capsicostorepartner.activity.WithDrawFormActivity;
import digi.coders.capsicostorepartner.adapter.TransactionAdapter;
import digi.coders.capsicostorepartner.databinding.FragmentWalletBinding;
import digi.coders.capsicostorepartner.helper.MyApi;
import digi.coders.capsicostorepartner.model.KycData;
import digi.coders.capsicostorepartner.model.ShowProgress;
import digi.coders.capsicostorepartner.model.Transaction;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment {
    FragmentWalletBinding binding;
    private SingleTask singleTask;
    private List<Transaction> list=new ArrayList<>();
    int page=0;
    int scrollStatus=0,scrollStatusForData=0;
    TransactionAdapter transactionAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentWalletBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        singleTask=(SingleTask)getActivity().getApplication();
        //withdraw money
        Glide.with(getActivity()).load(R.raw.loading).into(binding.loading);
        binding.withdrawMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withDrawMoney();
            }
        });
        //transaction list

        binding.mainLayout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                // on scroll change we are checking when users scroll as bottom.
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    if(scrollStatusForData==0) {
                        if (scrollStatus == 0) {
                            scrollStatus = 1;
                            page = page + 25;
                            loadTrsactionList();
                        }
                    }
                }
            }
        });


        binding.transactionList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        transactionAdapter=new TransactionAdapter(list);
        binding.transactionList.setAdapter(transactionAdapter);
        loadTrsactionList();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTrsactionList();
    }

    private void withDrawMoney() {
        ShowProgress.getShowProgress(getActivity()).show();
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.kycStatus(vendor.getId());
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful())
                {

                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject= jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            JSONObject jsonObject1=jsonObject.getJSONObject("data");
                            KycData kycData=new Gson().fromJson(jsonObject1.toString(),KycData.class);
                            if(kycData.getIsStatus().equals("pending"))
                            {

                                startActivity(new Intent(getActivity(), KYCStatusActivity.class));
                            }
                            else if(kycData.getIsStatus().equals("verified"))
                            {
                                startActivity(new Intent(getActivity(), WithDrawFormActivity.class));
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "You kyc reject by Admin Retry again ?", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), KYCActivity.class));
                            }

                        }
                        else
                        {
                            startActivity(new Intent(getActivity(), KYCActivity.class));
                            ShowProgress.getShowProgress(getActivity()).hide();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();
                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadTrsactionList() {
        binding.loading.setVisibility(View.VISIBLE);
        String ven=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(ven,Vendor.class);
        MyApi myApi=singleTask.getRetrofit().create(MyApi.class);
        Call<JsonArray> call=myApi.getTransactionHistory(vendor.getId(),page+"");
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                if(response.isSuccessful())
                {
                    try {
                        JSONArray jsonArray=new JSONArray(new Gson().toJson(response.body()));
                        JSONObject jsonObject= jsonArray.getJSONObject(0);
                        String res=jsonObject.getString("res");
                        String msg=jsonObject.getString("message");
                        String wallet=jsonObject.getString("wallet");
                        binding.walletMoney.setText("₹ "+wallet);
                        if(res.equals("success"))
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            JSONArray jsonArray1=jsonObject.getJSONArray("data");

                            if(jsonArray1.length()>0)
                            {
                                list=new ArrayList<>();
                                for(int i=0;i<jsonArray1.length();i++)
                                {
                                    Transaction transaction=new Gson().fromJson(jsonArray1.getJSONObject(i).toString(),Transaction.class);
                                    list.add(transaction);
                                }

                                transactionAdapter.addData(list);
                                scrollStatus=0;
                            }
                            else
                            {
                                ShowProgress.getShowProgress(getActivity()).hide();
                                binding.noTxt.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                            }
                        }
                        else
                        {
                            ShowProgress.getShowProgress(getActivity()).hide();
                            binding.noTxt.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                binding.loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                ShowProgress.getShowProgress(getActivity()).hide();
                binding.loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(),t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
