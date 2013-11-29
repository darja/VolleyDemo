package com.demos.volley;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.demos.volley.model.mediawiki.SearchResult;
import com.demos.volley.model.mediawiki.SearchSuggestion;
import com.demos.volley.request.SimpleXmlRequest;

import java.util.List;

public class WikiSearchActivity extends ListActivity {
    private final String TAG = "VolleyDemo.WikiSearchActivity";

    private RequestQueue mRequestQueue;
    private ProgressDialog mProgressDialog;

    private EditText mSearchStringView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        mRequestQueue = Volley.newRequestQueue(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.searching));

        mSearchStringView = (EditText) findViewById(R.id.search_string);

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

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SearchResult item = (SearchResult) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(item.getUrl())) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
                    startActivity(intent);
                }
            }
        });
    }

    public void performSearch(View view) {
        mProgressDialog.show();

        String url = String.format("http://ru.wikipedia.org/w/api.php?action=opensearch&search=%s&format=xml", Uri.encode(getSearchString()));
        Log.d(TAG, "Requesting url: " + url);
        SimpleXmlRequest<SearchSuggestion> request = new SimpleXmlRequest<SearchSuggestion>(
            SearchSuggestion.class, Request.Method.GET, url,
            new Response.Listener<SearchSuggestion>() {
                @Override
                public void onResponse(SearchSuggestion response) {
                    setListAdapter(new SearchResultAdapter(response.getSearchResults()));
                    mProgressDialog.dismiss();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, error.getMessage(), error);
                    mProgressDialog.dismiss();
                    setListAdapter(null);
                    Toast.makeText(WikiSearchActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        mRequestQueue.add(request);
    }

    public String getSearchString() {
        return mSearchStringView.getText().toString();
    }

    private class SearchResultAdapter extends BaseAdapter {
        private final List<SearchResult> mItems;

        SearchResultAdapter(List<SearchResult> items) {
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
                convertView = LayoutInflater.from(WikiSearchActivity.this).inflate(R.layout.item_search_result, parent, false);
                convertView.setTag(new ViewHolder(convertView));
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            SearchResult item = mItems.get(position);

            holder.titleView.setText(item.getTitle());
            holder.descriptionView.setText(item.getDescription());

            return convertView;
        }
    }

    private class ViewHolder {
        TextView titleView;
        TextView descriptionView;

        ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.title);
            descriptionView = (TextView) view.findViewById(R.id.description);
        }
    }
}