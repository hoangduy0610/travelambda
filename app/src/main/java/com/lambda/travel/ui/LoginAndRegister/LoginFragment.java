package com.lambda.travel.ui.LoginAndRegister;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseUser;
import com.lambda.travel.MainActivity;
import com.lambda.travel.R;

public class LoginFragment extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    TextView textViewForgotPassword, textViewCreateAccount;
    ProgressBar progressBar;
    Button btnLogin;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();*/
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_login_screen);
        mAuth=FirebaseAuth.getInstance();
        edtEmail=findViewById(R.id.edt_email);
        edtPassword=findViewById(R.id.edt_password);
        btnLogin=findViewById(R.id.btnLogin_sub);
        textViewForgotPassword=findViewById(R.id.txtView_ForgotPassword);
        textViewCreateAccount = findViewById(R.id.loginNow);
        progressBar = findViewById(R.id.progressBar);
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegisterFragment.class);
                startActivity(intent);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password;
                email= String.valueOf(edtEmail.getText());
                password= String.valueOf(edtPassword.getText());
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(LoginFragment.this,"Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(LoginFragment.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginFragment.this, "Login successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginFragment.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}
