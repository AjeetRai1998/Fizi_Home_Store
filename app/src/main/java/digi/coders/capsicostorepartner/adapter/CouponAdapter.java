package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.activity.AddCouponActivity;
import digi.coders.capsicostorepartner.databinding.CouponLayoutBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.model.Coupon;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.MyHolder> {
    private List<Coupon> couponList;
    private Context ctx;
    public CouponAdapter(List<Coupon> couponList) {
        this.couponList = couponList;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.coupon_layout,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {
        Coupon coupon=couponList.get(position);
        holder.binding.couponCode.setText(coupon.getCouponCode());
        holder.binding.discountType.setText(coupon.getType());
        holder.binding.expiryDate.setText(coupon.getExpiryDate());
        holder.binding.minimumPurchase.setText("₹ "+coupon.getMinimumPurchase());
        holder.binding.upTo.setText("₹"+coupon.getMaxAmountuse());
        Picasso.get().load(Constraints.BASE_URL+Constraints.COUPON+coupon.getIcon()).into(holder.binding.couponLogo);

        holder.binding.deleteCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCouponActivity.coupon=coupon;
                AddCouponActivity.status=1;
                ctx.startActivity(new Intent(ctx, AddCouponActivity.class));

            }
        });

    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        CouponLayoutBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=CouponLayoutBinding.bind(itemView);
        }
    }
}
