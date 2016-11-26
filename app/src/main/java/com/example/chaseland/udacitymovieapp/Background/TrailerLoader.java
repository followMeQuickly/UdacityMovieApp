package com.example.chaseland.udacitymovieapp.Background;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.chaseland.udacitymovieapp.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaseland on 11/26/16.
 */

public class TrailerLoader extends AsyncTaskLoader {

    private Context mContext;
    private String mMovieID;
    public TrailerLoader(Context context, String movieID) {
        super(context);
        mContext = context;
        mMovieID = movieID;

    }
    @Override
    public Object loadInBackground() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewJson = "";


        String movieAPIKey = mContext.getString(R.string.movieAPIKey);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(mMovieID)
                .appendPath("trailer")
                .appendQueryParameter("api_key", movieAPIKey);

        try {
            URL url = new URL(builder.toString());
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
            reviewJson = stringBuffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
            Log.e("Loaded Json \n", reviewJson);
            List<TrailerInfo> trailers = getTrailers(reviewJson);
            return trailers;

        }
    }

    public List<TrailerInfo> getTrailers(String trailerJson)
    {
        List<TrailerInfo> trailers = new ArrayList<>();
        return trailers;

    }
    public class TrailerInfo{
        public TrailerInfo(){

        }
    }
}
