package com.example.chaseland.udacitymovieapp.Background;

import android.content.AsyncTaskLoader;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.chaseland.udacitymovieapp.R;
import com.example.chaseland.udacitymovieapp.data.MovieReviewContract;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaseland on 11/20/16.
 */

public class ReviewLoader extends AsyncTaskLoader {
    private String mMovieId;
    private Context mContext;
    public ReviewLoader(Context context, String movieId) {
        super(context);
        mContext = context;
        mMovieId = movieId;
    }


    @Override
    public List<ReviewInfo> loadInBackground() {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String reviewJson = "";


        String movieAPIKey = mContext.getString(R.string.movieAPIKey);
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.themoviedb.org")
                .appendPath("3")
                .appendPath("movie")
                .appendPath(mMovieId)
                .appendPath("reviews")
                .appendQueryParameter("api_key", movieAPIKey);

        HttpURLConnection connection = null;

        try {
            URL url = new URL(builder.toString());
            urlConnection = (HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream stream = urlConnection.getInputStream();
            if(stream == null){
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null){
                stringBuffer.append(line +"\n");
            }
            if(stringBuffer.length() == 0){
                return null;
            }
            reviewJson = stringBuffer.toString();



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try {
                    reader.close();
                }catch (final IOException e){
                    Log.e("", "Error closing stream", e);
                }
            }
            Log.e("Loaded Json \n", reviewJson);
            List<ReviewInfo> reviews = getReviews(reviewJson);
            return reviews;

        }

    }
    private List<ReviewInfo> getReviews(String jsonString){
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            String id = jsonObject.getString("id");
            JSONArray array = jsonObject.getJSONArray("results");
            List<ReviewInfo> reviews = new ArrayList<>();
            for(int i = 0; i < array.length(); i++){
                JSONObject currentObject  = (JSONObject)array.get(i);
                String author  =  currentObject.getString("author");
                String content = currentObject.getString("content");
                String url = currentObject.getString("url");

                ReviewInfo info = new ReviewInfo(author, content, 0, url);
                reviews.add(info);


            }
            return reviews;


        } catch (JSONException exception){

        }
        return null;
    }
    public class ReviewInfo{
        private String mAuthor;
        private String mURL;
        private int mID;
        private String mContent;

        public String getmAuthor() { return mAuthor; }
        public String getUrl() {return mURL; }
        public int getID() {return mID;}
        public String getContent(){ return mContent;}

        public ReviewInfo(String author, String content, int id, String url)
        {
            mAuthor = author;
            mContent = content;
            mID = id;
            mURL = url;
        }

    }

}

