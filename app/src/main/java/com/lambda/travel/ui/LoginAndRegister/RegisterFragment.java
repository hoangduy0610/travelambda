
package com.lambda.travel.ui.LoginAndRegister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.lambda.travel.LoginRegisterActivity;
import com.lambda.travel.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends AppCompatActivity {

    EditText editTextCityzen,editTextFullName,editTextPhoneNumber,editTextEmail,editTextPassword, editTextConfirmPassword;
    Button btnRegister;
    TextView textViewLoginNow;
    ImageView img;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    boolean passwordVisible,confirmPasswordVisible;
    final int TOUCH_OFFSET = 60 ;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_register_screen);

        mAuth =FirebaseAuth.getInstance();
        editTextCityzen = findViewById(R.id.edt_Citizen);
        editTextFullName=findViewById(R.id.edt_fullname);
        editTextEmail =findViewById(R.id.edt_email);
        editTextPhoneNumber=findViewById(R.id.edt_phone);
        editTextPassword=findViewById(R.id.edt_password);
        editTextConfirmPassword=findViewById(R.id.edt_confirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        img= findViewById(R.id.imageView_back);
        textViewLoginNow=findViewById(R.id.loginNow);
        progressBar= findViewById(R.id.progressBar);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
            startActivity(intent);
            finish();
            }
        });
        textViewLoginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginFragment.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                if(checkValid()) // kiểm tra empty input
                {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getUid();

                                        // Create a new user record in Firestore
                                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                                        Map<String, Object> newUser = new HashMap<>();
                                        newUser.put("userId", userId);
                                        newUser.put("cityzen", editTextCityzen.getText().toString());
                                        newUser.put("fullname", editTextFullName.getText().toString());
                                        newUser.put("phone", editTextPhoneNumber.getText().toString());
                                        newUser.put("email", email);

                                        db.collection("users")
                                            .document(userId)
                                            .set(newUser)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), "Failed to create user record", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else progressBar.setVisibility(View.GONE);

            }
        });
        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[RIGHT].getBounds().width()- TOUCH_OFFSET)) {
                        int selection = editTextPassword.getSelectionEnd();
                        if (passwordVisible) {
                            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.invisible_24px, 0);
                            editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_24px, 0);
                            editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        editTextPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
        editTextConfirmPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextConfirmPassword.getRight() - editTextConfirmPassword.getCompoundDrawables()[RIGHT].getBounds().width()- TOUCH_OFFSET)) {
                        int selection = editTextConfirmPassword.getSelectionEnd();
                        if (confirmPasswordVisible) {
                            editTextConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.invisible_24px, 0);
                            editTextConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            confirmPasswordVisible = false;
                        } else {
                            editTextConfirmPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_24px, 0);
                            editTextConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            confirmPasswordVisible = true;
                        }
                        editTextConfirmPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });
    }
    public boolean checkValid()
    {
        String cityzen,fullname,phone,email,password, confirmpassword;
        cityzen = String.valueOf(editTextCityzen.getText());
        fullname = String.valueOf(editTextFullName.getText());
        phone = String.valueOf(editTextPhoneNumber.getText());
        email = String.valueOf(editTextEmail.getText());
        password = String.valueOf(editTextPassword.getText());
        confirmpassword =String.valueOf(editTextConfirmPassword.getText());
        if(TextUtils.isEmpty(cityzen))
        {
            Toast.makeText(RegisterFragment.this,"Enter cityzen", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(com.lambda.travel.ui.LoginAndRegister.RegisterFragment.this,"Enter fullname", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(com.lambda.travel.ui.LoginAndRegister.RegisterFragment.this,"Enter phone", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(RegisterFragment.this,"Enter email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(com.lambda.travel.ui.LoginAndRegister.RegisterFragment.this,"Enter password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(confirmpassword))
        {
            Toast.makeText(com.lambda.travel.ui.LoginAndRegister.RegisterFragment.this,"Enter confirm password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!confirmpassword.equals(password))
        {
            Toast.makeText(com.lambda.travel.ui.LoginAndRegister.RegisterFragment.this,"Password and password confirm don't same", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}

