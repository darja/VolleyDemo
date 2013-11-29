package com.demos.volley;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.demos.volley.model.imdb.Film;
import com.demos.volley.request.ImdbSearchRequest;

import java.util.List;

public class ImdbSearchActivity extends RequestListActivity {
    private final String TAG = "VolleyDemo.ImdbSearchActivity";

    private EditText mSearchStringView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchStringView = (EditText) findViewById(R.id.search_string);

        getListView().setOnItemClickListener(mFilmDetailsSelectedListener);

        mSearchStringView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v);
                    return true;
                }
                return false;
            }
        });
    }

    public void performSearch(View view) {
        mProgressDialog.show();

        ImdbSearchRequest request = new ImdbSearchRequest(getSearchString(),
            new Response.Listener<List<Film>>() {
                @Override
                public void onResponse(List<Film> result) {
                    setListAdapter(new FilmsAdapter(result));
                    mProgressDialog.dismiss();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.getMessage(), error);
                    mProgressDialog.dismiss();
                    setListAdapter(null);
                    Toast.makeText(ImdbSearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        );
        mRequestQueue.add(request);
    }

    public String getSearchString() {
        return mSearchStringView.getText().toString();
    }

    private AdapterView.OnItemClickListener mFilmDetailsSelectedListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Film chosenFilm = (Film) parent.getItemAtPosition(position);

            Intent intent = new Intent(ImdbSearchActivity.this, ImdbFilmDetailsActivity.class);
            intent.putExtra(ImdbFilmDetailsActivity.EXTRA_ID, chosenFilm.getId());
            startActivity(intent);
        }
    };

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
                convertView = LayoutInflater.from(ImdbSearchActivity.this).inflate(R.layout.item_film, parent, false);
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
