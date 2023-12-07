package com.example.app.Models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

/**
 * Datamodel for representing a story.
 */
public class Story implements Parcelable {
    private String id;
    private String title;
    private String shortDescription;
    private String content;

    /**
     * Empty constructor.
     */
    public Story() {};

    /**
     * Constructor with member initialization.
     */
    public Story(String title, String shortDescription, String content) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.content = content;
    };

    /**
     * Getter for title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Getter for short description.
     */
    public String getShortDescription() {
        return this.shortDescription;
    }

    /**
     * Getter for content.
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Getter for id.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Setter for id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Implementation of Parcelable interface.
     */
    protected Story(Parcel in) {
        id = in.readString();
        title = in.readString();
        shortDescription = in.readString();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(shortDescription);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Story> CREATOR = new Parcelable.Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    @Override
    public String toString() {
        return title;
    }
}
