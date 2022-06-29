package digi.coders.capsicostorepartner.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ActivityDocumentBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.model.MerchantCategory;
import digi.coders.capsicostorepartner.model.Vendor;
import digi.coders.capsicostorepartner.singletask.SingleTask;

public class DocumentActivity extends AppCompatActivity {

    ActivityDocumentBinding binding;
    private SingleTask singleTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDocumentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        singleTask=(SingleTask)getApplication();
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setData();

        //handle store type spinner

    }

    private void setData() {
        String js=singleTask.getValue("vendor");
        Vendor vendor=new Gson().fromJson(js,Vendor.class);
        Log.e("sdsd",Constraints.BASE_URL+Constraints.MERCHANT+vendor.getIcon());
        Picasso.get().load(Constraints.BASE_URL+Constraints.MERCHANT+vendor.getIcon()).into(binding.storeImage);
        Picasso.get().load(Constraints.BASE_URL+Constraints.MERCHANT+vendor.getProofPhoto()).into(binding.establishmentPhoto);
        Picasso.get().load(Constraints.BASE_URL+Constraints.MERCHANT+vendor.getOwnerphotoFront()).into(binding.idFrontPhoto);
        Picasso.get().load(Constraints.BASE_URL+Constraints.MERCHANT+vendor.getOwnerphotoBack()).into(binding.idBackPhoto);



        MerchantCategory category=new MerchantCategory();
        if(vendor.getMerchantCategoryId().equals(category.getId()))
        {

        }

        binding.storetype.setSelection(2);
        binding.storetype.setEnabled(false);


        //handle establishment proof
        String[] d=getResources().getStringArray(R.array.esablishmentproof);
        for(int i=0;i<d.length;i++)
        {
            if(vendor.getStoreproofType().equals(d[i]))
            {
                binding.establishmentProof.setSelection(i);
                binding.establishmentProof.setEnabled(false);
            }
        }


        //handle id proof
        String[] w=getResources().getStringArray(R.array.idproof);
        for(int i=0;i<w.length;i++)
        {
            if(vendor.getOwnerproofType().equals(w[i]))
            {
                binding.idProof.setSelection(i);
                binding.idProof.setEnabled(false);
            }
        }



    }
}