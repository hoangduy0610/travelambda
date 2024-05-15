package com.lambda.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.lambda.travel.ui.LoginAndRegister.LoginFragment;
import com.lambda.travel.ui.LoginAndRegister.RegisterFragment;

public class LoginRegisterActivity extends AppCompatActivity {
    Button btnRegister,btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), HomeTabActivity.class);
            startActivity(intent);
            finish();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.start_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnLogin=findViewById(R.id.btnLogin_sub);
        btnRegister=findViewById(R.id.btnRegister_sub);
        btnLogin.setOnClickListener(new View.OnClickListener() {
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
                Intent intent = new Intent(getApplicationContext(), RegisterFragment.class);
                startActivity(intent);
                finish();
            }
        });
    }
}