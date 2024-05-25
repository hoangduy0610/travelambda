package com.lambda.travel.ui.InforBook;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentInformationbookingScreenBinding;

public class InformationBookingFragment extends Fragment {
    private FragmentInformationbookingScreenBinding binding;
    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInformationbookingScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_informationbooking_screen, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bottomNav.setVisibility(View.VISIBLE);
        binding = null;
    }
}
