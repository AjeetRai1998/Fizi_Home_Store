package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.AddOnDesignBinding;
import digi.coders.capsicostorepartner.model.AddOnProduct;

public class GroupAdOnAdapter extends RecyclerView.Adapter<GroupAdOnAdapter.MyHolder> {
    private Context ctx;
    private AddOnProduct[] addOnProducts;

    public GroupAdOnAdapter(AddOnProduct[] addOnProducts) {
        this.addOnProducts = addOnProducts;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        AddOnProduct addOnProduct=addOnProducts[position];
        holder.binding.addOnName.setText(addOnProduct.getName());
        if(addOnProduct.getType().equals("veg"))
        {
            holder.binding.productImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.vej));
        }
        else
        {
            holder.binding.productImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.non));
        }

    }

    @Override
    public int getItemCount() {
        return addOnProducts.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        AddOnDesignBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=AddOnDesignBinding.bind(itemView);
        }
    }
}
