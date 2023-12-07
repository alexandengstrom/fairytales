package com.example.app.Views.Activities;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.example.app.R;
import com.example.app.ViewModels.ChooseLocationViewModel;
import com.example.app.ViewModels.LoginViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Activity for choosing a location on a map.
 */
public class ChooseLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ChooseLocationViewModel viewModel;

    /**
     * Initializes the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        viewModel = new ViewModelProvider(this).get(ChooseLocationViewModel.class);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    /**
     * Callback for when the map is ready for use.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(this::handleMapClick);
    }

    /**
     * Handles map click events.
     */
    private void handleMapClick(LatLng point) {
        String address = viewModel.getLocation(point, this);

        Intent returnIntent = new Intent();
        returnIntent.putExtra("selectedLocation", address);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
