package com.example.android.popmoviesearchstage1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by karenulmer on 2/20/2018.
 *
 * References used for coding guide and corrections:
 * 1) https://github.com/first087/Android-ViewHolder-Example
 * 2)https://github.com/ajinkya007/
 * 3) https://github.com/bapspatil
 * 4) my previous udacity projects: NewsApp and BookApp
 *
 * Work in progress! NEED TO FIGURE OUT WHAT IS WRONG WITH THIS...
 */

public class DetailsActivity extends AppCompatActivity {


    public static Movie movie;
    public static Intent intent;
    public static TextView title, releaseDate, rating, summary;
    public static ImageView movie_poster;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

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
}