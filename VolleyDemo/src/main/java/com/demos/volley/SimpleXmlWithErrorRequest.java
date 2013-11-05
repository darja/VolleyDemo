package com.demos.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;

import java.io.UnsupportedEncodingException;

public class SimpleXmlWithErrorRequest<T, E extends ApiError> extends ExtendedRequest<T> {
    private final Class<T> mResponseType;
    private final Class<E> mErrorType;

    private Matcher mMatcher;

    public SimpleXmlWithErrorRequest(Class<T> responseType, Class<E> errorType,
                                 int method, String url,
                                 Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);

        mResponseType = responseType;
        mErrorType = errorType;
    }

    public void setMatcher(Matcher matcher) {
        mMatcher = matcher;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String responseString;
        try {
            responseString = getResponseString(response);
        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(e));
        }

        Serializer serializer = mMatcher == null ? new Persister() : new Persister(mMatcher);

        // check if response contains error
        try {
            E error = serializer.read(mErrorType, responseString);

            if (error != null && error.isReasonable()) {
                return Response.error(error);
            }

        } catch (Exception ignored) {
            // In seems there is no error here
        }

        // no error – parse result
        try {
            T result = serializer.read(mResponseType, responseString);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }
}