package com.example.android.popmoviesearchstage1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by karenulmer on 3/5/2018.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private static final int VIEW_TYPE_MOVIE_LIST = 0;
    private static final int VIEW_TYPE_MOVIE_DETAILS = 1;
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String POSTER_PATH = "http://image.tmdb.org/t/p/w185//";
    private String poster;

    private ArrayList<Movie> mMoviesList;


    /**
     * ViewHolder for fields of the movie
     */
    static class ViewHolder {
        private ImageView posterImageView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView overviewTextView;
        private TextView ratingTextView;
    }


    /**
     * @param context The current context. Used to inflate the layout file.
     * @param movies  A List of movies objects to display in a list.
     */
    private Context mContext;

    public MovieAdapter(Activity context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }


    /**
     * @return The View for the position in the AdapterView.
     */

    /**
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param viewGroup   The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup viewGroup) {



        View movieGridView = convertView;

        final Movie currentMovie = getItem(position);
        ViewHolder holder;

        if (movieGridView == null) {
            movieGridView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.ratingTextView = (TextView) movieGridView.findViewById(R.id.tv_rating);
            holder.dateTextView = (TextView) movieGridView.findViewById(R.id.tv_date);
            holder.posterImageView = (ImageView) movieGridView.findViewById(R.id.movie_poster);
            movieGridView.setTag(holder);
        }
        holder = (ViewHolder) movieGridView.getTag();
        holder.dateTextView.setText(currentMovie.getReleaseDate());
        holder.ratingTextView.setText(currentMovie.getVoteAverage());



        poster = POSTER_PATH + currentMovie.getPosterPath();

        Picasso.with(getContext()).setLoggingEnabled(true);

        Picasso.with(getContext())
                .load(poster)
                .into(holder.posterImageView);

        // Set an item click listener on the GridView, which sends an intent to a web browser
        // to open a website with more information about the selected movie.
        movieGridView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Movie movieInformation = new Movie(currentMovie.getId(), currentMovie.getTitle(),
                        currentMovie.getReleaseDate(), currentMovie.getOverview(),
                        currentMovie.getVoteAverage(), currentMovie.getPosterPath());

                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra(DetailsActivity.EXTRA_MOVIE, movieInformation);
                     intent.putExtra(DetailsActivity.EXTRA_POSTER, poster);
                    getContext().startActivity(intent);

                }
        });


        return movieGridView;


    }
}

