package com.example.app.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.example.app.Models.StoriesRepository;
import com.example.app.R;
import com.example.app.ViewModels.StoryViewModel;

/**
 * Activity for displaying a story.
 */
public class Story extends AppCompatActivity {

    private StoryViewModel viewModel;
    private String storyId;
    private TextView titleTextView;
    private TextView shortDescriptionTextView;
    private TextView contentTextView;


    /**
     * Initializes the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            storyId = bundle.getString("storyId");
        }

        viewModel = new ViewModelProvider(this).get(StoryViewModel.class);
        viewModel.fetchStoryById(storyId);

        titleTextView = findViewById(R.id.story_title);
        shortDescriptionTextView = findViewById(R.id.story_short_description);
        contentTextView = findViewById(R.id.story_content);

        viewModel.getStoryLiveData().observe(this, story -> {
            handleStoryLoaded(story);
        });
    }

    /**
     * Renders the chosen story to the screen when it has been loaded.
     */
    private void handleStoryLoaded(com.example.app.Models.Story story) {
        if (story != null) {
            titleTextView.setText(story.getTitle());
            shortDescriptionTextView.setText(story.getShortDescription());
            contentTextView.setText(story.getContent());
        }
    }
}