package com.example.android.popmoviesearchstage1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by karenulmer on 3/5/2018.
 */


public class Movie  implements Parcelable {


    /**
     * Movie title
     */
    private String mTitle;

    /**
     * Movie Release Date
     */
    private String mReleaseDate;
    /**
     * Movie URL
     */
    private String mMovieUrl;

    /**
     *  Overview
     */
    private String mOverview;

    /**
     *  Rating
     */
    private String mVoteAverage;

    /**
     * Poster Path
     */
    private String mPosterPath;

    int mId;


    /**
     * Create a new movie object from five inputs
     *
     * @param title    is the title of the movie
     * @param releaseDate    is the date of the movie
     * @param posterPath is the poster image of the movie
     * @param overview  is the synopsis of the movie
     */
    public Movie (int id, String title,  String releaseDate, String overview, String voteAverage, String posterPath) {

        mTitle = title;
        mReleaseDate = releaseDate;
        //mMovieUrl = movieUrl;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mId = id;
        mPosterPath = posterPath;

    }

    protected Movie(Parcel in) {
        mTitle = in.readString();
        mReleaseDate = in.readString();
        //mMovieUrl = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readString();
        mId = in.readInt();
        mPosterPath = in.readString();
    }

    /**
     * Get the title of the movie
     */
    public String getTitle() {
        return mTitle;
    }

    public int getId() {
        return mId;
    }

    /**
     * Get the year movie was released
     */
    public String getReleaseDate() {
        return mReleaseDate;
    }

    /**
     * Get the URL of the movie
     */
    public String getMovieUrl() { return mMovieUrl; }

    /**
     * Get the overview of the movie
     */
    public String getOverview() {
        return mOverview;
    }

    /**
     * Get the Rating of the movie
     */
    public String getVoteAverage() {
        return mVoteAverage;
    }

    /**
     * Get the Poster Path of the movie
     */
    public String getPosterPath() {return mPosterPath; }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        //dest.writeString(mMovieUrl);
        dest.writeString(mOverview);
        dest.writeString(mVoteAverage);
        dest.writeString(mPosterPath);
        dest.writeInt(mId);
    }
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}

