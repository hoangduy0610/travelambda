package com.lambda.travel.ui.LoginAndRegister;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lambda.travel.LoginRegisterActivity;
import com.lambda.travel.MainActivity;
import com.lambda.travel.R;
import com.lambda.travel.ui.forgotPassword.forgotPasswordFragment;

public class LoginFragment extends AppCompatActivity {
    EditText edtEmail,edtPassword;
    TextView textViewForgotPassword, textViewCreateAccount,txtViewForgotPassword;
    Button btnLogin;
    ImageView imgView_back;
    FirebaseAuth mAuth;
    ProgressBar progressBar ;
    boolean passwordVisible;

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
    @SuppressLint({"ClickableViewAccessibility", "MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_login_screen);
        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edt_email);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btnLogin);
        textViewForgotPassword = findViewById(R.id.txtView_ForgotPassword);
        textViewCreateAccount = findViewById(R.id.loginNow);
        imgView_back = findViewById(R.id.imageView_back_forgot);
        txtViewForgotPassword=findViewById(R.id.txtView_ForgotPassword);

        progressBar=findViewById(R.id.progressBar_login);
        txtViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forgotPasswordFragment.class);
                startActivity(intent);
                finish();
            }
        });
        textViewCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterFragment.class);
                startActivity(intent);
                finish();
            }
        });
        imgView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginRegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
                String email, password;
                email = String.valueOf(edtEmail.getText());
                password = String.valueOf(edtPassword.getText());
                if(checkIsBlank(email,password)) return; // kiểm tra đã fill chưa
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                btnLogin.setVisibility(View.VISIBLE);
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginFragment.this, "Login successful!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(LoginFragment.this, "Email or password are wrong!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        edtPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int RIGHT = 2;
                final int TOUCH_OFFSET=60;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (edtPassword.getRight() - edtPassword.getCompoundDrawables()[RIGHT].getBounds().width()-TOUCH_OFFSET)) {
                        int selection = edtPassword.getSelectionEnd();
                        if (passwordVisible) {
                            edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.invisible_24px, 0);
                            edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            edtPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.eye_24px, 0);
                            edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        edtPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private boolean checkIsBlank(String email,String password) {
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginFragment.this, "Enter email", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            return true;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginFragment.this, "Enter password", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            return true;
        }
        if(!email.matches(emailPattern))
        {
            Toast.makeText(LoginFragment.this, "Enter email", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }
}

