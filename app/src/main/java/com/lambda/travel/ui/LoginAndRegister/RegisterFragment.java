
package com.lambda.travel.ui.LoginAndRegister;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.lambda.travel.LoginRegisterActivity;
import com.lambda.travel.R;

public class RegisterFragment extends AppCompatActivity {

    EditText editTextCityzen,editTextFullName,editTextPhoneNumber,editTextEmail,editTextPassword, editTextConfirmPassword;
    Button btnRegister;
    TextView textViewLoginNow;
    ImageView img;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
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
        btnRegister = findViewById(R.id.btnRegister_sub);
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
                String cityzen,fullname,phone,email,password, confirmpassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(RegisterFragment.this,"Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(com.lambda.travel.ui.LoginAndRegister.RegisterFragment.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "Account created",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }
}

