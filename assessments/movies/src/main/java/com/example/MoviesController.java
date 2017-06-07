package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class MoviesController {

    private final MovieRepository db;

    public MoviesController(MovieRepository repo) {
        this.db = repo;
    }

    @GetMapping("/movies/find")
    public ResponseEntity<Movie> findMovie(@RequestParam String title, @RequestParam int year) {
        Movie movie = this.db.findByTitleAndYear(title, year);
        if (movie != null) {
            return new ResponseEntity<>(movie, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/movies")
    public void createMovie(@RequestBody Movie movie, HttpServletResponse response) throws IOException {
        this.db.save(movie);
        response.sendRedirect(String.format("/movies/%s/%s", movie.getYear(), movie.getTitle()));
    }

    @GetMapping("/movies/{year}/{title}")
    public Movie getMovieByYearAndTitle(@PathVariable int year, @PathVariable String title) {
        return this.db.findByTitleAndYear(title, year);
    }

    @GetMapping("/movies/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return this.db.findOne(id);
    }

}
