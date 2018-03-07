package com.example.android.popmoviesearchstage1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karenulmer on 3/5/2018.
 */

public class MovieUtils {

         /**
         * Tag for log messages
         */
        public static final String LOG_TAG = MovieUtils.class.getName();

        private MovieUtils() {
        }

        /**
         * Query the TMDB API and return a list of {@link Movie} objects.
         */

        public static List<Movie> fetchMovieData(String requestUrl) {

            URL url = createUrl(requestUrl);
            String jsonResponse = null;

            try {
                jsonResponse = makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem making the HTTP request.", e);
            }

            // Extract relevant fields from the JSON response and create a list of {@link Movie}s
            List<Movie> movies = extractFeatureFromJson(jsonResponse);

            // Return the list of {@link Book}s
            return movies;
        }

        /**
         * Returns new URL object from the given string URL.
         */
        private static URL createUrl(String stringUrl) {
            URL url = null;
            try {
                url = new URL(stringUrl);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Problem building the URL ", e);
            }
            return url;
        }

        /**
         * Make an HTTP request to the given URL and return a String as the response.
         */
        private static String makeHttpRequest(URL url) throws IOException {
            String jsonResponse = "";

            // If the URL is null, then return early.
            if (url == null) {
                return jsonResponse;
            }

            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the movies JSON results.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    // Closing the input stream could throw an IOException, which is why
                    // the makeHttpRequest(URL url) method signature specifies than an IOException
                    // could be thrown.
                    inputStream.close();
                }
            }
            return jsonResponse;
        }

        /**
         * Convert the {@link InputStream} into a String which contains the
         * whole JSON response from the server.
         */
        private static String readFromStream(InputStream inputStream) throws IOException {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            }
            return output.toString();
        }

        /**
         * Return a list of {@link Movie} objects that has been built up from
         * parsing the given JSON response.
         */
        private static List<Movie> extractFeatureFromJson(String movieJSON) {
            // If the JSON string is empty or null, then return early.
            if (TextUtils.isEmpty(movieJSON)) {
                return null;
            }

            // Create an empty ArrayList that we can start adding movies to
            List<Movie> movies = new ArrayList<>();

            try {

                JSONObject baseJsonResponse = new JSONObject(movieJSON);
                JSONArray moviesArray = baseJsonResponse.getJSONArray("results");

                for (int i = 0; i < moviesArray.length(); i++) {

                    // Get a single movie at position i within the list of movies
                    JSONObject currentMovie = moviesArray.getJSONObject(i);

                    // Extract the value for the key called "id"
                    int id = currentMovie.getInt("id");

                    // Extract the value for the key called "title"
                    String title = currentMovie.getString("title");

                    // For a given movie, extract the JSONObject associated with the
                    // key called vote_average which represents movie rating.
                    String voteAverage = currentMovie.getString("vote_average");

                    // Extract the value for the key called "release_date"
                    String releaseDate = currentMovie.getString("release_date");

                    // Extract the image URL for the key called "poster_path" for poster image
                    //String imageUrl = currentMovie.getString("poster_path");
                    String posterPath = currentMovie.getString("poster_path");

                    // Extract the value for the key called "release_date"
                    String overview = currentMovie.getString("overview");


                    // Create a new {@link Movie} object with the title,  release date, average vote, imageUrl and id
                    // and url from the JSON response.
                    Movie movie = new Movie(id, title, releaseDate, overview, voteAverage,  posterPath );

                    // Add the new {@link Movie} to the list of movies.
                    movies.add(movie);


                }
            }catch(JSONException e){
                Log.e("QueryUtils", "Problem parsing the News JSON results", e);
            }
            return movies;
        }
    }



