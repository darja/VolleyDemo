package com.demos.volley;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.demos.volley.model.imdb.FilmDetails;
import com.demos.volley.model.imdb.ImdbError;
import com.demos.volley.request.GsonWithErrorRequest;

public class ImdbSearchSingleItemActivity extends RequestActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_single_item);
    }

    public void performSearch(View view) {
        String url = "http://www.omdbapi.com/?t=" + Uri.encode(getSearchString());

        mProgressDialog.show();

        GsonWithErrorRequest<FilmDetails, ImdbError> request = new GsonWithErrorRequest<FilmDetails, ImdbError>(
            FilmDetails.class, ImdbError.class, Request.Method.GET, url,
            new Response.Listener<FilmDetails>() {
                @Override
                public void onResponse(FilmDetails data) {
                    showFilmDetails(data);
                    mProgressDialog.dismiss();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showError(error);
                    mProgressDialog.dismiss();
                }
            }
        );

        mRequestQueue.add(request);
    }

    public String getSearchString() {
        EditText searchStringView = (EditText) findViewById(R.id.search_string);
        return searchStringView.getText().toString();
    }

    private void showFilmDetails(FilmDetails details) {
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView yearView = (TextView) findViewById(R.id.year);
        TextView summaryView = (TextView) findViewById(R.id.summary);
        TextView actorsView = (TextView) findViewById(R.id.actors);
        TextView genreView = (TextView) findViewById(R.id.genre);
        TextView imdbRatingView = (TextView) findViewById(R.id.rating);

        titleView.setText(details.getTitle());
        yearView.setText(String.valueOf(details.getYear()));
        summaryView.setText(details.getSummary());
        actorsView.setText(details.getActors());
        genreView.setText(details.getGenre());
        imdbRatingView.setText(String.valueOf(details.getRating()));

        findViewById(R.id.details_container).setVisibility(View.VISIBLE);
        findViewById(R.id.error_message).setVisibility(View.GONE);
    }

    private void showError(VolleyError error) {
        TextView errorView = (TextView) findViewById(R.id.error_message);
        errorView.setText(error.getMessage());
        errorView.setVisibility(View.VISIBLE);
        findViewById(R.id.details_container).setVisibility(View.GONE);
    }
}