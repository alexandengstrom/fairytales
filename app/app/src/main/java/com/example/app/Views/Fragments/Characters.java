package com.example.app.Views.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app.Models.Character;
import com.example.app.R;
import com.example.app.ViewModels.CharactersViewModel;
import com.example.app.Views.Activities.Login;
import com.example.app.Views.Activities.MainActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;

/**
 * Fragment for creating a new character.
 */
public class Characters extends Fragment {
    private CharactersViewModel viewModel;

    private View rootView;
    private LinearLayout charactersLinearLayout;
    private Button createCharacterButton;
    private EditText newCharacterName;
    private EditText newCharacterAge;
    private EditText newCharacterOccupation;
    private EditText newCharacterAbout;
    private FirebaseAnalytics firebaseAnalytics;

    /**
     * Empty constructor.
     */
    public Characters() {}

    /**
     * Returns an instance of the fragment.
     */
    public static Characters newInstance() {
        Characters fragment = new Characters();
        return fragment;
    }

    /**
     * Initializes the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Setups all event listeners.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_characters, container, false);
        viewModel = new ViewModelProvider(this).get(CharactersViewModel.class);
        firebaseAnalytics = FirebaseAnalytics.getInstance(getActivity());

        linkComponents();

        if (savedInstanceState != null) {
            newCharacterName.setText(savedInstanceState.getString("name", ""));
            newCharacterAge.setText(savedInstanceState.getString("age", ""));
            newCharacterOccupation.setText(savedInstanceState.getString("occupation", ""));
        }

        createCharacterButton.setOnClickListener(v -> {
            handleCreateNewCharacter();
        });

        viewModel.getCharacterCreationStatus().observe(getActivity(), isSuccess -> {
            handleCharacterCreationStatusChanged(isSuccess);
        });

        viewModel.getCharacters().observe(getViewLifecycleOwner(), characters -> {
            handleAvailableCharactersUpdated(characters);
        });

        return rootView;
    }

    /**
     * Saves the state on screen rotations.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name", newCharacterName.getText().toString());
        outState.putString("age", newCharacterAge.getText().toString());
        outState.putString("occupation", newCharacterOccupation.getText().toString());
        outState.putString("story", newCharacterAbout.getText().toString());
    }

    /**
     * Collect the data the user has provided and sends it to the viewmodel.
     */
    private void handleCreateNewCharacter() {
        String name = newCharacterName.getText().toString().trim();
        String age = newCharacterAge.getText().toString().trim();
        String occupation = newCharacterOccupation.getText().toString().trim();
        String story = newCharacterAbout.getText().toString().trim();

        viewModel.createAndSaveCharacter(name, age, occupation, story);
    }

    /**
     * Link the components of the fragments to variables.
     */
    private void linkComponents() {
        charactersLinearLayout = rootView.findViewById(R.id.characters_list_linear_layout);
        createCharacterButton = rootView.findViewById(R.id.create_character_button);
        newCharacterName = rootView.findViewById(R.id.new_character_name);
        newCharacterAge = rootView.findViewById(R.id.new_character_age);
        newCharacterOccupation = rootView.findViewById(R.id.new_character_occupation);
        newCharacterAbout = rootView.findViewById(R.id.new_character_about);
    }

    /**
     * Display an error toast if a character was not created successfully.
     */
    private void handleCharacterCreationStatusChanged(Boolean isSuccess) {
        if (!isSuccess) {
            Toast.makeText(getActivity(), "Failed to create character",
                    Toast.LENGTH_SHORT).show();

            Bundle params = new Bundle();
            params.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Creates an invalid character");
            firebaseAnalytics.logEvent("user_interaction", params);
        }
    }

    /**
     * Re-render the available characters when a character is added or deleted.
     */
    private void handleAvailableCharactersUpdated(List<Character> characters) {
        charactersLinearLayout.removeAllViews();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (characters.isEmpty()) {
            // Maybe display a message that the user hasnt created any characters yet?
        } else {
            for (Character character : characters) {
                CharacterInformation characterInfoFragment = CharacterInformation.newInstance(character);
                fragmentTransaction.add(R.id.characters_list_linear_layout, characterInfoFragment);
            }
        }

        fragmentTransaction.commit();
    }
}
