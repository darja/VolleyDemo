package com.demos.volley.request;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.transform.Matcher;

import java.io.UnsupportedEncodingException;

public class SimpleXmlRequest<T> extends ExtendedRequest<T> {
    private final Class<T> mResponseType;

    private Matcher mMatcher;


    public SimpleXmlRequest(Class<T> responseType, int method, String url,
                        Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        mResponseType = responseType;
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
        try {
            T result = serializer.read(mResponseType, responseString);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }
}
