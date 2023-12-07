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
import com.example.app.ViewModels.LoginViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Activity for logging in user.
 */
public class Login extends AppCompatActivity {

    private LoginViewModel viewModel;

    /**
     * Initializes the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        Button loginButton = findViewById(R.id.login_button);
        TextView createNewAccount = findViewById(R.id.create_new_account_link);

        loginButton.setOnClickListener(v -> {
            loginUser();
        });

        createNewAccount.setOnClickListener(v -> {
            startActivity(new Intent(Login.this, Register.class));
        });

        viewModel.getLoginStatus().observe(this, isSuccess -> {
            handleLoginStatusChanged(isSuccess);
        });
    }

    /**
     * Collects the login data and sends it to viewmodel.
     */
    private void loginUser() {
        EditText emailField = findViewById(R.id.login_input_email);
        String email = emailField.getText().toString();
        EditText passwordField = findViewById(R.id.login_input_password);
        String password = passwordField.getText().toString();
        viewModel.loginUser(email, password);
    }

    /**
     * If user logged in successfully, navigate to main activity. Else show error toast to user.
     */
    private void handleLoginStatusChanged(Boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(Login.this, "Authentication successful.",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(Login.this, "Authentication failed.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}