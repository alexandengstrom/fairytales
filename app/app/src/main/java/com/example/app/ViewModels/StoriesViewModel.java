package com.example.app.ViewModels;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.app.Models.Character;
import com.example.app.Models.CharacterRepository;
import com.example.app.Models.OpenAI;
import com.example.app.Models.StoriesRepository;
import com.example.app.Models.Story;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * ViewModel for stories fragment. Responsible for saving and fetching stories.
 */
public class StoriesViewModel extends ViewModel implements OpenAI.ResponseCallback {
    private OpenAI api;
    private final StoriesRepository repository;
    private MutableLiveData<Story> lastStoryCreated;
    private LiveData<List<Story>> allStories;
    private MediatorLiveData<List<Story>> filteredStories;
    private MutableLiveData<String> query;

    /**
     * Constructor, initializes all members.
     */
    public StoriesViewModel() {
        this.repository = new StoriesRepository();
        this.api = new OpenAI();
        this.lastStoryCreated = new MutableLiveData<Story>();
        this.query = new MutableLiveData<>();
        query.setValue("");

        this.allStories = repository.getAllStories();
        this.filteredStories = new MediatorLiveData<>();
        this.filteredStories.addSource(allStories, this::filterStories);
        this.filteredStories.addSource(query, q -> filterStories(allStories.getValue()));
    }

    /**
     * Filter the list of available stories based on the current search query.
     */
    private void filterStories(List<Story> stories) {
        if (stories == null) {
            return;
        }

        String currentQuery = query.getValue();
        List<Story> filtered = stories.stream()
                .filter(story -> story.getTitle().toLowerCase().contains(currentQuery.toLowerCase()))
                .collect(Collectors.toList());
        filteredStories.setValue(filtered);
    }

    /**
     * Returns a list of all the filtered stories.
     */
    public LiveData<List<Story>> getStories() {
        return filteredStories;
    }

    /**
     * Creates a new story. Verifies if the easte egg character should be included by asking the
     * repository. The method will also verify that the language is one of the allowed languages.
     * Sends a request to the api when all the data is verified.
     */
    public void createStory(List<Character> characters, String location, String plot, Location lastKnownLocation, String language, Context context) {
        if (repository.storyShouldIncludeEasterEgg(lastKnownLocation)) {
            characters.add(repository.getEasterEggCharacter());
        }

        String filteredLanguage;

        if (language.equals("svenska")
                || language.equals("français")
                || language.equals("Deutsch")
                || language.equals("español")) {
            filteredLanguage = language;
        } else {
            filteredLanguage = "English";

        }

        api.requestStory(characters, location, plot, filteredLanguage, context, this);
    }

    /**
     * Returns the last story created.
     */
    public LiveData<Story> getLastStoryCreated() {
        return lastStoryCreated;
    }

    /**
     * Interface for callbacks when a story has been created.
     */
    @Override
    public void onSuccess(Story story) {
        lastStoryCreated.postValue(story);
    }

    @Override
    public void onError(VolleyError error) {
    }

    /**
     * Updates the search query.
     */
    public void updateQuery(String query) {
        this.query.setValue(query);
    }
}
