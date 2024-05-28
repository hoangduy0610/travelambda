package com.lambda.travel.ui.InforBook;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentInformationbookingScreenBinding;
import com.lambda.travel.dto.TourInfo;
import com.lambda.travel.model.Location;
import com.lambda.travel.model.Tour;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import java.util.HashMap;
import java.util.Map;

public class InformationBookingFragment extends Fragment {
    private FragmentInformationbookingScreenBinding binding;
    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInformationbookingScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_informationbooking_screen, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        // onPress backScreenBtn
        View backScreenBtn = root.findViewById(R.id.imageView2);
        backScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                // navigation pop to previous
                navController.popBackStack();
            }
        });

        Tour tour = TourInfo.tour;
        Location location = TourInfo.location;

        TextView tourName = root.findViewById(R.id.readReviewNameTour);
        TextView tourReview = root.findViewById(R.id.starContentReviewElem);
        TextView tourLocation = root.findViewById(R.id.readReviewLocation);

        tourName.setText(tour.name);
        tourLocation.setText(location.city);
        tourReview.setText(Double.toString(TourInfo.reviewPoint));

        Button bookNowBtn = root.findViewById(R.id.booknow_btn);
        View finalRoot = root;
     
        bookNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText textEditName = finalRoot.findViewById(R.id.editTextName);
                EditText textEditEmailAddress = finalRoot.findViewById(R.id.editTextEmailAddress);
                EditText textEditPhone = finalRoot.findViewById(R.id.editTextPhone);
                EditText textEditMature = finalRoot.findViewById(R.id.editTextMature);
                EditText textEditChildren = finalRoot.findViewById(R.id.editTextChildren);
                String name = textEditName.getText().toString();
                String emailAddress = textEditEmailAddress.getText().toString();
                String phone = textEditPhone.getText().toString();
                int mature = Integer.parseInt(textEditMature.getText().toString());
                int children = Integer.parseInt(textEditChildren.getText().toString());

                // Create a new document with the specified fields
                Map<String, Object> booking = new HashMap<>();
                booking.put("name", name);
                booking.put("emailAddress", emailAddress);
                booking.put("phone", phone);
                booking.put("amount", new HashMap<String, Integer>() {{
                    put("mature", mature);
                    put("children", children);
                }});
                booking.put("tour_id", TourInfo.tour_id);
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String userId = currentUser.getUid();
                booking.put("user_id", userId);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("tour_bookings")
                    .add(booking)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Show a toast message to indicate successful booking
                            Toast.makeText(getActivity(), "Booking successful!", Toast.LENGTH_SHORT).show();

                            // Pop the current fragment until the home fragment is reached
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                            navController.popBackStack(R.id.navigation_home, false);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Booking failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        bottomNav.setVisibility(View.VISIBLE);
        binding = null;
    }
}
