package com.example.app.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app.Models.Character;

import android.os.Bundle;

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

import com.example.app.Models.Character;
import com.example.app.R;
import com.example.app.ViewModels.CharactersViewModel;

/**
 * Fragment for displaying a character that has already been created.
 */
public class CharacterInformation extends Fragment {

    CharactersViewModel viewModel;
    TextView characterNameTextView;
    TextView ageTextView;
    TextView occupationTextView;
    TextView storyTextView;
    Button removeCharacterButton;

    private Character character;

    /**
     * Empty constructor.
     */
    public CharacterInformation() {}

    /**
     * Returns an instance of the fragment.
     */
    public static CharacterInformation newInstance(Character character) {
        CharacterInformation fragment = new CharacterInformation();
        Bundle args = new Bundle();
        args.putParcelable("character", character);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Initializes the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            character = getArguments().getParcelable("character");
        }
    }

    /**
     * Setup the the text components to display the character information in a formatted way.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null && character == null) {
            character = savedInstanceState.getParcelable("character");
        }
        View rootView = inflater.inflate(R.layout.fragment_character_information, container, false);
        viewModel = new ViewModelProvider(this).get(CharactersViewModel.class);

        characterNameTextView = rootView.findViewById(R.id.character_name_textview);
        ageTextView = rootView.findViewById(R.id.age_textview);
        occupationTextView = rootView.findViewById(R.id.occupation_textview);
        storyTextView = rootView.findViewById(R.id.story_textview);
        removeCharacterButton = rootView.findViewById(R.id.remove_character_button);

        if (character != null) {
            characterNameTextView.setText(character.getName());
            ageTextView.setText(character.getAge() + " years old");
            occupationTextView.setText(character.getOccupation());
            storyTextView.setText(character.getStory());
        }

        removeCharacterButton.setOnClickListener(v -> {
            viewModel.removeCharacter(character);
        });

        characterNameTextView.setText(character.getName());
        ageTextView.setText(character.getAge() + " years old");
        occupationTextView.setText(character.getOccupation());
        storyTextView.setText(character.getStory());

        return rootView;
    }

    /**
     * Saves the state on screen rotations.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (character != null) {
            outState.putParcelable("character", character);
        }
    }
}
