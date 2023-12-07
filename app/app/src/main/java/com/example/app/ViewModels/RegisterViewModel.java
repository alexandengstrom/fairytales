package com.example.app.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ViewModel for the register activity.
 */
public class RegisterViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> registrationSuccess;

    /**
     * Constructor, initializes the members.
     */
    public RegisterViewModel() {
        mAuth = FirebaseAuth.getInstance();
        registrationSuccess = new MutableLiveData<>();
    }

    /**
     * Returns the current registration status.
     */
    public LiveData<Boolean> getRegistrationStatus() {
        return registrationSuccess;
    }

    /**
     * Tries to register a new user with the credentials provided and updates the state variable.
     */
    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        registrationSuccess.setValue(true);
                    } else {
                        registrationSuccess.setValue(false);
                    }
                });
    }
}
