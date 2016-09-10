package com.example.chaseland.udacitymovieapp;

/**
 * Created by chaseland on 8/10/16.
 */
public class MovieInfo {
    public String getMoviePath() {
        return moviePath;
    }


    private String moviePath;

    public String getMovieAverageVote() {
        return movieAverageVote;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    private String movieAverageVote;

    private String releaseDate;

    private String title;


    public String getMovieDescription() {
        return movieDescription;
    }

    private String movieDescription;

    public MovieInfo(String moviePath, String movieDescription, String movieTitle, String movieAverageVote, String releaseDate) {
        this.movieDescription = movieDescription;
        this.moviePath = moviePath;
        this.releaseDate = releaseDate;
        this.movieAverageVote = movieAverageVote;
        this.title = movieTitle;
    }

}
