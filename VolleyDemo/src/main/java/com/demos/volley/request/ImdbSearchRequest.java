package com.demos.volley.request;

import android.net.Uri;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.demos.volley.model.imdb.Film;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ImdbSearchRequest extends Request<List<Film>> {
    private Response.Listener<List<Film>> mSuccessListener;

    public ImdbSearchRequest(String searchString, Response.Listener<List<Film>> successListener, Response.ErrorListener errorListener) {
        super(Method.GET, constructUrl(searchString), errorListener);
        mSuccessListener = successListener;
    }

    private static String constructUrl(String searchString) {
        return "http://www.omdbapi.com/?s=" + Uri.encode(searchString);
    }

    @Override
    protected Response<List<Film>> parseNetworkResponse(NetworkResponse response) {
        try {
            String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            JSONObject rootObj = new JSONObject(data);

            if (rootObj.has("Error")) {
                return Response.error(new VolleyError(rootObj.getString("Error")));
            }

            JSONArray resultsArray = rootObj.getJSONArray("Search");
            List<Film> result = new ArrayList<Film>(resultsArray.length());
            for (int i = 0; i < resultsArray.length(); ++i) {
                result.add(new Film(resultsArray.getJSONObject(i)));
            }

            return Response.success(result, null);

        } catch (UnsupportedEncodingException e) {
            return Response.error(new VolleyError(e));
        } catch (JSONException e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(List<Film> response) {
        if (mSuccessListener != null) {
            mSuccessListener.onResponse(response);
        }
    }
}
