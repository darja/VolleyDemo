package com.demos.volley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class GsonWithErrorRequest<T, E extends ApiError> extends ExtendedRequest<T> {
    private Class<T> mResponseType;
    private Class<E> mApiErrorType;

    public GsonWithErrorRequest(Class<T> responseType, Class<E> errorType, int method, String url,
                                  Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        mResponseType = responseType;
        mApiErrorType = errorType;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String jsonString = null;
        try {
            jsonString = getResponseString(response);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(e));
        }
        Gson gson = new Gson();

        try {
            E error = gson.fromJson(jsonString, mApiErrorType);
            if (error != null && error.isReasonable()) {
                return Response.error(error);
            }
        } catch (Exception ignored) {
        }

        T result = gson.fromJson(jsonString, mResponseType);

        return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
    }
}
