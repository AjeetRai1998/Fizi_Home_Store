package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.AddProductFormActivity;
import digi.coders.capsicostorepartner.databinding.MasterProductLayoutBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.model.Product;

public class MasterProductAdapter extends RecyclerView.Adapter<MasterProductAdapter.MyHolder> {

    private Context ctx;
    private List<Product> list;

    public MasterProductAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.master_product_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Product product =list.get(position);
        Picasso.get().load(Constraints.BASE_URL+Constraints.MASTER_PRODUCT+ product.getIcon()).into(holder.binding.masterProductImage);
        holder.binding.masterProductName.setText(product.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProductFormActivity.product=product;
                Intent intent=new Intent(ctx, AddProductFormActivity.class);
                intent.putExtra("key",1);

                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder
    {

        MasterProductLayoutBinding binding;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            binding=MasterProductLayoutBinding.bind(itemView);
        }

    }
}
