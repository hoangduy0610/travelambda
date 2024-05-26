package com.lambda.travel.ui.booking;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lambda.travel.model.Activities;
import com.lambda.travel.model.Food;
import com.lambda.travel.model.Hotel;
import com.lambda.travel.model.Tours;
import com.squareup.picasso.Picasso;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lambda.travel.databinding.FragmentBookingBinding;

import com.lambda.travel.R;


import java.util.ArrayList;

public class BookingFragment extends Fragment {
    private FragmentBookingBinding binding;
    private ViewFlipper viewFlipper;
    private ViewFlipper textFlipper;

    private ViewFlipper nameFlipper;
    private BottomNavigationView bottomNav;

    private int tintColor = Color.parseColor("#c9d4e4");
    private int whiteColor = Color.parseColor("#FFFFFF");

    private FirebaseFirestore db;
    private static final String TAG = "FirestoreExample";
    private ColorStateList onClickedTintList = ColorStateList.valueOf(tintColor);
    private  ColorStateList notClickedTintList = ColorStateList.valueOf(whiteColor);
    View foodsButton ;

    View hotelsButton;

    View activitiesButton;

    Hotel hotels;
    ArrayList<Food> foods;
    ArrayList<Activities> activities;

    private String[] foodDescriptions;

    private TextView detail;
    
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root= inflater.inflate(R.layout.fragment_booking, container, false);

        db = FirebaseFirestore.getInstance();

        // Read data from Firestore
        readDataFromFirestore();

        //detail = root.findViewById(R.id.detail);

        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        super.onCreate(savedInstanceState);

        foodsButton= root.findViewById(R.id.foods_button);

        hotelsButton = root.findViewById(R.id.hotels_button);

        activitiesButton = root.findViewById(R.id.activities_button);

        // default tab is hotels
        viewFlipper = root.findViewById(R.id.image_view_flipper);
        textFlipper = root.findViewById(R.id.text_view_flipper);
        nameFlipper = root.findViewById(R.id.name_flipper);
        DrawableCompat.setTintList(hotelsButton.getBackground(), onClickedTintList);
        DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
        DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);

        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
//        View decorView = requireActivity().getWindow().getDecorView();
// Hide both the navigation bar and the status bar.
// SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
// a general rule, you should design your app to hide the status bar whenever you
// hide the navigation bar.
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
//        decorView.setSystemUiVisibility(uiOptions);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void readDataFromFirestore() {
        DocumentReference docRef = db.collection("tours").document("2CO0pcIFAhAvbcsrDIpY");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Handle data found
                        Tours tours = document.toObject(Tours.class);

                        hotels = tours.hotel;
                        foods = tours.food;
                        activities = tours.activities;

                        foodsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawableCompat.setTintList(foodsButton.getBackground(), onClickedTintList);
                                DrawableCompat.setTintList(hotelsButton.getBackground(), notClickedTintList);
                                DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);
                                clearViewFlipper(nameFlipper);
                                clearViewFlipper(textFlipper);
                                clearViewFlipper(viewFlipper);
                                for (int i=0 ; i<foods.size(); i++) {
                                    ImageView imageView = new ImageView(getContext());
                                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                    //imageView.setImageResource(foodsImageList[i]);
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
                        });
//
                        hotelsButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
                                DrawableCompat.setTintList(hotelsButton.getBackground(), onClickedTintList);
                                DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);
                                clearViewFlipper(nameFlipper);
                                clearViewFlipper(textFlipper);
                                clearViewFlipper(viewFlipper);
                                for (int i=0 ; i<hotels.images.size(); i++) {
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
                                String text = hotels.name +"\n" + String.valueOf(hotels.star);
                                nameTextView.setText(text);
                                nameTextView.setGravity(Gravity.CENTER);
                                nameFlipper.addView(nameTextView);
                            }
                        });

                        activitiesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
                                DrawableCompat.setTintList(hotelsButton.getBackground(), notClickedTintList);
                                DrawableCompat.setTintList(activitiesButton.getBackground(), onClickedTintList);
                                clearViewFlipper(nameFlipper);
                                clearViewFlipper(textFlipper);
                                clearViewFlipper(viewFlipper);
                                for (int i=0 ; i<activities.size(); i++) {
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
                        });
                       Log.d(TAG, "hotel is " +  hotels );
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private void clearViewFlipper(ViewFlipper viewFlipper) {
        viewFlipper.removeAllViews();
        viewFlipper.invalidate();
    }

}


