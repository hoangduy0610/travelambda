package com.lambda.travel.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentHomeScreenBinding;
import com.lambda.travel.dto.Cache;
import com.lambda.travel.model.Review;
import com.lambda.travel.ui.reviews.SeenReviewFragment;
import com.lambda.travel.dto.TourInfo;
import com.lambda.travel.model.Location;
import com.lambda.travel.model.Tour;
import com.lambda.travel.ui.booking.BookingFragment;
import com.lambda.travel.ui.reviews.SendReviewsFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment  {

    private FragmentHomeScreenBinding binding;
    private Calendar timestamp = Calendar.getInstance();
    private boolean allowTogglePicker = true;
    private String idSearch;
    ImageView fab;
    private BottomNavigationView bottomNav;
    @SuppressLint("NewApi")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_home_screen, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        if (bottomNav != null) {
            bottomNav.setVisibility(View.VISIBLE);
        }
        EditText datePickerHolder = root.findViewById(R.id.datePickerHolder);
        DatePicker datePicker = root.findViewById(R.id.datePicker);
        timestamp.set(timestamp.get(Calendar.YEAR), timestamp.get(Calendar.MONTH), timestamp.get(Calendar.DAY_OF_MONTH), 0,0,0);
        datePickerHolder.setText(timestamp.get(Calendar.DAY_OF_MONTH) + "/" + (timestamp.get(Calendar.MONTH)+1) + "/" + timestamp.get(Calendar.YEAR));
        datePickerHolder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!allowTogglePicker) {
                    allowTogglePicker = true;
                    return;
                }
                allowTogglePicker = false;
                datePickerHolder.clearFocus();
                if (datePicker.getVisibility() == View.GONE) {
                    datePicker.setVisibility(View.VISIBLE);
                } else {
                    datePicker.setVisibility(View.GONE);
                }
            }
        });

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                datePickerHolder.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                datePicker.setVisibility(View.GONE);
                // Set timestamp from year, month and day
                timestamp.set(year, monthOfYear, dayOfMonth, 0,0,0);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Get the current user ID
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();
        View finalRoot = root;
        if (Cache.avatarCache == "") {
            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String imageUrl = document.getString("imageUrl");

                            if (imageUrl != null) {
                                // Load the image from imageUrl
                                Cache.avatarCache = imageUrl;
                                Cache.userName = document.getString("fullname");
                                ImageView imageView = finalRoot.findViewById(R.id.avatar_image);
                                Picasso.get().load(imageUrl).into(imageView);
                            }
                        } else {
                            Toast.makeText(getActivity(), "Something error happened", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Something error happened", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            ImageView imageView = finalRoot.findViewById(R.id.avatar_image);
            Picasso.get().load(Cache.avatarCache).into(imageView);
        }

        SwipeRefreshLayout mySwipeRefreshLayout = root.findViewById(R.id.swiperefresh);
        if (Cache.locationsCache.isEmpty()) {
            initFirestoreLocationCache(finalRoot, mySwipeRefreshLayout);
        } else {
            loadDataLocationToDropdown(finalRoot, Cache.locationsCache, Cache.locationLabelsCache, Cache.locationValuesCache);
        }

        mySwipeRefreshLayout.setOnRefreshListener(() -> {
            Log.i("Tag", "onRefresh called from SwipeRefreshLayout");
            initFirestoreLocationCache(finalRoot, mySwipeRefreshLayout);
        });

        View searchButton = root.findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.collection("tours")
                    .whereEqualTo("destination_id", idSearch)
                    .whereGreaterThanOrEqualTo("arrival_date", timestamp.getTime())
                    .whereLessThanOrEqualTo("departure_date", timestamp.getTime())
                    .orderBy("name")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Handle the query results here
                            List<Tour> tours = new ArrayList<>();
                            List<String> tour_ids = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Process each document
                                Tour tour_temp = document.toObject(Tour.class);
                                tours.add(tour_temp);
                                tour_ids.add(document.getId());
                            }

                            if (tours.isEmpty()) {
                                Toast.makeText(requireContext(), "No tours found", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // Create and show a popup modal with a ListView of all tours
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Tours");

                            // Create a list of tour labels
                            List<String> tourLabels = new ArrayList<>();
                            for (Tour tour : tours) {
                                String amountCurrency = String.format("%,.2f", Float.valueOf(Long.toString(tour.pricing)));
                                String label = tour.name + " - " + amountCurrency.substring(0, amountCurrency.length() - 3) + " VND";
                                tourLabels.add(label);
                            }

                            // Create an ArrayAdapter for the ListView
                            ArrayAdapter<String> tourAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, tourLabels);

                            // Set the ArrayAdapter to the ListView
                            ListView tourListView = new ListView(requireContext());
                            tourListView.setAdapter(tourAdapter);

                            // Set the ListView as the content view of the dialog
                            builder.setView(tourListView);

                            // Create and show the dialog
                            AlertDialog dialog = builder.create();

                            // Set the item click listener for the ListView
                            tourListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // Get the selected tour object
                                    Tour selectedTour = tours.get(position);
                                    TourInfo.tour = selectedTour;
                                    TourInfo.tour_id = tour_ids.get(position);

                                    db.collection("reviews")
                                        .whereEqualTo("tour_id", tour_ids.get(position))
                                        .get()
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                int totalReviews = 0;
                                                int totalPoints = 0;
                                                TourInfo.reviews = new ArrayList<>();
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Review re_temp = document.toObject(Review.class);
                                                    TourInfo.reviews.add(re_temp);
                                                    int reviewPoint = re_temp.review;
                                                    totalPoints += reviewPoint;
                                                    totalReviews++;
                                                }

                                                double averagePoint = totalPoints / (double) totalReviews;
                                                // Use the averagePoint for further processing or display
                                                TourInfo.reviewPoint = averagePoint;

                                                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().build();
                                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                                                navController.navigate(R.id.navigation_booking_screen, null, null, extras);
                                                // Dismiss the dialog
                                                dialog.dismiss();
                                            } else {
                                                Log.d("D", "Error getting reviews: ", task.getException());
                                            }
                                    });
                                }
                            });
                            dialog.show();
                        } else {
                            Log.d("D", "Error getting documents: ", task.getException());
                        }
                    });
            }
        });
        
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initFirestoreLocationCache(View finalRoot, SwipeRefreshLayout srl) {
        srl.setRefreshing(true);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations")
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    List<Location> locations = new ArrayList<>();
                    List<String> locationLabels = new ArrayList<>();
                    List<String> locationValues = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Location location = document.toObject(Location.class);
                        locations.add(location);
                        locationLabels.add(location.city);
                        locationValues.add(document.getId());
                    }

                    Cache.locationsCache = locations;
                    Cache.locationLabelsCache = locationLabels;
                    Cache.locationValuesCache = locationValues;

                    loadDataLocationToDropdown(finalRoot, locations, locationLabels, locationValues);
                    srl.setRefreshing(false);
                } else {
                    Log.d("D", "Error getting documents: ", task.getException());
                    srl.setRefreshing(false);
                }
            });
    }

    private void loadDataLocationToDropdown(View finalRoot, List<Location> locations, List<String> locationLabels, List<String> locationValues) {
        Spinner locationSpinner = finalRoot.findViewById(R.id.location);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, locationLabels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
        locationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String id = locationValues.get(arg2);
                idSearch = id;
                TourInfo.location = locations.get(arg2);
                Log.d("T", id);
                ImageView imageView = finalRoot.findViewById(R.id.imageView);
                String imageUrl = locations.get(arg2).image;
                Picasso.get().load(imageUrl).into(imageView);

                TextView textView = finalRoot.findViewById(R.id.location_desc);
                textView.setText(locations.get(arg2).description);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
}