package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.OrderItemsLayoutBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.model.Orderproduct;


public class OrderItemsAdapter extends RecyclerView.Adapter<OrderItemsAdapter.MyHolder> {


    Orderproduct[] orderItems;
    Context mContext;
    int status;

    public OrderItemsAdapter(Orderproduct[] orderItems, Context mContext,int status) {
        this.orderItems = orderItems;
        this.mContext = mContext;
        this.status = status;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_items_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        holder.binding.productNameWithQty.setText(orderItems[position].getQty()+" x "+orderItems[position].getName());
        if(status==0) {
            holder.binding.cardImage.setVisibility(View.VISIBLE);
            holder.binding.productPrice.setVisibility(View.GONE);
            Picasso.get().load(Constraints.BASE_URL + Constraints.MASTER_PRODUCT + orderItems[position].getIcon()).into(holder.binding.images);
        }else  if(status==2) {
            holder.binding.cardImage.setVisibility(View.VISIBLE);
            holder.binding.productPrice.setVisibility(View.VISIBLE);
            Picasso.get().load(Constraints.BASE_URL + Constraints.MASTER_PRODUCT + orderItems[position].getIcon()).into(holder.binding.images);
        }else{
            holder.binding.cardImage.setVisibility(View.GONE);
            holder.binding.productPrice.setVisibility(View.VISIBLE);
        }
        if(!orderItems[position].getQty().equalsIgnoreCase("")){
            if(!orderItems[position].getPrice().equalsIgnoreCase("")){
                if(!orderItems[position].getAddonproduct_prize1().equalsIgnoreCase("")){
                    holder.binding.productPrice.setText("\u20b9"+(Integer.parseInt(orderItems[position].getQty())*(Double.parseDouble(orderItems[position].getPrice())+Double.parseDouble(orderItems[position].getAddonproduct_prize1()))));

                }else{
                    holder.binding.productPrice.setText("\u20b9"+(Integer.parseInt(orderItems[position].getQty())*(Double.parseDouble(orderItems[position].getPrice()))));

                }
            }
        }

        if(orderItems[position].getAddonproductname()!=null) {
            if (!orderItems[position].getAddonproductname().equalsIgnoreCase("[]")&&!orderItems[position].getAddonproductname().equalsIgnoreCase("")) {
                String[] str = orderItems[position].getAddonproductname().split(",");
                String addproduct = "";
                for (int i = 0; i < str.length; i++) {

                    if (i == 0) {
                        addproduct = "(" + str[i];
                    } else {
                        addproduct = addproduct + ") (" + str[i];
                    }

                }
                holder.binding.addonItems.setText(addproduct + ")");
                holder.binding.addonItems.setVisibility(View.VISIBLE);
            } else {
                holder.binding.addonItems.setVisibility(View.GONE);
            }
        }else {
            holder.binding.addonItems.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderItems.length;
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        OrderItemsLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=OrderItemsLayoutBinding.bind(itemView);
        }
    }
}
