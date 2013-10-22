package com.demos.volley.model.imdb;

import org.json.JSONException;
import org.json.JSONObject;

public class Film {
    public enum Type { movie, series, episode, game;}

    private final String mId;
    private final Type mType;
    private final String mTitle;
    private final int mYear;

    public Film(JSONObject obj) throws JSONException {
        mId = obj.getString("imdbID");
        mTitle = obj.getString("Title");
        mYear = obj.getInt("Year");
        mType = Type.valueOf(obj.getString("Type"));
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
