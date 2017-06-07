package com.galvanize;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)

    public class MoviesRequest {

    public String title;
    public Integer year;
    public String reviewer;
    public String comment;
    public Double starRating;


    public MoviesRequest() {
    }

    public MoviesRequest(String title, Integer year, String reviewer, String comment, Double starRating) {
        this.title = title;
        this.year = year;
        this.reviewer = reviewer;
        this.comment = comment;
        this.starRating = starRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getStarRating() {
        return starRating;
    }

    public void setStarRating(Double starRating) {
        this.starRating = starRating;
    }

    @Override
    public String toString() {
        return "MoviesRequest{" +
                "title='" + title + '\'' +
                ", year=" + year +
                ", reviewer='" + reviewer + '\'' +
                ", comment='" + comment + '\'' +
                ", starRating=" + starRating +
                '}';
    }
}