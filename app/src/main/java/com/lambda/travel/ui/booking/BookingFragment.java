package com.lambda.travel.ui.booking;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.FragmentNavigator;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.lambda.travel.databinding.FragmentBookingBinding;
import com.lambda.travel.R;
import com.lambda.travel.model.Review;
import com.lambda.travel.model.TourSchedule;
import com.lambda.travel.ui.InforBook.InformationBookingFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lambda.travel.dto.TourInfo;
import com.lambda.travel.model.Activity;
import com.lambda.travel.model.Food;
import com.lambda.travel.model.Hotel;
import com.lambda.travel.model.Tour;
import com.lambda.travel.ui.reviews.SeenReviewFragment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BookingFragment extends Fragment {
    private FragmentBookingBinding binding;
    private ViewFlipper viewFlipper;
    private ViewFlipper textFlipper;
    private ViewFlipper nameFlipper;
    private final int tintColor = Color.parseColor("#c9d4e4");
    private final int whiteColor = Color.parseColor("#FFFFFF");
    private FirebaseFirestore db;
    private static final String TAG = "FirestoreExample";
    private final ColorStateList onClickedTintList = ColorStateList.valueOf(tintColor);
    private final ColorStateList notClickedTintList = ColorStateList.valueOf(whiteColor);
    private View foodsButton;
    private View hotelsButton;
    private View activitiesButton;
    private Hotel hotels;
    private ArrayList<Food> foods;
    private ArrayList<Activity> activities;
    private String[] foodDescriptions;
    private TextView detail;

    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_booking, container, false);
        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        // onPress backScreenBtn
        View backScreenBtn = root.findViewById(R.id.bookingBackBtn);
        backScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                // navigation pop to previous
                bottomNav.setVisibility(View.VISIBLE);
                navController.popBackStack();
            }
        });

        if (!Double.isNaN(TourInfo.reviewPoint)) {
            ((TextView) root.findViewById(R.id.booking_rating)).setText(Double.toString(TourInfo.reviewPoint));
        }
        db = FirebaseFirestore.getInstance();

        //detail = root.findViewById(R.id.detail);

        super.onCreate(savedInstanceState);

        foodsButton = root.findViewById(R.id.foods_button);
        hotelsButton = root.findViewById(R.id.hotels_button);
        activitiesButton = root.findViewById(R.id.activities_button);

        // default tab is hotels
        viewFlipper = root.findViewById(R.id.image_view_flipper);
        textFlipper = root.findViewById(R.id.text_view_flipper);
        nameFlipper = root.findViewById(R.id.name_flipper);
        DrawableCompat.setTintList(hotelsButton.getBackground(), onClickedTintList);
        DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
        DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);


        // Read data from Firestore
        readDataFromFirestore(root);
        
        View contButton = root.findViewById(R.id.btn_continue);

        // Set an OnClickListener to handle the onPress event
        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment informationBookingFragment = new InformationBookingFragment();
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,informationBookingFragment);
//                fragmentTransaction.commit();
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().build();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_infor_booking_screen, null, null, extras);
            }
        });

        View tourSchedule = root.findViewById(R.id.tour_schedule_detail);

        // Set an OnClickListener to handle the onPress event
        tourSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Tour Schedule");

                StringBuilder scheduleDetails = new StringBuilder();
                for (TourSchedule schedule : TourInfo.tour.tour_schedule) {
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

        root.findViewById(R.id.booking_rating).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment seenReviewFragment = new SeenReviewFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.add(R.id.nav_host_fragment_activity_main, seenReviewFragment);
//                fragmentTransaction.commit();
                FragmentNavigator.Extras extras = new FragmentNavigator.Extras.Builder().build();
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                navController.navigate(R.id.navigation_reviews_screen, null, null, extras);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void readDataFromFirestore(View root) {
        Tour tours = TourInfo.tour;

        Picasso.get().load(tours.banner).into((ImageView) root.findViewById(R.id.bannerImg));
        ((TextView) root.findViewById(R.id.locationName)).setText(TourInfo.location.city);

        hotels = tours.hotel;
        foods = tours.food;
        activities = tours.activities;

        // Display hotels
        displayHotels();

        foodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display foods
                displayFoods();
            }
        });

        hotelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display hotels
                displayHotels();
            }
        });

        activitiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display activities
                displayActivities();
            }
        });
    }

    private void clearViewFlipper(ViewFlipper viewFlipper) {
        viewFlipper.removeAllViews();
        viewFlipper.invalidate();
    }

    private void displayHotels() {
        DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
        DrawableCompat.setTintList(hotelsButton.getBackground(), onClickedTintList);
        DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);
        clearViewFlipper(nameFlipper);
        clearViewFlipper(textFlipper);
        clearViewFlipper(viewFlipper);

        for (int i = 0; i < hotels.images.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String imageUrl = hotels.images.get(i);

            // Use Picasso to load the image from the URL into the ImageView
            Picasso.get().load(imageUrl).into(imageView);
            viewFlipper.addView(imageView);
        }

        TextView textView = new TextView(getContext());
        textView.setText(hotels.detail);
        textView.setGravity(Gravity.CENTER);
        textFlipper.addView(textView);

        TextView nameTextView = new TextView(getContext());
        String text = hotels.name + "\n" + String.valueOf(hotels.star);
        nameTextView.setText(text);
        nameTextView.setGravity(Gravity.CENTER);
        nameFlipper.addView(nameTextView);
    }

    private void displayFoods() {
        DrawableCompat.setTintList(foodsButton.getBackground(), onClickedTintList);
        DrawableCompat.setTintList(hotelsButton.getBackground(), notClickedTintList);
        DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);
        clearViewFlipper(nameFlipper);
        clearViewFlipper(textFlipper);
        clearViewFlipper(viewFlipper);

        for (int i = 0; i < foods.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String imageUrl = foods.get(i).image;

            // Use Picasso to load the image from the URL into the ImageView
            Picasso.get().load(imageUrl).into(imageView);
            viewFlipper.addView(imageView);

            TextView textView = new TextView(getContext());
            textView.setText(foods.get(i).detail);
            textView.setGravity(Gravity.CENTER);
            textFlipper.addView(textView);

            TextView nameTextView = new TextView(getContext());
            nameTextView.setText(foods.get(i).name);
            nameTextView.setGravity(Gravity.CENTER);
            nameFlipper.addView(nameTextView);
        }
    }

    private void displayActivities() {
        DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
        DrawableCompat.setTintList(hotelsButton.getBackground(), notClickedTintList);
        DrawableCompat.setTintList(activitiesButton.getBackground(), onClickedTintList);
        clearViewFlipper(nameFlipper);
        clearViewFlipper(textFlipper);
        clearViewFlipper(viewFlipper);

        for (int i = 0; i < activities.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            String imageUrl = activities.get(i).image;

            // Use Picasso to load the image from the URL into the ImageView
            Picasso.get().load(imageUrl).into(imageView);
            viewFlipper.addView(imageView);

            TextView textView = new TextView(getContext());
            textView.setText(activities.get(i).detail);
            textView.setGravity(Gravity.CENTER);
            textFlipper.addView(textView);

            TextView nameTextView = new TextView(getContext());
            nameTextView.setText(activities.get(i).name);
            nameTextView.setGravity(Gravity.CENTER);
            nameFlipper.addView(nameTextView);
        }
    }
}
