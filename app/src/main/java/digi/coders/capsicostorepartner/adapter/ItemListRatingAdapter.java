package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ItemRatingLayoutBinding;


public class ItemListRatingAdapter extends RecyclerView.Adapter<ItemListRatingAdapter.MyHolder> {


    String[] name;
    Context ctx;

    public ItemListRatingAdapter(String[] name, Context ctx){
        this.ctx=ctx;
        this.name=name;
    }
    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        holder.binding.name.setText(name[position].split("CAPSICO")[0]);
        if(name[position].split("CAPSICO").length>1) {
            holder.binding.rating.setText(name[position].split("CAPSICO")[1]);
        }else{
            holder.binding.rating.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ItemRatingLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding= ItemRatingLayoutBinding.bind(itemView);
        }
    }
}
