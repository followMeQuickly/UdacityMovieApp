package com.example.chaseland.udacitymovieapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;


    private void FillGridView() {


        MovieApiFactory factory = new MovieApiFactory();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String tag = preferences.getString(getString(R.string.list_setting_key), "popular");
        String apiKey = getString(R.string.movieAPIKey);
        factory.execute(tag, apiKey);
        try {

            mImageAdapter = new ImageAdapter(this);
            final List<MovieInfo> posterPaths = factory.get();

            for (int i = 0; i < posterPaths.size(); i++) {
                mImageAdapter.addItem(posterPaths.get(i));

            }


            mRecyclerView = (RecyclerView) findViewById(R.id.gridview);
            //mRecyclerView.setHasFixedSize(true);
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mImageAdapter);
            /*mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(parent.getContext(), SelectedMovie.class);
                    String imagePath = (String) parent.getItemAtPosition(position);
                    MovieInfo info = posterPaths.get(position);
                    intent.putExtra(getString(R.string.movieUrl), imagePath);
                    intent.putExtra(getString(R.string.movieDescription), info.getMovieDescription());
                    intent.putExtra(getString(R.string.averageVote), info.getMovieAverageVote());
                    intent.putExtra(getString(R.string.releaseDate), info.getReleaseDate());
                    intent.putExtra(getString(R.string.title), info.getTitle());



                    startActivity(intent);


                }
            });*/
        } catch (ExecutionException exception) {

        } catch (InterruptedException exception) {

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FillGridView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}

class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.PosterViewHolder> {

    private List<MovieInfo> movieInfos;
    private Context mContext;

    public ImageAdapter(Context context) {
        movieInfos = new ArrayList<MovieInfo>();
        mContext = context;


    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(mContext);
        View view = inflator.inflate(R.layout.poster_feed_item, parent, false);
        PosterViewHolder posterViewHolder = new PosterViewHolder(view);
        return posterViewHolder;

    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {
        final int positionStuff = position;
        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SelectedMovie.class);
                MovieInfo info = movieInfos.get(positionStuff);
                intent.putExtra(mContext.getString(R.string.movieUrl), info.getMoviePath());
                intent.putExtra(mContext.getString(R.string.movieDescription), info.getMovieDescription());
                intent.putExtra(mContext.getString(R.string.averageVote), info.getMovieAverageVote());
                intent.putExtra(mContext.getString(R.string.releaseDate), info.getReleaseDate());
                intent.putExtra(mContext.getString(R.string.title), info.getTitle());

                mContext.startActivity(intent);

            }
        });
        Picasso.with(mContext).load(movieInfos.get(position).getMoviePath())

                .into(holder.posterImageView);


    }


    @Override
    public int getItemCount() {
        return movieInfos.size();
    }

    public void addItem(MovieInfo movieInfo) {
        movieInfos.add(movieInfo);
        // when you say this, it lets the recycler view know that the list has changed
        notifyItemInserted(movieInfos.size() - 1);
    }

    public class PosterViewHolder extends RecyclerView.ViewHolder {

        private ImageView posterImageView;

        public PosterViewHolder(View itemView) {
            super(itemView);
            posterImageView = (ImageView) itemView.findViewById(R.id.posterFeedImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("things", "fuck");
                }
            });
        }


    }


    /*@Override
    public int getCount() {
        return mImagePaths.length;
    }

    @Override
    public Object getItem(int position) {
        return mImagePaths[position];
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    /*@Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {


        View gridView;
        ImageView imageView = new ImageView(mContext);

        Picasso.with(mContext).load(mImagePaths[position]).fit().into(imageView);


        imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        gridView = imageView;


        return gridView;
    }*/


}

