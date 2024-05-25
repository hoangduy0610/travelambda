package com.lambda.travel.ui.booking;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lambda.travel.HomeTabActivity;
import com.lambda.travel.MainActivity;
import com.lambda.travel.databinding.FragmentBookingBinding;

import com.lambda.travel.R;
import com.lambda.travel.ui.InforBook.InformationBookingFragment;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class BookingFragment extends Fragment {

    private FragmentBookingBinding binding;
    private ViewFlipper viewFlipper;
    private BottomNavigationView bottomNav;

    private int tintColor = Color.parseColor("#c9d4e4");
    private int whiteColor = Color.parseColor("#FFFFFF");

    private ColorStateList onClickedTintList = ColorStateList.valueOf(tintColor);
    private  ColorStateList notClickedTintList = ColorStateList.valueOf(whiteColor);
    int[] hotelImageList = {R.drawable.hotel1, R.drawable.hotel2, R.drawable.hotel3, R.drawable.hotel4, R.drawable.hotel5};
    int[] foodsImageList = {R.drawable.food1, R.drawable.food2, R.drawable.food3, R.drawable.food1, R.drawable.food5};
    int[] activitiesImageList = {R.drawable.activity1, R.drawable.activity2, R.drawable.activity3, R.drawable.activity4, R.drawable.activity5};
    int[] imageList = {};


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBookingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        root= inflater.inflate(R.layout.fragment_booking, container, false);

        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        super.onCreate(savedInstanceState);

        View hotelsButton = root.findViewById(R.id.hotels_button);
        View foodsButton = root.findViewById(R.id.foods_button);
        View activitiesButton = root.findViewById(R.id.activities_button);

        // default tab is hotels
        viewFlipper = root.findViewById(R.id.view_flipper);
        DrawableCompat.setTintList(hotelsButton.getBackground(), onClickedTintList);
        DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
        DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);
        for (int image : hotelImageList) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(image);
            viewFlipper.addView(imageView);
        }


        hotelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawableCompat.setTintList(hotelsButton.getBackground(), onClickedTintList);
                DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);
                DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);
                viewFlipper.removeAllViews();;
                viewFlipper.invalidate();
                for (int image : hotelImageList) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageResource(image);
                    viewFlipper.addView(imageView);
                }
            }
        });

        foodsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawableCompat.setTintList(foodsButton.getBackground(), onClickedTintList);
                DrawableCompat.setTintList(hotelsButton.getBackground(), notClickedTintList);
                DrawableCompat.setTintList(activitiesButton.getBackground(), notClickedTintList);
                viewFlipper.removeAllViews();;
                viewFlipper.invalidate();
                for (int image : foodsImageList) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageResource(image);
                    viewFlipper.addView(imageView);
                }
            }
        });

        activitiesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DrawableCompat.setTintList(activitiesButton.getBackground(), onClickedTintList);
                DrawableCompat.setTintList(hotelsButton.getBackground(), notClickedTintList);
                DrawableCompat.setTintList(foodsButton.getBackground(), notClickedTintList);;
                viewFlipper.removeAllViews();;
                viewFlipper.invalidate();
                for (int image :activitiesImageList) {
                    ImageView imageView = new ImageView(getContext());
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setImageResource(image);
                    viewFlipper.addView(imageView);
                }
            }
        });

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
}


