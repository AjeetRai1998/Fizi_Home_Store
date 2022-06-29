package digi.coders.capsicostorepartner.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import digi.coders.capsicostorepartner.R;
import digi.coders.capsicostorepartner.adapter.BookingListAdapter;
import digi.coders.capsicostorepartner.databinding.FragmentBookingsBinding;


public class BookingsFragment extends Fragment {

    FragmentBookingsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding=FragmentBookingsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);



        //loadBookingList

 //       loadBookingList();


    }

    private void loadBookingList() {
        binding.bookingList.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.bookingList.setAdapter(new BookingListAdapter());
    }


}
