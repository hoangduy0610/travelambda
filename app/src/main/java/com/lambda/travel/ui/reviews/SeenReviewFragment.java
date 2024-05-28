package com.lambda.travel.ui.reviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentSeenreviewsScreenBinding;
import com.lambda.travel.dto.TourInfo;
import com.lambda.travel.model.Review;

public class SeenReviewFragment extends Fragment {

    private FragmentSeenreviewsScreenBinding binding;
    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSeenreviewsScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_seenreviews_screen, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        ((TextView) root.findViewById(R.id.starReview)).setText(Double.toString(TourInfo.reviewPoint));
        ((TextView) root.findViewById(R.id.readReviewNameTour)).setText(TourInfo.tour.name);
        ((TextView) root.findViewById(R.id.readReviewLocation)).setText(TourInfo.location.city);

        LinearLayout reviewContainer = root.findViewById(R.id.listReviewContainer);
        for (Review review : TourInfo.reviews) {
            View reviewView = inflater.inflate(R.layout.review_element_layout, reviewContainer, false);
            TextView authorReviewName = reviewView.findViewById(R.id.authorReviewName);
            TextView reviewRemark = reviewView.findViewById(R.id.reviewRemark);

            reviewRemark.setText(review.remark);
            ((TextView) reviewView.findViewById(R.id.starContentReviewElem)).setText(Integer.toString(review.review));
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference userRef = db.collection("users").document(review.user_id);

            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot document = task.getResult();
                    if (task.isSuccessful()) {
                        if (document.exists()) {
                            authorReviewName.setText(document.getString("fullname"));
                        }
                    }
                    reviewContainer.addView(reviewView);
                }
            });
        }

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
        binding = null;
    }
}