package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.AddOnProductLayoutBinding;
import digi.coders.capsicostorepartner.model.AddOnGroup;
import digi.coders.capsicostorepartner.model.AddOnProduct;

public class AddOnProductAdapter extends RecyclerView.Adapter<AddOnProductAdapter.MyHolder> {

    private List<AddOnProduct> list;
    private Context ctx;
    private GetPosition getPosition;
    private int i=0;
    private int key;
    private AddOnGroup addOnGroup;


    public AddOnProductAdapter(List<AddOnProduct> list,int key,AddOnGroup addOnGroup) {
        this.list = list;
        this.key=key;
        this.addOnGroup=addOnGroup;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.add_on_product_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        AddOnProduct addOnProduct=list.get(position);
        holder.binding.productName.setText(addOnProduct.getName());
        //holder.binding.checkBox.setClickable(false);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i==0)
                {
                    holder.binding.checkBox.setChecked(true);
                    i++;
                }
                else
                {
                    holder.binding.checkBox.setChecked(false);
                    i=0;
                }

                Toast.makeText(ctx, "sdsd", Toast.LENGTH_SHORT).show();
            }
        });
        //holder.binding.productPrice.setText("₹ "+addOnProduct.getPrice());
        if(key==1)
        {
            holder.binding.checkBox.setVisibility(View.GONE);
            holder.binding.productImage.setVisibility(View.VISIBLE);
            holder.binding.editAddOn.setVisibility(View.VISIBLE);
            if(addOnProduct.getType().equals("veg"))
            {
                holder.binding.productImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.vej));
            }
            else
            {
                holder.binding.productImage.setImageDrawable(ctx.getResources().getDrawable(R.drawable.non));
            }
            holder.binding.editAddOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPosition.findPosition(position,addOnProduct,holder.binding);
                }
            });
        }

        else
        {
            holder.binding.checkBox.setVisibility(View.VISIBLE);
            holder.binding.checkBox.setClickable(false);
            holder.binding.productImage.setVisibility(View.GONE);
            holder.binding.editAddOn.setVisibility(View.GONE);
            if(addOnGroup!=null)
            {
                String[] s=addOnGroup.getAddproductId().split(",");
                Log.e("dsd",s.length+"");
                for(int i=0;i<s.length;i++)
                {
                    if(s[i].equals(list.get(position).getId()))
                    {
                        holder.binding.checkBox.setChecked(true);
                    }

                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getPosition.findPosition(position,addOnProduct,holder.binding);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void getAdapterPosition(GetPosition getPosition)
    {
        this.getPosition=getPosition;

    }
    public interface  GetPosition
    {
        void findPosition(int position,AddOnProduct addOnProduct,AddOnProductLayoutBinding binding);
    }
    public class MyHolder extends RecyclerView.ViewHolder
    {
        AddOnProductLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=AddOnProductLayoutBinding.bind(itemView);
        }

    }
}
