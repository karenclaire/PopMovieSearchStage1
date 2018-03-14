package com.example.android.popmoviesearchstage1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by karenulmer on 2/20/2018.
 *
 *References used for coding guide and corrections:
 * 1) https://github.com/first087/Android-ViewHolder-Example/blob/master/app/src/main/java/com/artitk/android_viewholder_example/GridViewActivity.java
 * 2)https://github.com/ajinkya007/Popular-Movies-Stage-1
 * 3) https://github.com/bapspatil/FlickOff
 * 4)https://github.com/henriquenfaria/popular-movies-stage-1
 * 5) my previous udacity projects: NewsApp and BookApp
 * 6) https://googledevndscholars.slack.com/threads/
 * 7)https://gist.github.com/riyazMuhammad/1c7b1f9fa3065aa5a46f
 */

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    public static final String EXTRA_MOVIE = "intent_extra_movie";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(EXTRA_MOVIE)) {
                DetailsFragment detailsFragment = DetailsFragment.newInstance((Movie) intent
                        .getParcelableExtra(EXTRA_MOVIE));
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout, detailsFragment)
                        .commit();
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DetailsFragment extends Fragment {

        public DetailsFragment() {  }

        private static final String ARG_MOVIE = "arg_movie";
        public  Intent intent;

        @BindView(R.id.tv_rating)
        TextView ratingTextView;
        @BindView (R.id.tv_date) TextView dateTextView;
        @BindView (R.id.movie_poster)
        ImageView posterImageView;
        @BindView(R.id.tv_summary) TextView overviewTextView;
        @BindView(R.id.title)TextView titleTextView;

        public ArrayList<Movie> moviesList;
        Context mContext;
        private Movie mMovie;



        //Create new Fragment instance
         public static DetailsFragment newInstance(Movie movieSelected) {
         DetailsFragment fragment = new DetailsFragment();
         Bundle args = new Bundle();
         args.putParcelable(ARG_MOVIE, movieSelected);
         fragment.setArguments(args);
         return fragment;
         }

         @Override
         public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         if (getArguments() != null) {
         mMovie = getArguments().getParcelable(ARG_MOVIE);
         }
         }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_details, container, false);

            if (mMovie == null) {
                int id = intent.getIntExtra("movie_id", 0);
                int movie_position = intent.getIntExtra("movie_position", 0);
                mMovie = moviesList.get(movie_position);
                dateTextView.setText(mMovie.getReleaseDate());
                ratingTextView.setText(mMovie.getVoteAverage());
                overviewTextView.setText(mMovie.getOverview());
                titleTextView.setText(mMovie.getTitle());

                Picasso.with(mContext).setLoggingEnabled(true);

                Picasso.with(mContext)
                        .load(MovieAdapter.POSTER_PATH + mMovie.getPosterPath())
                        .into(posterImageView);
            }
            return view;
        }
    }


}



