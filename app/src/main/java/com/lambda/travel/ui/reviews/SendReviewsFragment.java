package com.lambda.travel.ui.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentHomeScreenBinding;
import com.lambda.travel.databinding.FragmentSendreviewsScreenBinding;
import com.lambda.travel.dto.HistoryDto;
import com.lambda.travel.dto.SelectTourForReview;
import com.lambda.travel.dto.TourInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SendReviewsFragment extends Fragment {

    private FragmentSendreviewsScreenBinding binding;
    private BottomNavigationView bottomNav;
    private int rate = 5;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSendreviewsScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_sendreviews_screen, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

//        ((TextView) root.findViewById(R.id.textView7)).setText(Double.toString(SelectTourForReview.reviewPoint));
        ((TextView) root.findViewById(R.id.textView6)).setText(SelectTourForReview.tour.name);
        ((TextView) root.findViewById(R.id.textView8)).setText(SelectTourForReview.location.city);

        RatingBar ratingBar = root.findViewById(R.id.ratingBar3);
        ratingBar.setRating(5);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rate = Math.round(rating);
            }
        });

        View finalRoot = root;
        root.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                String userId = currentUser.getUid();
                String remark = ((EditText) finalRoot.findViewById(R.id.editTextTextMultiLine)).getText().toString();

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Map<String, Object> review = new HashMap<>();
                review.put("user_id", userId);
                review.put("tour_id", SelectTourForReview.tourId);
                review.put("location_id", SelectTourForReview.tour.destination_id);
                review.put("remark", remark);
                review.put("review", rate);

                db.collection("reviews")
                    .add(review)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            // Document added successfully
                            HistoryDto.histories = new ArrayList<>();
                            Toast.makeText(getActivity(), "Review added successfully", Toast.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                            navController.popBackStack();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error adding document
                            Toast.makeText(getActivity(), "Review failed", Toast.LENGTH_SHORT).show();
                        }
                    });
            }
        });

        root.findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
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