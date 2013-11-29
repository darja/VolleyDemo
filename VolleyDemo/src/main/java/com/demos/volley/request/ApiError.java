package com.demos.volley.request;

import com.android.volley.VolleyError;

public abstract class ApiError extends VolleyError {
    public abstract boolean isReasonable();
}
