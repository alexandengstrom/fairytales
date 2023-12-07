package com.example.app.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import com.example.app.Views.Fragments.Home;
import com.example.app.R;
import com.example.app.Views.Fragments.Stories;
import com.example.app.Views.Fragments.Characters;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Main activity.
 */
public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {
        private BottomNavigationView bottomNavigationView;
        private FirebaseAuth mAuth;
        private Stories storiesFragment;
        private Characters charactersFragment;
        private Home homeFragment;

    /**
     * Initializes the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        homeFragment = new Home();
        storiesFragment = new Stories();
        charactersFragment = new Characters();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {
            bottomNavigationView.setSelectedItemId(savedInstanceState.getInt("selectedItemId", R.id.home));
        } else {
            bottomNavigationView.setSelectedItemId(R.id.home);
        }

        setupNotifications();
    }

    /**
     * Callback for menu item changed event.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int i = item.getItemId();
        Fragment selectedFragment = null;
        String tag = null;

        if (i == R.id.home) {
            tag = "HomeFragment";
            selectedFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (selectedFragment == null) {
                selectedFragment = new Home();
            }
        }
        else if (i == R.id.stories) {
            tag = "StoriesFragment";
            selectedFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (selectedFragment == null) {
                selectedFragment = new Stories();
            }
        } else if (i == R.id.characters) {
            tag = "CharactersFragment";
            selectedFragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (selectedFragment == null) {
                selectedFragment = new Characters();
            }
        } else if (i == R.id.logout) {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, Login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, selectedFragment, tag).commit();
            return true;
        }
        return false;
    }

    /**
     * Save activities state on screen rotations.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedItemId", bottomNavigationView.getSelectedItemId());
        //outState.clear();
    }

    /**
     * Setups everything needed for notifications in the application.
     */
    private void setupNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NewStoryChannel";
            String description = "Channel for new story notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("storyChannelId", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
                Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                startActivity(intent);
            }
        }
    }
}