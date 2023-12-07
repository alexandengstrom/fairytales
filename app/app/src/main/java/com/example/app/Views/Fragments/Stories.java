package com.example.app.Views.Fragments;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.app.Models.Character;
import com.example.app.Models.Story;
import com.example.app.R;
import com.example.app.ViewModels.CharactersViewModel;
import com.example.app.ViewModels.StoriesViewModel;
import com.example.app.Views.Activities.ChooseLocationActivity;
import com.example.app.Views.Activities.Login;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Fragment responsible for letting the user create a new story.
 */
public class Stories extends Fragment implements AddedCharacter.OnCharacterRemovedListener {

    private Spinner charactersSpinner;
    private Button addButton;
    private Button createStoryButton;
    private List<Character> addedCharacters;
    private ImageView chooseLocationPin;
    private FusedLocationProviderClient client;
    private Location lastKnownLocation;
    private FirebaseAnalytics firebaseAnalytics;
    private View rootView;
    private StoriesViewModel storiesViewModel;
    private CharactersViewModel charactersViewModel;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    /**
     * Empty constructor.
     */
    public Stories() {}

    /**
     * Returns an instance.
     */
    public static Stories newInstance() {
        return new Stories();
    }

    /**
     * Links components and setups all event listeners.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_stories, container, false);

        if (savedInstanceState != null) {
            addedCharacters = savedInstanceState.getParcelableArrayList("addedCharacters");

            String storyLocation = savedInstanceState.getString("storyLocation", "");
            String storyPlot = savedInstanceState.getString("storyPlot", "");

            EditText storyLocationField = rootView.findViewById(R.id.new_story_location);
            EditText storyPlotField = rootView.findViewById(R.id.new_story_plot);

            storyLocationField.setText(storyLocation);
            storyPlotField.setText(storyPlot);

            if (addedCharacters != null) {
                for (Character character : addedCharacters) {
                    addCharacterFragment(character);
                }
            }
        } else {
            addedCharacters = new ArrayList<>();
        }

        client = LocationServices.getFusedLocationProviderClient(getActivity());
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        lastKnownLocation = null;
        charactersViewModel = new ViewModelProvider(this).get(CharactersViewModel.class);
        storiesViewModel = new ViewModelProvider(requireActivity()).get(StoriesViewModel.class);

        this.checkForLocationPermissions();
        this.linkComponents();
        this.setupEventListeners();

        return rootView;
    }


    /**
     * Responsible for adding a new character fragment when a character has been added to the story.
     */
    private void addCharacterFragment(Character character) {
        AddedCharacter fragment = AddedCharacter.newInstance(character);
        fragment.setOnCharacterRemovedListener(this);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.added_characters_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Called when a location has been chosen in the map activity.
     * Puts the chosen location in the location text field.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            String selectedLocation = data.getStringExtra("selectedLocation");
            EditText storyLocationField = getView().findViewById(R.id.new_story_location);
            storyLocationField.setText(selectedLocation);
        }
    }

    /**
     * Removes a character fragment.
     */
    @Override
    public void onCharacterRemoved(Character character) {
        addedCharacters.remove(character);
    }

    /**
     * Stores the state of the fragment on screen rotations.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        EditText storyLocationField = rootView.findViewById(R.id.new_story_location);
        EditText storyPlotField = rootView.findViewById(R.id.new_story_plot);

        outState.putString("storyLocation", storyLocationField.getText().toString());
        outState.putString("storyPlot", storyPlotField.getText().toString());

        if (addedCharacters != null) {
            outState.putParcelableArrayList("addedCharacters", new ArrayList<>(addedCharacters));
        }
    }

    /**
     * Promps the user for allowing the app the get the users current location.
     */
    private void requestLocationPermissions() {
        if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION) ||
                shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_COARSE_LOCATION)) {
        }

        requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    /**
     * Fetches the last known location if the user allowed it.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLastKnownLocation();
            }
        }
    }

    /**
     * Fetches the last known location of the user.
     */
    private void fetchLastKnownLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            client.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                if (location != null) {
                    lastKnownLocation = location;
                }
            });
        }
    }

    /**
     * Controls if the user has already allowed the app to track the position.
     * If not the user will be prompted.
     */
    private void checkForLocationPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermissions();
        } else {
            fetchLastKnownLocation();
        }
    }

    /**
     * Links all components to variables.
     */
    private void linkComponents() {
        charactersSpinner = rootView.findViewById(R.id.characters_spinner);
        addButton = rootView.findViewById(R.id.add_character_button);
        createStoryButton = rootView.findViewById(R.id.create_story_button);
        chooseLocationPin = rootView.findViewById(R.id.location_pin_icon);
    }

    /**
     * Setups all event listeners for this fragment.
     */
    private void setupEventListeners() {
        charactersViewModel.getCharacters().observe(getViewLifecycleOwner(), characters -> {
            this.handleCharactersChangedEvent(characters);
        });

        addButton.setOnClickListener(v -> {
            this.handleAddButtonClick();
        });

        chooseLocationPin.setOnClickListener(v -> {
            this.handleChooseLocation();
        });

        createStoryButton.setOnClickListener(v -> {
            this.handleCreateStoryButtonClick();
        });
    }

    /**
     * Called when the user presses the Add-character button.
     */
    private void handleAddButtonClick() {
        Character selectedCharacter = (Character) charactersSpinner.getSelectedItem();
        if (selectedCharacter != null && !addedCharacters.contains(selectedCharacter)) {
            addedCharacters.add(selectedCharacter);
            addCharacterFragment(selectedCharacter);
        }
    }

    /**
     * Starts a ChooseLocation activity when the user clicks the pin.
     */
    private void handleChooseLocation() {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Chooses location from map");
        firebaseAnalytics.logEvent("user_interaction", params);

        Intent intent = new Intent(getActivity(), ChooseLocationActivity.class);
        startActivityForResult(intent, 1);
    }

    /**
     * Responsible for collecting all information the user has added to the story and then
     * send everything to the viewmodel.
     */
    private void handleCreateStoryButtonClick() {
        EditText storyLocationField = rootView.findViewById(R.id.new_story_location);
        EditText storyPlotField = rootView.findViewById(R.id.new_story_plot);
        String storyLocation = storyLocationField.getText().toString().trim();
        String storyPlot = storyPlotField.getText().toString().trim();
        String language = Locale.getDefault().getDisplayLanguage();
        storiesViewModel.createStory(addedCharacters, storyLocation, storyPlot, lastKnownLocation, language, this.getContext());
        Toast.makeText(getActivity(), "Creating story...", Toast.LENGTH_SHORT).show();

        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Creates a new story");
        firebaseAnalytics.logEvent("user_interaction", params);

        storyLocationField.setText("");
        storyPlotField.setText("");
        addedCharacters.clear();
        clearAddedCharactersContainer();
    }

    /**
     * If a new character has been added while the user creates a story it will be updated.
     */
    private void handleCharactersChangedEvent(List<Character> characters) {
        ArrayAdapter<Character> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, characters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        charactersSpinner.setAdapter(adapter);
    }

    /**
     * Remove all character that has been added to the story. Called when a new story is generated.
     */
    private void clearAddedCharactersContainer() {
        FragmentManager fragmentManager = getChildFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();

        if (fragments != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            for (Fragment fragment : fragments) {
                if (fragment != null && fragment.getView() != null && fragment.getView().getParent() != null
                        && ((ViewGroup) fragment.getView().getParent()).getId() == R.id.added_characters_container) {
                    fragmentTransaction.remove(fragment);
                }
            }

            fragmentTransaction.commit();
        }
    }

}