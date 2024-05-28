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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentHistoryBinding;
import com.lambda.travel.dto.HistoryDto;
import com.lambda.travel.model.BookingHistory;
import com.lambda.travel.model.Location;
import com.lambda.travel.model.Tour;
import com.lambda.travel.model.TourBooking;

import java.util.ArrayList;
import java.util.Date;

public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_history, container, false);
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
                                                BookingHistory temp_bkh = new BookingHistory(tour, tb, location);
                                                HistoryDto.histories.add(temp_bkh);
                                                processHistories(historiesContainer, temp_bkh);
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

        if (history.tourInfo.arrival_date.compareTo(new Date()) > 0) {
            View separatorReview = cardView.findViewById(R.id.seperator_review);
            TextView historyReviewBtn = cardView.findViewById(R.id.history_review_btn);
            TextView historyTourStatusText = cardView.findViewById(R.id.history_tour_status_text);
            if (history.tourInfo.departure_date.compareTo(new Date()) <= 0) {
                historyTourStatusText.setText("Incoming");
                historyTourStatusText.setTextColor(ContextCompat.getColor(getContext(), R.color.yellow_bold));
            } else {
                historyTourStatusText.setText("Upcoming");
                historyTourStatusText.setTextColor(ContextCompat.getColor(getContext(), R.color.color_text_green));
            }
            ((ViewGroup) separatorReview.getParent()).removeView(separatorReview);
            ((ViewGroup) historyReviewBtn.getParent()).removeView(historyReviewBtn);
        }

        historiesContainer.addView(cardView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}