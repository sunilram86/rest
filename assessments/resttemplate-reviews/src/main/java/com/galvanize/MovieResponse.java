package com.galvanize;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class MovieResponse {

    public Movie movie;

    public Review review;

    public MovieResponse() {
    }

    public MovieResponse(Movie movie, Review review) {
        this.movie = movie;
        this.review = review;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "MovieResponse{" +
                "movie=" + movie +
                ", review=" + review +
                '}';
    }
}
