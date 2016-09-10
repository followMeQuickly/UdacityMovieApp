package com.example.chaseland.udacitymovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class SelectedMovie extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ImageView view = (ImageView) findViewById(R.id.selectedPoster);
        TextView title = (TextView) findViewById(R.id.movie_title);
        TextView description = (TextView) findViewById(R.id.movie_description);
        TextView vote = (TextView) findViewById(R.id.vote);
        TextView releaseDate = (TextView) findViewById(R.id.release_date);

        String imageUrl = ((String) bundle.getString(getString(R.string.movieUrl)));
        String movieTitle = ((String) bundle.get(getString(R.string.title)));
        String movieDescription = ((String) bundle.getString(getString(R.string.movieDescription)));
        String movieVote = ((String) bundle.getString(getString(R.string.averageVote)));
        String movieReleaseDate = ((String) bundle.getString(getString(R.string.releaseDate)));

        title.setText(movieTitle);
        description.setText(movieDescription);
        vote.setText(movieVote);
        releaseDate.setText(movieReleaseDate);

        view.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(this).load(imageUrl).fit().into(view);


    }

    @Override
    public void onClick(View v) {

    }
}
