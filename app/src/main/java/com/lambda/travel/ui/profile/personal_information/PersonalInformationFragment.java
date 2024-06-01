package com.lambda.travel.ui.profile.personal_information;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lambda.travel.R;
import com.lambda.travel.databinding.FragmentPersonalinformationscreenBinding;
import com.lambda.travel.dto.Cache;

import java.util.UUID;

public class PersonalInformationFragment extends Fragment {

    private FragmentPersonalinformationscreenBinding binding;
    private BottomNavigationView bottomNav;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPersonalinformationscreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        // Set the layout to fragment_profilescreen
        root = inflater.inflate(R.layout.fragment_personalinformationscreen, container, false);

        bottomNav = getActivity().findViewById(R.id.nav_view);
        bottomNav.setVisibility(View.GONE);

        View finalRoot = root;

        // Get the current user ID
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Get the document with id=userId in the "users" collection in Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        // onPress backScreenBtn
        View backScreenBtn = root.findViewById(R.id.backScreenBtn);
        backScreenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_activity_main);
                // navigation pop to previous
                navController.popBackStack();
            }
        });

        // onPress backScreenBtn
        View editInfo = root.findViewById(R.id.imageView3);
        FloatingActionButton saveInfo = finalRoot.findViewById(R.id.saveInfoButton);
        // Fill EditText fields
        EditText editTextName = finalRoot.findViewById(R.id.editTextName);
        EditText editTextEmailAddress = finalRoot.findViewById(R.id.editTextEmailAddress);
        EditText editTextPhoneNumber = finalRoot.findViewById(R.id.editTextPhone);
        editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set fields as read-only
                editTextName.setEnabled(true);
                editTextPhoneNumber.setEnabled(true);

                saveInfo.setVisibility(View.VISIBLE);
                editInfo.setVisibility(View.INVISIBLE);
            }
        });

        saveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set fields as read-only
                editTextName.setEnabled(false);
                editTextEmailAddress.setEnabled(false);
                editTextPhoneNumber.setEnabled(false);
                saveInfo.setVisibility(View.INVISIBLE);
                editInfo.setVisibility(View.VISIBLE);
                // Show loading indicator overlay
                ProgressBar progressBar = new ProgressBar(getActivity());
                progressBar.setVisibility(View.VISIBLE);
                String newName = editTextName.getText().toString();
                String newEmailAddress = editTextEmailAddress.getText().toString();
                String newPhoneNumber = editTextPhoneNumber.getText().toString();

                // Update the document in Firestore
                userRef.update("fullname", newName, "email", newEmailAddress, "phone", newPhoneNumber)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Cache.userName = newName;
                                Toast.makeText(getActivity(), "Information saved successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Failed to save information", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });
            }
        });
        
        userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        editTextName.setText(document.getString("fullname"));
                        editTextEmailAddress.setText(document.getString("email"));
                        editTextPhoneNumber.setText(document.getString("phone"));

                        // Set fields as read-only
                        editTextName.setEnabled(false);
                        editTextEmailAddress.setEnabled(false);
                        editTextPhoneNumber.setEnabled(false);
                    } else {
                        Toast.makeText(getActivity(), "Something error happened", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Something error happened", Toast.LENGTH_SHORT).show();
                }
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