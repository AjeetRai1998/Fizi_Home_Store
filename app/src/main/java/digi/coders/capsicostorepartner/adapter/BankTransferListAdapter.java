package digi.coders.capsicostorepartner.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.BankProofBinding;
import digi.coders.capsicostorepartner.databinding.OrderItemsLayoutBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.model.Orderproduct;
import digi.coders.capsicostorepartner.model.ProofModel.DataItem;


public class BankTransferListAdapter extends RecyclerView.Adapter<BankTransferListAdapter.MyHolder> {


    List<DataItem> orderItems;
    Context mContext;

    public BankTransferListAdapter(List<DataItem> orderItems, Context mContext) {
        this.orderItems = orderItems;
        this.mContext = mContext;
    }

    @NonNull
    @NotNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bank_proof,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyHolder holder, int position) {

        holder.binding.amount.setText("Payment of \u20b9"+orderItems.get(position).getAmount());
        holder.binding.date.setText(orderItems.get(position).getDate());
        holder.binding.remark.setText(orderItems.get(position).getRemark());
        Picasso.get().load(Constraints.BASE_URL+"merchant/"+orderItems.get(position).getImage()).into(holder.binding.image);

        holder.binding.download.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DownloadManager downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(Constraints.BASE_URL+"merchant/"+orderItems.get(position).getImage());
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle("Payment Proof");
                        request.setDescription("Downloading");
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                        request.setDestinationUri(Uri.parse("file://" + folderName + "/myfile.mp3"));
                        downloadmanager.enqueue(request);
                    }
                }
        );
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        BankProofBinding binding;
        public MyHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            binding=BankProofBinding.bind(itemView);
        }
    }
}
