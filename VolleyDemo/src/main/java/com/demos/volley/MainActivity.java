package com.demos.volley;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
    }

    public void openImdbSearch(View view) {
        startActivity(new Intent(this, ImdbSearchActivity.class));
    }

    public void openWikiSearch(View view) {
        startActivity(new Intent(this, WikiSearchActivity.class));
    }

    public void openImdbSingleItemSearch(View view) {
        startActivity(new Intent(this, ImdbSearchSingleItemActivity.class));
    }
}