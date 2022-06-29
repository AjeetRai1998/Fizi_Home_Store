package digi.coders.capsicostorepartner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.util.List;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.helper.Constraints;


public class MyViewPagerAdapter extends PagerAdapter {

    Context ctx;
    List<String> str;

    public MyViewPagerAdapter(List<String> str, Context ctx) {
        this.str = str;

        this.ctx = ctx;
    }

    private LayoutInflater layoutInflater;
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.view_pager_layout, container, false);
        ImageView img=view.findViewById(R.id.image);

            Picasso.get()
                    .load(Constraints.BASE_URL+str.get(position)).placeholder(R.drawable.placeholder1)
                    .into(img);
            container.addView(view);

        return view;
    }


    @Override
    public int getCount() {

            return str.size();


    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
