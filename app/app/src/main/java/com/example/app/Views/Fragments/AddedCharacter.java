package com.example.app.Views.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.app.Models.Character;
import com.example.app.R;

/**
 * Fragment for displaying a character when it has been added to a story.
 */
public class AddedCharacter extends Fragment {

    private Character character;
    private OnCharacterRemovedListener listener;

    /**
     * Interface for communicating with the parent fragment.
     */
    public interface OnCharacterRemovedListener {
        void onCharacterRemoved(Character character);
    }

    /**
     * Empty constructor.
     */
    public AddedCharacter() {}

    /**
     * Returns an instance,
     */
    public static AddedCharacter newInstance(Character character) {
        AddedCharacter fragment = new AddedCharacter();
        Bundle args = new Bundle();
        args.putSerializable("character", character);
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
            character = (Character) getArguments().getSerializable("character");
        }
    }

    /**
     * Set correct string as character name and set listener to the remove button.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_added_character, container, false);

        Button removeButton = view.findViewById(R.id.remove_character_from_story_button);
        TextView characterName = view.findViewById(R.id.added_character_name);

        characterName.setText(character.toString());
        removeButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCharacterRemoved(character);
            }
            getParentFragmentManager().beginTransaction().remove(this).commit();
        });

        return view;
    }

    /**
     * Link the listener.
     */
    public void setOnCharacterRemovedListener(OnCharacterRemovedListener listener) {
        listener = listener;
    }
}
