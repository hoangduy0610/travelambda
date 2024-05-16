package com.lambda.travel.ui.forgotPassword;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.lambda.travel.R;
import com.lambda.travel.ui.LoginAndRegister.LoginFragment;

public class forgotPasswordFragment extends AppCompatActivity {

    TextView edt_email;
    Button btnContinue;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    ImageView img_back;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.fragment_forgotpw_screen);
        edt_email =findViewById(R.id.edt_email_forgot);
        btnContinue =findViewById(R.id.btnContinue);
        img_back=findViewById(R.id.img_back_forgot);
        progressBar =findViewById(R.id.progressBar_forgot);
        mAuth = FirebaseAuth.getInstance();
        //setOnClick
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginFragment.class);
                startActivity(intent);
                finish();
            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail())
                {
                    resetPassword();
                }
            }
        });
    }

    private void resetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        btnContinue.setVisibility(View.INVISIBLE);
        String email = String.valueOf(edt_email.getText());
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(forgotPasswordFragment.this, "Reset Password link has been sent to your register email!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginFragment.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Enter correct email id",Toast.LENGTH_SHORT).show();
                }
            }
        }) .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Error: -" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                btnContinue.setVisibility(View.VISIBLE);
            }
        });

    }
    private Boolean validateEmail()
    {
        String email=edt_email.getText().toString().trim();
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if(email.isEmpty())
        {
            Toast.makeText(forgotPasswordFragment.this, "Email can't be empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
            if(!email.matches(emailPattern))
            {
                Toast.makeText(forgotPasswordFragment.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
    }

}