package com.lambda.travel.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentHomeScreenBinding;
import com.lambda.travel.ui.hotel.RoomDetailsFragment;

public class HomeFragment extends Fragment {

    private FragmentHomeScreenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_home_screen, container, false);

        View fabButton = root.findViewById(R.id.fab);

        // Set an OnClickListener to handle the onPress event
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment roomDetailsFragment = new RoomDetailsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,roomDetailsFragment);
                fragmentTransaction.commit();
//                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().build();
//                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
//                navController.navigate(R.id.navigation_roomdetails, null, null, extras);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}