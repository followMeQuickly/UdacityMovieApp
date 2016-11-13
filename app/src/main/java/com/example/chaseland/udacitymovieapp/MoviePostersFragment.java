package com.example.chaseland.udacitymovieapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chaseland.udacitymovieapp.data.MoviePosterContract;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MoviePostersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MoviePostersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MoviePostersFragment extends Fragment {


    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;

    private static final String[] NOTIFY_MOVIE_PROJECTION = new String[] {

            MoviePosterContract.PosterEntry.COLUMN_POSTER_PATH,
            MoviePosterContract.PosterEntry.COLUMN_MOVIE_TITLE,
            MoviePosterContract.PosterEntry.COLUMN_MOVIE_DESCRIPTION,
            MoviePosterContract.PosterEntry.COLUMN_RELEASE_DATE,

            MoviePosterContract.PosterEntry.COLUMN_VOTE
    };

    public MoviePostersFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static MoviePostersFragment newInstance() {
        MoviePostersFragment fragment = new MoviePostersFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private void FillGridView(View view)  {


        FetchMoviesAsyncTask factory = new FetchMoviesAsyncTask(getActivity());
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String tag = preferences.getString(getString(R.string.list_setting_key), "popular");
        String apiKey = getString(R.string.movieAPIKey);
        factory.execute(tag, apiKey);



        mImageAdapter = new ImageAdapter(getActivity());
        Cursor cursor = getActivity().getContentResolver().query(MoviePosterContract.PosterEntry.CONTENT_URI, NOTIFY_MOVIE_PROJECTION, null, null, null);


        int f =  cursor.getCount();
        for(int i = 0; i < cursor.getCount(); i++)
        {
            cursor.moveToNext();
            MovieInfo info = new MovieInfo(cursor.getString(0),
                    cursor.getString(2),
                    cursor.getString(1),
                    cursor.getString(3),
                    cursor.getString(4));
            mImageAdapter.addItem(info);
        }
        cursor.close();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.gridview);
        mRecyclerView.setHasFixedSize(false);

        int tilePadding = 8;
        mRecyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        mRecyclerView.setAdapter(mImageAdapter);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_movie_posters, container, false);
        FillGridView(view);
        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            //mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
