package com.lambda.travel.ui.booking;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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
import com.lambda.travel.databinding.FragmentBookingBinding;
import com.lambda.travel.R;
import com.lambda.travel.ui.InforBook.InformationBookingFragment;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lambda.travel.dto.TourInfo;
import com.lambda.travel.model.Activity;
import com.lambda.travel.model.Food;
import com.lambda.travel.model.Hotel;
import com.lambda.travel.model.Tour;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class BookingFragment extends Fragment {
    private FragmentBookingBinding binding;
    private ViewFlipper viewFlipper;
    private ViewFlipper textFlipper;
    private ViewFlipper nameFlipper;
    private int tintColor = Color.parseColor("#c9d4e4");
    private int whiteColor = Color.parseColor("#FFFFFF");
    private FirebaseFirestore db;
    private static final String TAG = "FirestoreExample";
    private ColorStateList onClickedTintList = ColorStateList.valueOf(tintColor);
    private ColorStateList notClickedTintList = ColorStateList.valueOf(whiteColor);
    private View foodsButton;
    private View hotelsButton;
    private View activitiesButton;
    private Hotel hotels;
    private ArrayList<Food> foods;
    private ArrayList<Activity> activities;
    private String[] foodDescriptions;
    private TextView detail;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root = inflater.inflate(R.layout.fragment_booking, container, false);

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
        readDataFromFirestore();
        
        View contButton = root.findViewById(R.id.btn_continue);

        // Set an OnClickListener to handle the onPress event
        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment informationBookingFragment = new InformationBookingFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main,informationBookingFragment);
                fragmentTransaction.commit();
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

    private void readDataFromFirestore() {
        Tour tours = TourInfo.tour;

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
