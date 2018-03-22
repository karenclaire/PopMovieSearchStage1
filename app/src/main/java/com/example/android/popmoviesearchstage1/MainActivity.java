package com.example.android.popmoviesearchstage1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/** * Created by karenulmer on 2/20/2018.
 *
 * References used for coding guide and corrections:
        * 1) https://github.com/first087/Android-ViewHolder-Example/blob/master/app/src/main/java/com/artitk/android_viewholder_example/GridViewActivity.java
        * 2)https://github.com/ajinkya007/Popular-Movies-Stage-1
        * 3) https://github.com/bapspatil/FlickOff
        * 4)https://github.com/henriquenfaria/popular-movies-stage-1
        * 5) my previous udacity projects: NewsApp and BookApp
        * 6) https://googledevndscholars.slack.com/threads/
        * 7)https://gist.github.com/riyazMuhammad/1c7b1f9fa3065aa5a46f
**/

public class MainActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<List<Movie>> {

    /**
     * Tag for log messages
     */
    public static final String LOG_TAG = MainActivity.class.getName();
    /**
     * URL for Movie API for tmdb
     */
    private static final String MOVIE_REQUEST_URL = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String MOVIE_TOP_RATED_URL = "http://api.themoviedb.org/3/movie/top_rated?api_key=";
    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    public static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "";
    public static final String TOP_RATED_PATH = "top_rated?";
    public static final String POPULAR_PATH = "popular?";
    /**
     * Constant value for the movie loader ID
     */
    private static final int MOVIE_LOADER_ID = 1;


    /**
     * Movies List
     */
    public ArrayList<Movie> moviesList;

    /**
     * Adapter for the list of movies
     */
    public MovieAdapter mMovieAdapter;

    public Movie mMovie;

    /**
     * List of movies
     */
    private GridView movieGridView;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    /**
     * ProgressBar that is displayed when the data is loaded
     */
    private ProgressBar mProgressBar;

    private RecyclerView mRecyclerView;


    public String sortPref;


    @BindView(R.id.tv_rating)
    TextView ratingTextView;
    @BindView(R.id.tv_date)
    TextView dateTextView;
    @BindView(R.id.movie_poster)
    ImageView posterImageView;
    @BindView(R.id.tv_summary)
    TextView overviewTextView;
    @BindView(R.id.title)
    TextView titleTextView;


    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected movie.
        // Find a reference to the {@link movieGridView} in the layout
        mRecyclerView = findViewById(R.id.recyclerview_grid);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        /*
         * Use this setting to improve performance if you know that changes in content do not
         * change the child layout size in the RecyclerView
         */
        mRecyclerView.setHasFixedSize(true);

        GridView listView = findViewById(R.id.movie_list);

        movieGridView = listView;

        mMovieAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        // Set the adapter on the {@link GridView}
        // so the list can be populated in the user interface
        movieGridView.setAdapter(mMovieAdapter);


        // Set an item click listener on the GridView, which sends an intent to a web browser
        // to open a website with more information about the selected movie.

        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                launchDetailsActivity(position);
            }


        });


        // Find the reference to the progress bar in a layout
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        // Find the reference to the empty text view in a layout and set empty view
        mEmptyStateTextView = findViewById(R.id.empty_view);
        movieGridView.setEmptyView(mEmptyStateTextView);


        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);

        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet);
        }

    }

    // Helper method to check network connection
    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String popularity = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_default_value));
        String topRated = sharedPrefs.getString(getString(R.string.pref_sorting_criteria_key), getString(R.string.pref_sorting_criteria_top_rated));

        boolean sortingCriteriaValue = false;

        if (popularity.equals(sortingCriteriaValue) == true ) {
            Uri baseUri = Uri.parse(MOVIE_TOP_RATED_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            return new MovieLoader(this, uriBuilder.toString());

        } if (sortingCriteriaValue == false) {

            Uri baseUri = Uri.parse(MOVIE_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            return new MovieLoader(this, uriBuilder.toString());
        }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {

        mEmptyStateTextView.setText(R.string.no_movies);
        mProgressBar.setVisibility(View.GONE);
        mMovieAdapter.clear();

        if (movies != null && !movies.isEmpty()) {
            mMovieAdapter.addAll(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        mMovieAdapter.clear();
    }

    private void launchDetailsActivity(int position) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(DetailsActivity.EXTRA_MOVIE, position);
        startActivity(intent);
    }

}