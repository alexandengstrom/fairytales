package com.example.app.Models;


import android.location.Location;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for saving and fetching stories from the database.
 */
public class StoriesRepository {

    /**
     * Interface for callback when a new story is created-
     */
    public interface StoryCallback {
        void onStoryReceived(Story story);
    }

    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference storiesRef = database.getReference("stories");
    MutableLiveData<List<Story>> storiesList;

    /**
     * Constructor, initializes the livedata.
     */
    public StoriesRepository() {
        storiesList = new MutableLiveData<>();
    }

    /**
     * Saves a story to the database. Uses the current users ID as key.
     */
    public static void save(Story story) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            storiesRef.child(userId).push().setValue(story);
        }
    }

    /**
     * Removes a story from the database.
     */
    public void remove(Story story) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && story.getId() != null) {
            String userId = currentUser.getUid();
            storiesRef.child(userId).child(story.getId()).removeValue();
        }
    }

    /**
     * Returns a list of all available stories created by the current user.
     */
    public MutableLiveData<List<Story>> getAllStories() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            storiesRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<Story> stories = new ArrayList<>();
                    for (DataSnapshot storySnapshot : snapshot.getChildren()) {
                        Story story = storySnapshot.getValue(Story.class);
                        story.setId(storySnapshot.getKey());
                        stories.add(story);
                    }
                    storiesList.setValue(stories);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Error
                }
            });
        }
        return storiesList;
    }

    /**
     * Returns one story based on the id provided.
     */
    public void getStory(String id, StoryCallback callback) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            storiesRef.child(userId).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Story story = dataSnapshot.getValue(Story.class);
                    if (story != null) {
                        story.setId(dataSnapshot.getKey());
                    }
                    callback.onStoryReceived(story);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    callback.onStoryReceived(null);
                }
            });
        } else {
            callback.onStoryReceived(null);
        }
    }

    /**
     * Verifies if a location is withing the geofenching area where the easter egg character should
     * be included.
     */
    public boolean storyShouldIncludeEasterEgg(Location location) {
        if (location != null) {
            Location targetLocation = new Location("");
            targetLocation.setLatitude(58.400324);
            targetLocation.setLongitude(15.576291);

            return location.distanceTo(targetLocation) <= 1000;
        }

        return false;
    }

    /**
     * Returns the easter egg character.
     */
    public Character getEasterEggCharacter() {
        Character easterEgg = new Character();
        easterEgg.setName("Alexander Engström");
        easterEgg.setAge("30");
        easterEgg.setStory("Alexander is the creator of this app. He is studying programming at Linköping University. He will always be the smartest person in every story.");
        return easterEgg;
    }
}