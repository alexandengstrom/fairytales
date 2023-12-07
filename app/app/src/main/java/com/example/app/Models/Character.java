package com.example.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.lifecycle.MutableLiveData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Datamodel for representing a character.
 */
public class Character implements Serializable, Parcelable {
    private String id;
    private String name;
    private String age;
    private String occupation;
    private String story;


    /**
     * Empty constructor.
     */
    public Character() {}

    /**
     * Constructor with member initializing.
     */
    public Character(String name, String age, String occupation, String story) {
        this.name = name;
        this.age = age;
        this.occupation = occupation;
        this.story = story;
    }

    /**
     * Getter for id.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for age.
     */
    public String getAge() {
        return age;
    }

    /**
     * Getter for occupation.
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Getter for story.
     */
    public String getStory() {
        return story;
    }

    /**
     * Setter for id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Setter for name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for age.
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * Setter for story.
     */
    public void setStory(String story) {
        this.story = story;
    }

    /**
     * Constructor with parcel.
     */
    protected Character(Parcel in) {
        id = in.readString();
        name = in.readString();
        age = in.readString();
        occupation = in.readString();
        story = in.readString();
    }

    /**
     * Parcelable and Serializable implementation.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(age);
        dest.writeString(occupation);
        dest.writeString(story);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Character> CREATOR = new Parcelable.Creator<Character>() {
        @Override
        public Character createFromParcel(Parcel in) {
            return new Character(in);
        }

        @Override
        public Character[] newArray(int size) {
            return new Character[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }

}
