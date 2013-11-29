package com.demos.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.demos.volley.model.imdb.FilmDetails;

public class ImdbFilmDetailsActivity extends Activity {
    public static final String EXTRA_ID = "id";

    private ProgressDialog mProgressDialog;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        mProgressDialog = new ProgressDialog(this);

        String id = getIntent().getStringExtra(EXTRA_ID);
        String url = "http://www.omdbapi.com/?i=" + id;

        GsonRequest<FilmDetails> request = new GsonRequest<FilmDetails>(FilmDetails.class, Request.Method.GET, url,
            new Response.Listener<FilmDetails>() {
                @Override
                public void onResponse(FilmDetails response) {
                    mProgressDialog.dismiss();
                    showFilmDetails(response);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ImdbFilmDetailsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        );

        mProgressDialog.show();
        requestQueue.add(request);
    }

    private void showFilmDetails(FilmDetails details) {
        setContentView(R.layout.activity_film_details);

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
    }
}