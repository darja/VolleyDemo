package com.demos.volley;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.demos.volley.model.imdb.Film;

import java.util.List;

public class MainActivity extends Activity {
    private final String TAG = "VolleyDemo.BaseExampleActivity";

    private RequestQueue mRequestQueue;
    private ProgressDialog mProgressDialog;

    private EditText mSearchStringView;
    private ListView mFilmsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(this);
        mProgressDialog = new ProgressDialog(this);

        mSearchStringView = (EditText) findViewById(R.id.search_string);
        mFilmsListView = (ListView) findViewById(R.id.films_list);

        mSearchStringView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchImdb(v);
                    return true;
                }
                return false;
            };
        });
    }

    public void searchImdb(View view) {
        mProgressDialog.show();

        ImdbSearchRequest request = new ImdbSearchRequest(getSearchString(),
            new Response.Listener<List<Film>>() {
                @Override
                public void onResponse(List<Film> response) {
                    mFilmsListView.setAdapter(new FilmsAdapter(response));
                    mProgressDialog.dismiss();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mProgressDialog.dismiss();
                    Log.e(TAG, error.getMessage(), error);
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        );
        mRequestQueue.add(request);
    }

    public String getSearchString() {
        return mSearchStringView.getText().toString();
    }

    private class FilmsAdapter extends BaseAdapter {
        private final List<Film> mItems;

        FilmsAdapter(List<Film> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_film, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            Film item = mItems.get(position);

            holder.title.setText(item.getTitle());
            holder.additional.setText(String.valueOf(item.getYear()));
            holder.additional.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(getIcon(item.getType())), null, null, null);

            return convertView;
        }

        private int getIcon(Film.Type type) {
            switch (type) {
                case episode:
                    return R.drawable.ic_episode;
                case movie:
                    return R.drawable.ic_movie;
                case series:
                    return R.drawable.ic_series;
                default:
                    return R.drawable.ic_film_other;
            }
        }
    }

    private class ViewHolder {
        TextView title;
        TextView additional;

        ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title);
            additional = (TextView) view.findViewById(R.id.additional);
        }
    }
}
