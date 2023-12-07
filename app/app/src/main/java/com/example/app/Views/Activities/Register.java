package com.example.app.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.R;
import com.example.app.ViewModels.RegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity for register a new user.
 */
public class Register extends AppCompatActivity {
    private FirebaseAnalytics mFireBaseAnalytics;
    private RegisterViewModel viewModel;

    /**
     * Initializes the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        mFireBaseAnalytics = FirebaseAnalytics.getInstance(this);
        Button registerButton = findViewById(R.id.register_button);
        TextView alreadyHaveAccount = findViewById(R.id.already_have_account_link);

        registerButton.setOnClickListener(v -> {
            registerUser();
        });

        viewModel.getRegistrationStatus().observe(this, isSuccess -> {
            handleRegistrationStatusChanged(isSuccess);
        });

        alreadyHaveAccount.setOnClickListener(v -> {
            startActivity(new Intent(Register.this, Login.class));
        });
    }

    /**
     * Logging events for Firebase analytics.
     */
    private void logRegistrationEvent() {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.METHOD, "email");
        mFireBaseAnalytics.logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);
    }

    /**
     * Collects register data and sends it to the viewmodel.
     */
    private void registerUser() {
        EditText emailField = findViewById(R.id.register_email_input);
        String email = emailField.getText().toString();
        EditText passwordField = findViewById(R.id.register_password_input);
        String password = passwordField.getText().toString();
        viewModel.registerUser(email, password);
    }

    /**
     * Called every time the user tries to register a new account. Navigate to login activity on success.
     */
    private void handleRegistrationStatusChanged(Boolean isSuccess) {
        if (isSuccess) {
            logRegistrationEvent();
            startActivity(new Intent(Register.this, Login.class));
        } else {
            Toast.makeText(Register.this, "Registration failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}