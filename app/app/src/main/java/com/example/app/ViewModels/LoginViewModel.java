package com.example.app.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

/**
 * ViewModel for the Login activity.
 */
public class LoginViewModel extends ViewModel {
    private FirebaseAuth mAuth;
    private MutableLiveData<Boolean> loginSuccess;

    /**
     * Constructor that initializes the members.
     */
    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
        loginSuccess = new MutableLiveData<>();
    }

    /**
     * Returns the current login status.
     */
    public LiveData<Boolean> getLoginStatus() {
        return loginSuccess;
    }

    /**
     * Tries to log in the user with the provided credentials and updates the state variable.
     */
    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loginSuccess.setValue(true);
                    } else {
                        loginSuccess.setValue(false);
                    }
                });
    }
}
