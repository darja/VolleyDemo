package com.demos.volley.model.imdb;

import org.json.JSONException;
import org.json.JSONObject;

public class Film {
    public enum Type { movie, series, episode, other }

    private final String mId;
    private final Type mType;
    private final String mTitle;
    private final int mYear;

    public Film(JSONObject obj) throws JSONException {
        mId = obj.getString("imdbID");
        mTitle = obj.getString("Title");
        mYear = obj.getInt("Year");

        Type type = Type.other;
        try {
            type = Type.valueOf(obj.getString("Type"));
        } catch (IllegalArgumentException ignored) {
        }
        mType = type;
    }

    public String getTitle() {
        return mTitle;
    }

    public Type getType() {
        return mType;
    }

    public int getYear() {
        return mYear;
    }

    public String getId() {
        return mId;
    }
}
