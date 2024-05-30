package com.lambda.travel.ui.InforBook;

import static android.app.PendingIntent.getActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.lambda.travel.dto.TourBookInfo;
import com.lambda.travel.dto.TourInfo;
import com.lambda.travel.model.Location;
import com.lambda.travel.model.Tour;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.lambda.travel.model.TourSchedule;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class InformationBookingFragment extends Fragment {
    private FragmentInformationbookingScreenBinding binding;
    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInformationbookingScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_informationbooking_screen, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        Button bookNowBtn = root.findViewById(R.id.booknow_btn);
        View finalRoot = root;
        Tour tour = TourInfo.tour;
        Location location = TourInfo.location;

        if (TourBookInfo.booking != null) {
            tour = TourBookInfo.booking.tourInfo;
            location = TourBookInfo.booking.location;

            bookNowBtn.setVisibility(View.GONE);
            EditText textEditName = finalRoot.findViewById(R.id.editTextName);
            EditText textEditEmailAddress = finalRoot.findViewById(R.id.editTextEmailAddress);
            EditText textEditPhone = finalRoot.findViewById(R.id.editTextPhone);
            EditText textEditMature = finalRoot.findViewById(R.id.editTextMature);
            EditText textEditChildren = finalRoot.findViewById(R.id.editTextChildren);

            textEditName.setText(TourBookInfo.booking.bookingInfo.name);
            textEditEmailAddress.setText(TourBookInfo.booking.bookingInfo.emailAddress);
            textEditPhone.setText(TourBookInfo.booking.bookingInfo.phone);
            textEditMature.setText(Long.toString(TourBookInfo.booking.bookingInfo.amount.mature) + " Adults");
            textEditChildren.setText(Long.toString(TourBookInfo.booking.bookingInfo.amount.children) + " Children");

            textEditName.setEnabled(false);
            textEditEmailAddress.setEnabled(false);
            textEditPhone.setEnabled(false);
            textEditMature.setEnabled(false);
            textEditChildren.setEnabled(false);

            View tourSchedule = root.findViewById(R.id.viewTourSchedule);
            tourSchedule.setVisibility(View.VISIBLE);

            // Set an OnClickListener to handle the onPress event
            tourSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                    builder.setTitle("Tour Schedule");

                    StringBuilder scheduleDetails = new StringBuilder();
                    for (TourSchedule schedule : TourBookInfo.booking.tourInfo.tour_schedule) {
                        scheduleDetails.append("Date: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(schedule.date)).append("\n");
                        scheduleDetails.append("Details: ").append(schedule.detail).append("\n\n");
                    }

                    builder.setMessage(scheduleDetails.toString());

                    // Add a button to dismiss the dialog
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    // Create and show the dialog
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }

        TextView tourName = root.findViewById(R.id.readReviewNameTour);
        TextView tourReview = root.findViewById(R.id.starContentReviewElem);
        TextView tourLocation = root.findViewById(R.id.readReviewLocation);

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

        tourName.setText(tour.name);
        tourLocation.setText(location.city);
        if (!Double.isNaN(TourInfo.reviewPoint)) {
            tourReview.setText(Double.toString(TourInfo.reviewPoint));
        }
     
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

                if (isInputValidated(name, emailAddress, phone, mature, children)) {
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
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TourBookInfo.booking = null;
//        bottomNav.setVisibility(View.VISIBLE);
        binding = null;
    }

    private boolean isInputValidated(String name, String emailAddress, String phone, int mature, int children) {
        // Validate inputs
        if (name.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (emailAddress.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Email regex pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,8}";

        // Validate email address
        if (!Pattern.matches(emailPattern, emailAddress)) {
            Toast.makeText(getActivity(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.isEmpty()) {
            Toast.makeText(getActivity(), "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Phone number regex pattern
        String phonePattern = "\\d{10}";

        // Validate phone number
        if (!Pattern.matches(phonePattern, phone)) {
            Toast.makeText(getActivity(), "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (mature <= 0) {
            Toast.makeText(getActivity(), "Please enter a valid number of mature participants", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (children < 0) {
            Toast.makeText(getActivity(), "Please enter a valid number of children participants", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
