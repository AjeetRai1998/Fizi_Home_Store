package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Layout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.databinding.ReviewDesignBinding;
import digi.coders.capsicostorepartner.helper.Constraints;
import digi.coders.capsicostorepartner.model.Review;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyHolder> {

    private List<Review> list;
    Context ctx;

    public ReviewAdapter(List<Review> list, Context ctx) {
        this.list = list;
        this.ctx = ctx;
    }
    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_design,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        Review review=list.get(position);
        holder.binding.name.setText(review.getUser_details().getName());
        if(!review.getRemark().equalsIgnoreCase("")) {
            holder.binding.review.setText(review.getRemark());
        }else{
            holder.binding.review.setVisibility(View.GONE);
        }
        holder.binding.date.setText(review.getUpdatedAt());

//        holder.binding.itemsList.setLayoutManager(new LinearLayoutManager(ctx));
//        holder.binding.itemsList.setHasFixedSize(true);
//        holder.binding.itemsList.setAdapter(new OrderItemsAdapter(review.getOrderproduct(),ctx));

        if(!review.getImage().equalsIgnoreCase("no image")&&!review.getImage().equalsIgnoreCase("")){
            byte[] decodedString = Base64.decode(review.getImage(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.binding.image.setImageBitmap(decodedByte);
            holder.binding.image.setVisibility(View.VISIBLE);

        }
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(review.getCreatedAt().split(" ")[0]);
            String dtt=new SimpleDateFormat("dd MMM, yyyy").format(date);
            holder.binding.date.setText(dtt+" "+review.getCreatedAt().split(" ")[1]);
        }catch (Exception e){

        }

        if(!review.getItem_rating().equalsIgnoreCase("")) {
            String[] items = review.getItem_rating().split("CAPINDIA");
            String[] name;
            name=new String[items.length];
            for (int i = 0; i < items.length; i++) {
                String[] rat = items[i].split("CAPSICO");
                if(rat.length>1) {
                    name[i] = rat[0] + "CAPSICO" + rat[1];
                }else{
                    name[i] = rat[0];
                }

            }
            holder.binding.itemRating.setLayoutManager(new LinearLayoutManager(ctx));
            holder.binding.itemRating.setHasFixedSize(true);
            holder.binding.itemRating.setAdapter(new ItemListRatingAdapter(name,ctx));
        }else{
            holder.binding.itemRating.setVisibility(View.GONE);
        }


        if(Double.parseDouble(review.getRating())==1){
            holder.binding.ratingone.setVisibility(View.VISIBLE);
            holder.binding.ratingtwo.setVisibility(View.GONE);
            holder.binding.ratingThree.setVisibility(View.GONE);
            holder.binding.ratingFour.setVisibility(View.GONE);
            holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())==2){
            holder.binding.ratingtwo.setVisibility(View.VISIBLE);
            holder.binding.ratingone.setVisibility(View.GONE);
            holder.binding.ratingThree.setVisibility(View.GONE);
            holder.binding.ratingFour.setVisibility(View.GONE);
            holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())==3){
            holder.binding.ratingThree.setVisibility(View.VISIBLE);
            holder.binding.ratingtwo.setVisibility(View.GONE);
            holder.binding.ratingone.setVisibility(View.GONE);
            holder.binding.ratingFour.setVisibility(View.GONE);
            holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())==4){
            holder.binding.ratingFour.setVisibility(View.VISIBLE);
            holder.binding.ratingtwo.setVisibility(View.GONE);
            holder.binding.ratingThree.setVisibility(View.GONE);
            holder.binding.ratingone.setVisibility(View.GONE);
            holder.binding.ratingFive.setVisibility(View.GONE);
        }else   if(Double.parseDouble(review.getRating())==5){
            holder.binding.ratingFive.setVisibility(View.VISIBLE);
            holder.binding.ratingtwo.setVisibility(View.GONE);
            holder.binding.ratingThree.setVisibility(View.GONE);
            holder.binding.ratingFour.setVisibility(View.GONE);
            holder.binding.ratingone.setVisibility(View.GONE);
        }

        if(review.getTaste().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.tImg);
            holder.binding.tText.setText("Bad");
        }else if(review.getTaste().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.tImg);
            holder.binding.tText.setText("OK");
        }else if(review.getTaste().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.tImg);
            holder.binding.tText.setText("Good");
        }else if(review.getTaste().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.tImg);
            holder.binding.tText.setText("Great");
        }

        if(review.getPacking().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.pImg);
            holder.binding.pText.setText("Bad");
        }else if(review.getPacking().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.pImg);
            holder.binding.pText.setText("OK");
        }else if(review.getPacking().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.pImg);
            holder.binding.pText.setText("Good");
        }else if(review.getPacking().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.pImg);
            holder.binding.pText.setText("Great");
        }

        if(review.getQuantity().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.qImg);
            holder.binding.qText.setText("Bad");
        }else if(review.getQuantity().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.qImg);
            holder.binding.qText.setText("OK");
        }else if(review.getQuantity().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.qImg);
            holder.binding.qText.setText("Good");
        }else if(review.getQuantity().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.qImg);
            holder.binding.qText.setText("Great");
        }

        if(review.getHygine().equalsIgnoreCase("1")){
            Picasso.get().load(R.drawable.bad).into(holder.binding.hImg);
            holder.binding.hText.setText("Bad");
        }else if(review.getHygine().equalsIgnoreCase("2")){
            Picasso.get().load(R.drawable.ok).into(holder.binding.hImg);
            holder.binding.hText.setText("OK");
        }else if(review.getHygine().equalsIgnoreCase("3")){
            Picasso.get().load(R.drawable.happy).into(holder.binding.hImg);
            holder.binding.hText.setText("Good");
        }else if(review.getHygine().equalsIgnoreCase("4")){
            Picasso.get().load(R.drawable.great).into(holder.binding.hImg);
            holder.binding.hText.setText("Great");
        }
//        Picasso.get().load(AppConstraints.BASE_URL+AppConstraints.USER+review.getUser_details().getIcon())
//                .placeholder(R.drawable.placeholder).into(holder.binding.icon);
//


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder
    {
        ReviewDesignBinding binding;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            binding=ReviewDesignBinding.bind(itemView);
        }
    }
}
