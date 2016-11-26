package com.example.chaseland.udacitymovieapp.Background;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.chaseland.udacitymovieapp.data.MoviePosterContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by chaseland on 8/6/16.
 */
public class FetchMoviesAsyncTask extends AsyncTask<String, Void, Void> {

    private Context mContext;
    public FetchMoviesAsyncTask(Context context) {
        mContext = context;
    }


    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String movieJson = "";
        String typeOCall = "popular";
        String parameter = "popular";
        String movieApiKey = "";
        if (params.length > 1) {
            typeOCall = params[0];
            movieApiKey = params[1];

        }
        if (!typeOCall.equals("popular")) {
            parameter = "top_rated";
        }
        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org").
                    appendPath("3").


                    appendPath("movie").
                    appendPath(parameter).

                    appendQueryParameter("api_key", movieApiKey);
            String urlString = builder.toString();
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream stream = urlConnection.getInputStream();
            if (stream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0) {
                return null;
            }
            movieJson = stringBuffer.toString();


        } catch (IOException exception) {
            // log things here
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("", "Error closing stream", e);
                }
            }
        }
        Log.e("Loaded Json \n", movieJson);
        getMovies(movieJson);
        return null ;
    }

    private void getMovies(String jsonString) {


        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray array = jsonObject.getJSONArray("results");
            JSONObject object = (JSONObject) array.get(0);

            String baseImagePath = "http://image.tmdb.org/t/p/w154//";
            for (int i = 0; i < array.length(); i++) {
                JSONObject currentObject = (JSONObject) array.get(i);
                int id = currentObject.getInt("id");
                String posterPath = baseImagePath + currentObject.getString("poster_path");
                String posterDescription = currentObject.getString("overview");
                String vote = currentObject.getString("vote_average");
                String releaseDate = currentObject.getString("release_date");
                String title = currentObject.getString("title");

                ContentValues movieValues = new ContentValues();
                movieValues.put(MoviePosterContract.PosterEntry.COLUMN_MOVIE_ID, id);
                movieValues.put(MoviePosterContract.PosterEntry.COLUMN_POSTER_PATH, posterPath);
                movieValues.put(MoviePosterContract.PosterEntry.COLUMN_MOVIE_TITLE, title);
                movieValues.put(MoviePosterContract.PosterEntry.COLUMN_MOVIE_DESCRIPTION, posterDescription);
                movieValues.put(MoviePosterContract.PosterEntry.COLUMN_RELEASE_DATE, releaseDate);
                movieValues.put(MoviePosterContract.PosterEntry.COLUMN_VOTE, vote);


                // this is some janky shit I had to do because it kept throwing exceptions for repeat insertions
                try{
                    mContext.getContentResolver().insert(MoviePosterContract.PosterEntry.CONTENT_URI, movieValues);

                }
                catch(SQLException exception)
                {



                }

            }

        } catch (JSONException exception) {

        }


    }
}
