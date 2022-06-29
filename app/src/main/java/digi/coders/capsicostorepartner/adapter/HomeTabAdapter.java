package digi.coders.capsicostorepartner.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import digi.coders.capsicostorepartner.fragment.CompleteOrderFragment;
import digi.coders.capsicostorepartner.fragment.NewOrderFragment;
import digi.coders.capsicostorepartner.fragment.PreparingOrderFragment;


public class HomeTabAdapter extends FragmentPagerAdapter {


    private Context myContext;
    int totalTabs;

    public HomeTabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewOrderFragment newOrderFragment = new NewOrderFragment();
                return newOrderFragment;
            case 1:
                PreparingOrderFragment processingOrderFragment = new PreparingOrderFragment();
                return processingOrderFragment;
            case 2:
                CompleteOrderFragment deliveredOrderFragment = new CompleteOrderFragment();
                return deliveredOrderFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}

