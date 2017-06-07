package com.galvanize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MoviesController {

    @Autowired
    MovieService movieService;

    @PostMapping("/reviews")
    public ResponseEntity<MovieResponse> postReviews(@RequestBody MoviesRequest moviesRequest) {


        return new ResponseEntity<>(movieService.processMovie(moviesRequest), HttpStatus.OK);
    }


    @GetMapping("/reviews/{id}")
    public ResponseEntity<MovieResponse> getReviews(@PathVariable Long id) {

        return new ResponseEntity<>(movieService.getMovie(id), HttpStatus.OK);
    }

    @GetMapping("/externalService/{id}")
    public ResponseEntity<ExternalServiceResp> getExtervice(@PathVariable Integer id)

    {
        return movieService.getExternalMovieServicebyId(id);

    }


}
