package com.example.app.Views.Fragments;

import android.content.Intent;
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
import com.example.app.Models.Story;
import com.example.app.R;
import com.example.app.ViewModels.CharactersViewModel;

/**
 * Fragment for displaying the short description and title of a story.
 */
public class StorySummary extends Fragment {

    private Story story;

    /**
     * Empty constructor.
     */
    public StorySummary() {}

    /**
     * Returns a new instance.
     */
    public static StorySummary newInstance(Story story) {
        StorySummary fragment = new StorySummary();
        Bundle args = new Bundle();
        args.putParcelable("story", story);
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
            story = getArguments().getParcelable("story");
        }
    }


    /**
     * Links all components and unpacks the bundle.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_summary, container, false);

        TextView storyTitle = rootView.findViewById(R.id.story_title);
        TextView storyShortDescription = rootView.findViewById(R.id.story_short_description);
        LinearLayout layout = rootView.findViewById(R.id.story_summary_container);


        storyTitle.setText(story.getTitle());
        storyShortDescription.setText(story.getShortDescription());

        layout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), com.example.app.Views.Activities.Story.class);
            Bundle bundle = new Bundle();
            bundle.putString("storyId", story.getId());
            intent.putExtras(bundle);
            startActivity(intent);
        });
        return rootView;
    }

    /**
     * Saves the state on screen rotations.
     */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (story != null) {
            outState.putParcelable("story", story);
        }
        outState.clear();
    }

}
