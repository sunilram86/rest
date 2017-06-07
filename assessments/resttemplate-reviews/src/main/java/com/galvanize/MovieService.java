package com.galvanize;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class MovieService {

    private MovieConfig movieConfig;

    @Autowired
    MovieRepository movieRepository;

    public MovieService(MovieConfig movieConfig) {
        this.movieConfig = movieConfig;
    }

    @Autowired
    RestTemplate restTemplate;


    public MovieResponse processMovie(MoviesRequest moviesRequest) {
        ResponseEntity<ExternalServiceResp> response = getExternalMovieService(moviesRequest, null);


        if (response.getStatusCode().is2xxSuccessful()) {

            return getMovieResponse(moviesRequest, response);

        }

        return null;
    }

    public MovieResponse getMovie(Long id) {

        Review dbreview = this.movieRepository.findOne(id);

        Integer movieId = dbreview.getMovieId();

        ResponseEntity<ExternalServiceResp> response = getExternalMovieServicebyId(movieId);


        if (response.getStatusCode().is2xxSuccessful()) {

            return finalResponse(dbreview, response);

        }

        return null;
    }

    private MovieResponse getMovieResponse(MoviesRequest moviesRequest, ResponseEntity<ExternalServiceResp> response) {
        ExternalServiceResp res = response.getBody();

        Review review = new Review();
        review.setMovieId(response.getBody().getId());
        review.setComment(moviesRequest.getComment());
        review.setReviewer(moviesRequest.getReviewer());
        review.setStarRating(moviesRequest.getStarRating());

        this.movieRepository.save(review);

        MovieResponse movieResponse = new MovieResponse();
        Movie movie = new Movie();
        movie.setId(response.getBody().getId());
        movie.setTitle(response.getBody().getTitle());
        movie.setYear(response.getBody().getYear());
        movieResponse.setMovie(movie);
        movieResponse.setReview(review);

        return movieResponse;
    }

    private MovieResponse finalResponse(Review dbReview, ResponseEntity<ExternalServiceResp> response) {


        MovieResponse movieResponse = new MovieResponse();
        Movie movie = new Movie();
        movie.setId(response.getBody().getId());
        movie.setTitle(response.getBody().getTitle());
        movie.setYear(response.getBody().getYear());
        movieResponse.setMovie(movie);
        movieResponse.setReview(dbReview);

        return movieResponse;
    }

    public ResponseEntity<ExternalServiceResp> getExternalMovieService(MoviesRequest moviesRequest, String newUrl) {

        URI uri;

        if (newUrl != null) {
            uri = UriComponentsBuilder
                    .fromUriString(newUrl)
                    .build()
                    .toUri();
        } else {
            uri = UriComponentsBuilder
                    .fromUriString(movieConfig.getUrl() + "/movies/find")
                    .queryParam("title", moviesRequest.getTitle())
                    .queryParam("year", moviesRequest.getYear())
                    .build()
                    .toUri();
        }


        String auth = "Bearer " + movieConfig.getToken();

        // Create the request
        RequestEntity<?> request = RequestEntity.get(uri)
                .header("Authorization", auth)
                .build();

        // Execute the request
        try {
            return restTemplate.exchange(
                    request, ExternalServiceResp.class);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode().is4xxClientError()) {

                String posturl = movieConfig.getUrl() + "/movies";
                ResponseEntity<String> resp = postExternalServiceRespResponseEntity(moviesRequest, posturl);

                if (resp.getStatusCode().is3xxRedirection()) {
                    String headerUrl = resp.getHeaders().get("Location").get(0);

                    return getExternalMovieService(moviesRequest, headerUrl);

                }

            }
        }
        return null;
    }



    @HystrixCommand(fallbackMethod = "defaultInvokeRemoteService1",
            commandProperties = {
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "4"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "30000")},
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "30000")})
    public ResponseEntity<ExternalServiceResp> getExternalMovieServicebyId(Integer id) {

        URI uri = UriComponentsBuilder
                .fromUriString(movieConfig.getUrl() + "/movies" + "/{host}")
                .buildAndExpand(id)
                .toUri();

        String auth = "Bearer " + movieConfig.getToken();

        RequestEntity<?> request = RequestEntity.get(uri)
                .header("Authorization", auth)
                .build();

        System.out.println("Calling the external webservice");
        return restTemplate.exchange(
                request, ExternalServiceResp.class);


    }

    public ResponseEntity<ExternalServiceResp> defaultInvokeRemoteService1(Integer id) {

        System.out.println("inside fallback");
        ExternalServiceResp externalServiceResp = new ExternalServiceResp();
        externalServiceResp.setMessage("Some Freaking error! Please retry later");
        return new ResponseEntity<>(externalServiceResp, HttpStatus.OK);

    }

    private ResponseEntity<String> postExternalServiceRespResponseEntity(MoviesRequest moviesRequest, String url) {

        String title = moviesRequest.getTitle();
        Integer year = moviesRequest.getYear();

        NewMovieRequest message = new NewMovieRequest();
        message.setTitle(title);
        message.setYear(year);

        URI uri = UriComponentsBuilder
                .fromUriString(url)
                .build()
                .toUri();

        // Create the request

        String authHeader = "Bearer " + movieConfig.getToken();
        RequestEntity<?> request = RequestEntity.post(uri)
                .header("Authorization", authHeader)
                .contentType(MediaType.APPLICATION_JSON)
                .body(message);

        // Execute the request
        return restTemplate.exchange(
                request, String.class);
    }

}