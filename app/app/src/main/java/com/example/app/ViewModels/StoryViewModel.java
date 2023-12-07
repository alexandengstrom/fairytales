package com.example.app.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.Models.StoriesRepository;
import com.example.app.Models.Story;

/**
 * ViewModel for the story activity.
 */
public class StoryViewModel extends ViewModel {
    private final StoriesRepository repository;
    private MutableLiveData<Story> storyLiveData;

    /**
     * Constructor, creates a repository and initializes the livedata.
     */
    public StoryViewModel() {
        this.repository = new StoriesRepository();
        this.storyLiveData = new MutableLiveData<>();
    }

    /**
     * Fetches a story from the repository based on the provided ID.
     */
    public void fetchStoryById(String storyId) {
        System.out.println("Loading story in StoryViewModel");
        repository.getStory(storyId, new StoriesRepository.StoryCallback() {
            @Override
            public void onStoryReceived(Story story) {
                storyLiveData.postValue(story);
            }
        });
    }

    /**
     * Returns the fetched story.
     */
    public LiveData<Story> getStoryLiveData() {
        return storyLiveData;
    }
}
