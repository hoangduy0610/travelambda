package com.lambda.travel.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentHistoryBinding;
import com.lambda.travel.dto.HistoryDto;
import com.lambda.travel.dto.SelectTourForReview;
import com.lambda.travel.model.BookingHistory;
import com.lambda.travel.model.Location;
import com.lambda.travel.model.Review;
import com.lambda.travel.model.Tour;
import com.lambda.travel.model.TourBooking;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_history, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.VISIBLE);
        ConstraintLayout historiesContainer = root.findViewById(R.id.historiesContainer);
        SwipeRefreshLayout mySwipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(() -> {
                Log.i("Tag", "onRefresh called from SwipeRefreshLayout");
                initFirestoreData(historiesContainer, mySwipeRefreshLayout);
            }
        );

        if (HistoryDto.histories.isEmpty()) {
            mySwipeRefreshLayout.setRefreshing(true);
            initFirestoreData(historiesContainer, mySwipeRefreshLayout);
        } else {
            for (BookingHistory history : HistoryDto.histories) {
                processHistories(historiesContainer, history);
            }
        }
        return root;
    }

    private void initFirestoreData(ConstraintLayout historiesContainer, SwipeRefreshLayout srl) {
        HistoryDto.histories = new ArrayList<>();
        historiesContainer.removeAllViews();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference bookingsRef = db.collection("tour_bookings");

        bookingsRef.whereEqualTo("user_id", currentUserId)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        TourBooking tb = document.toObject(TourBooking.class);
                        String tourId = tb.tour_id;
                        DocumentReference tourRef = db.collection("tours").document(tourId);
                        tourRef.get().addOnCompleteListener(tourTask -> {
                            if (tourTask.isSuccessful()) {
                                DocumentSnapshot tourDoc = tourTask.getResult();
                                if (tourDoc.exists()) {
                                    Tour tour = tourDoc.toObject(Tour.class);
                                    String destinationId = tour.destination_id;
                                    DocumentReference destinationRef = db.collection("locations").document(destinationId);
                                    destinationRef.get().addOnCompleteListener(destinationTask -> {
                                        if (destinationTask.isSuccessful()) {
                                            DocumentSnapshot destinationDoc = destinationTask.getResult();
                                            if (destinationDoc.exists()) {
                                                Location location = destinationDoc.toObject(Location.class);
                                                db.collection("reviews")
                                                    .whereEqualTo("tour_id", tourDoc.getId())
                                                    .whereEqualTo("user_id", currentUserId)
                                                    .get()
                                                    .addOnCompleteListener(reviewTask -> {
                                                        if (reviewTask.isSuccessful()) {
                                                            QuerySnapshot reviewDoc = reviewTask.getResult();
                                                            Review review;
                                                            if (!reviewDoc.isEmpty()) {
                                                                review = reviewDoc.getDocuments().get(0).toObject(Review.class);
                                                            } else {
                                                                review = new Review("", -1, "", "");
                                                            }
                                                            BookingHistory temp_bkh = new BookingHistory(tourDoc.getId(), tour, tb, location, review);
                                                            HistoryDto.histories.add(temp_bkh);
                                                            processHistories(historiesContainer, temp_bkh);
                                                        } else {
                                                            // Handle error
                                                            Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            }
                                        } else {
                                            // Handle error
                                            Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                // Handle error
                                Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    srl.setRefreshing(false);
                } else {
                    // Handle error
                    Toast.makeText(getContext(), "Error occurred", Toast.LENGTH_SHORT).show();
                    srl.setRefreshing(false);
                }
            });
    }

    private void processHistories(ConstraintLayout historiesContainer, BookingHistory history) {
        View cardView = LayoutInflater.from(getContext()).inflate(R.layout.history_card_layout, historiesContainer, false);

        TextView tourNameTextView = cardView.findViewById(R.id.textView);
        TextView locationTextView = cardView.findViewById(R.id.textView1);

        tourNameTextView.setText(history.tourInfo.name);
        locationTextView.setText(history.location.city);

        View separatorReview = cardView.findViewById(R.id.seperator_review);
        TextView historyReviewBtn = cardView.findViewById(R.id.history_review_btn);
        TextView historyTourStatusText = cardView.findViewById(R.id.history_tour_status_text);
        if (history.tourInfo.arrival_date.compareTo(new Date()) > 0) {
            if (history.tourInfo.departure_date.compareTo(new Date()) <= 0) {
                historyTourStatusText.setText("Incoming");
                historyTourStatusText.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow_bold));
            } else {
                historyTourStatusText.setText("Upcoming");
                historyTourStatusText.setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_green));
            }
            ((ViewGroup) separatorReview.getParent()).removeView(separatorReview);
            ((ViewGroup) historyReviewBtn.getParent()).removeView(historyReviewBtn);
        } else if (history.review.review >= 0) {
            ((ViewGroup) separatorReview.getParent()).removeView(separatorReview);
            ((ViewGroup) historyReviewBtn.getParent()).removeView(historyReviewBtn);
        } else {
            historyReviewBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelectTourForReview.tourId = history.tourId;
                    SelectTourForReview.tour = history.tourInfo;
                    SelectTourForReview.location = history.location;
                    FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().build();
                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.navigation_submit_review, null, null, extras);
                }
            });
        }

        historiesContainer.addView(cardView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}