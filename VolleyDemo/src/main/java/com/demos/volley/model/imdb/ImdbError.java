package com.demos.volley.model.imdb;

import android.text.TextUtils;
import com.demos.volley.request.ApiError;
import com.google.gson.annotations.SerializedName;

public class ImdbError extends ApiError {
    @SerializedName("Error")
    private String mErrorMessage;

    @Override
    public boolean isReasonable() {
        return !TextUtils.isEmpty(mErrorMessage);
    }

    @Override
    public String getMessage() {
        return mErrorMessage;
    }
}
