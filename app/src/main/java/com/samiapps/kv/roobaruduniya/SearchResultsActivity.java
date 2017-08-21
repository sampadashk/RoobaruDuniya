package com.samiapps.kv.roobaruduniya;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * Created by KV on 21/8/17.
 */

public class SearchResultsActivity extends AppCompatActivity {
    TextView tv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_search);
        tv=(TextView) findViewById(R.id.show_searchresult);
        Log.d("heyhere","here");

        handleIntent(getIntent());
    }

    @Override


        public void onNewIntent(Intent intent) {
            setIntent(intent);
            handleIntent(intent);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.searchid).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }



    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("getquery",query);
            doSearch(query);
            //use the query to search your data somehow
        }
    }
    private void doSearch(String queryStr) {
        // get a Cursor, prepare the ListAdapter
        tv.setText(queryStr);
        // and set it
    }
}
