package digi.coders.capsicostorepartner.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.PaymentMethodDesignBinding;
import digi.coders.capsicostorepartner.model.PaymentModeImage;


public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.MyHolde> {

    private List<PaymentModeImage> list;
    private Context ctx;
    private int lastCheckedPosition = 4;
    private int row_index=0;
    private GetPosition getPosition;
    public PaymentMethodAdapter(List<PaymentModeImage> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public MyHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_method_design,parent,false);
        return new MyHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolde holder, @SuppressLint("RecyclerView") int position) {
        PaymentModeImage image=list.get(position);
        holder.binding.paymentImage.setImageDrawable(ctx.getDrawable(image.getImage()));
        holder.binding.radioButton.setChecked(position == lastCheckedPosition);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void findMyPosition(GetPosition getPosition)
    {

        this.getPosition=getPosition;
    }

    public interface GetPosition
    {
        void click(View v,int position);

    }

    public class MyHolde extends RecyclerView.ViewHolder
    {
        PaymentMethodDesignBinding binding;
        public MyHolde(@NonNull View itemView) {
            super(itemView);
            binding=PaymentMethodDesignBinding.bind(itemView);
            binding.radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);
                    getPosition.click(v,lastCheckedPosition);
                }
            });
        }
    }
}
