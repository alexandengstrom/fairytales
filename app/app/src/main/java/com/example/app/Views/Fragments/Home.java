package com.example.app.Views.Fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.app.Models.Story;
import com.example.app.R;
import com.example.app.ViewModels.StoriesViewModel;

import java.util.List;
import java.util.Locale;

/**
 * Landing page fragment that displays all available stories and let the user search for stories.
 */
public class Home extends Fragment {

    private View rootView;
    private StoriesViewModel storiesViewModel;
    private LinearLayout storiesLinearLayout;
    private EditText editText;

    private ProgressBar loadingProgressBar;

    /**
     * Empty constructor.
     */
    public Home() {}

    /**
     * Returns an instance.
     */
    public static Home newInstance() {
        Home fragment = new Home();
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
     * Links components and setups event listeners.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        storiesViewModel = new ViewModelProvider(requireActivity()).get(StoriesViewModel.class);
        storiesLinearLayout = rootView.findViewById(R.id.storiesListLinearLayout);
        editText = rootView.findViewById(R.id.search_stories_field);

        loadingProgressBar = rootView.findViewById(R.id.loading_progressbar);
        loadingProgressBar.setVisibility(View.VISIBLE);


        storiesViewModel.getStories().observe(getViewLifecycleOwner(), stories -> {
            loadingProgressBar.setVisibility(View.GONE);
            this.handleStoriesUpdatedEvent(stories);
        });

        storiesViewModel.getLastStoryCreated().observe(getViewLifecycleOwner(), story -> {
            this.handleNewStoryCreated(story);
        });

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadingProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateSearch(s.toString());
            }
        });

        return rootView;
    }

    /**
     * Re-renders all stories when a new story has been added or removed from the database.
     */
    private void handleStoriesUpdatedEvent(List<Story> stories) {
        storiesLinearLayout.removeAllViews();


        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (stories.isEmpty()) {
            //maybe create some kind of message
        } else {
            for (int i = stories.size() - 1; i >= 0; i--) {
                Story story = stories.get(i);
                StorySummary storySummaryFragment = StorySummary.newInstance(story);
                fragmentTransaction.add(R.id.storiesListLinearLayout, storySummaryFragment);
            }
        }
        fragmentTransaction.commit();
    }

    /**
     * Responsible for sending a notification when a new story has been created.
     */
    private void handleNewStoryCreated(Story story) {
        if (story != null) {
            Intent intent = new Intent(getContext(), com.example.app.Views.Activities.Story.class);
            Bundle bundle = new Bundle();
            bundle.putString("storyId", story.getId());
            intent.putExtras(bundle);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            int requestCode = (int) System.currentTimeMillis();

            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), requestCode, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), "storyChannelId")
                    .setSmallIcon(R.drawable.book_icon)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle("New Story Created")
                    .setContentText("The story '" + story.getTitle() + "' has been created.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            int notificationId = (int) System.currentTimeMillis();

            if (notificationManager != null) {
                notificationManager.notify(notificationId, builder.build());
            }
        }
    }

    /**
     * Updates the search query when the user type something in the search text field.
     */
    private void updateSearch(String text) {
        this.storiesViewModel.updateQuery(text);
    }
}