package com.demos.volley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

public class GsonRequest<T> extends ExtendedRequest<T> {
    private Class<T> mResponseType;

    public GsonRequest(Class<T> responseType, int method, String url, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mResponseType = responseType;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String jsonString;
        try {
            jsonString = getResponseString(response);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(e));
        }

        Gson gson = new Gson();
        T result = gson.fromJson(jsonString, mResponseType);

        return Response.success(result, null);
    }
}
