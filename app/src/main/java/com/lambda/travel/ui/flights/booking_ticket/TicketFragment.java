package com.lambda.travel.ui.flights.booking_ticket;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentTicketScreenBinding;

public class TicketFragment extends Fragment {

    private FragmentTicketScreenBinding binding;
    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTicketScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Set the layout to fragment_profilescreen
        root = inflater.inflate(R.layout.fragment_ticket_screen, container, false);

        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        // onPress backScreenBtn
        View backScreenBtn = root.findViewById(R.id.ticketBackHeadingBtn);
        backScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                // navigation pop to previous
                navController.popBackStack();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bottomNav.setVisibility(View.VISIBLE);
        binding = null;
    }
}