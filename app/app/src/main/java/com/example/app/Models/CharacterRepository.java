package com.example.app.Models;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for saving and fetching characters from the database.
 */
public class CharacterRepository {
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static final DatabaseReference charactersRef = database.getReference("characters");

    /**
     * Saves a character to the database. Uses the users ID a key.
     */
    public void save(Character character) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            charactersRef.child(userId).push().setValue(character);
        }
    }

    /**
     * Removes a character from the database.
     */
    public void remove(Character character) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null && character.getId() != null) {
            String userId = currentUser.getUid();
            charactersRef.child(userId).child(character.getId()).removeValue();
        }
    }

    /**
     * Returns all characters from the database created by the current user.
     */
    public MutableLiveData<List<Character>> getAllCharacters() {
        MutableLiveData<List<Character>> charactersList = new MutableLiveData<>();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            charactersRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    List<Character> characters = new ArrayList<>();
                    for (DataSnapshot characterSnapshot : snapshot.getChildren()) {
                        Character character = characterSnapshot.getValue(Character.class);
                        character.setId(characterSnapshot.getKey());
                        characters.add(character);
                    }
                    charactersList.setValue(characters);
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Error
                }
            });
        }
        return charactersList;
    }
}
