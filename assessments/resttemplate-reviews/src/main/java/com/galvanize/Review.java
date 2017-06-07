package com.galvanize;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Review {

    @Id
    @GeneratedValue
    Long id;

    Integer movieId;

    String reviewer;

    String comment;

    Double starRating;

    public Review() {
    }

    public Review(Integer movieId, String reviewer, String comment, Double starRating) {
        this.movieId = movieId;
        this.reviewer = reviewer;
        this.comment = comment;
        this.starRating = starRating;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty
    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Integer getMovieId() {
        return movieId;
    }

    @JsonProperty

    public void setMovieId(Integer movieId) {
        this.movieId = movieId;
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
        return "Review{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", reviewer='" + reviewer + '\'' +
                ", comment='" + comment + '\'' +
                ", starRating=" + starRating +
                '}';
    }




}
