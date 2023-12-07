package com.example.app.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app.Models.Character;
import com.example.app.Models.CharacterRepository;

import java.util.List;

/**
 * ViewModel for the characters fragment. Responsible for saving and fetching characters.
 */
public class CharactersViewModel extends ViewModel {
    private final CharacterRepository repository;
    private MutableLiveData<Boolean> characterCreatedSuccessfully;

    /**
     * Constructor, creates a repository.
     */
    public CharactersViewModel() {

        this.repository = new CharacterRepository();
        characterCreatedSuccessfully = new MutableLiveData<>();
    }

    /**
     * Saves a character to the repository.
     */
    public void saveCharacter(Character character) {
        repository.save(character);
    }

    /**
     * Removes a character from the repository.
     */
    public void removeCharacter(Character character) {
        repository.remove(character);
    }

    /**
     * Returns a list of all characters currently available for the specific user.
     */
    public LiveData<List<Character>> getCharacters() {
        return repository.getAllCharacters();
    }

    /**
     * Verifies if the character information provided is valid.
     */
    public boolean isValidInput(String name, String age, String occupation, String story) {
        return !name.isEmpty() && !age.isEmpty() && !occupation.isEmpty() && !story.isEmpty();
    }

    /**
     * Return the status of current creation of a character.
     */
    public LiveData<Boolean> getCharacterCreationStatus() {
        return characterCreatedSuccessfully;
    }

    /**
     * Verifies if a character is valid and saves it to the repository.
     */
    public void createAndSaveCharacter(String name, String age, String occupation, String story) {
        if (isValidInput(name, age, occupation, story)) {
            Character newCharacter = new Character(name, age, occupation, story);
            saveCharacter(newCharacter);
            characterCreatedSuccessfully.setValue(true);
        } else {
            characterCreatedSuccessfully.setValue(false);
        }
    }
}
