package com.lambda.travel.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentProfilescreenBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfilescreenBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfilescreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Set the layout to fragment_profilescreen
        root = inflater.inflate(R.layout.fragment_profilescreen, container, false);
        // Find the personal_information_item view
        View personalInformationItem = root.findViewById(R.id.personal_information_item);

        // Set an OnClickListener to handle the onPress event
        personalInformationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().build();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_personal_information, null, null, extras);
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