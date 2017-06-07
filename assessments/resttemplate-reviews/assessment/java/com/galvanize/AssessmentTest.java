package com.galvanize;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "movies.url=https://movies.test.host",
        "movies.token=abc123",
})
public class AssessmentTest extends GalvanizeApiTestHelpers {

    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer server;

    @Before
    public void setup() {
        server = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testCreate() throws Exception {
        JsonObject payload = new JsonObject();
        payload.addProperty("title", "Gremlins");
        payload.addProperty("year", 1984);
        payload.addProperty("reviewer", "Hercules Mulligan");
        payload.addProperty("comment", "I loved it!");
        payload.addProperty("starRating", 3);

        int movieId = new Random().nextInt();

        JsonObject moviesServiceResponse = new JsonObject();
        moviesServiceResponse.addProperty("id", movieId);
        moviesServiceResponse.addProperty("title", "Gremlins");
        moviesServiceResponse.addProperty("year", 1984);

        server.expect(requestTo("https://movies.test.host/movies/find?title=Gremlins&year=1984"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withSuccess(this.gson.toJson(moviesServiceResponse), MediaType.APPLICATION_JSON));

        String url = "/reviews";
        JsonObject response = post(url, payload).getAsJsonObject();

        JsonObject movie = response.getAsJsonObject("movie");
        JsonObject review = response.getAsJsonObject("review");

        assertThat(movie.get("id").getAsInt(), equalTo(movieId));
        assertThat(review.get("id"), notNullValue());

        server.verify();
    }

    @Test
    public void testCreateWithMissingMovie() throws Exception {
        JsonObject payload = new JsonObject();
        payload.addProperty("title", "Gremlins");
        payload.addProperty("year", 1984);
        payload.addProperty("reviewer", "Hercules Mulligan");
        payload.addProperty("comment", "I loved it!");
        payload.addProperty("starRating", 3);

        int movieId = new Random().nextInt();

        JsonObject moviesServiceResponse = new JsonObject();
        moviesServiceResponse.addProperty("id", movieId);
        moviesServiceResponse.addProperty("title", "Gremlins");
        moviesServiceResponse.addProperty("year", 1984);

        server.expect(requestTo("https://movies.test.host/movies/find?title=Gremlins&year=1984"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));

        server.expect(requestTo("https://movies.test.host/movies"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andExpect(MockRestRequestMatchers.jsonPath("$.title", equalTo("Gremlins")))
                .andExpect(MockRestRequestMatchers.jsonPath("$.year", equalTo(1984)))
                .andRespond(withStatus(HttpStatus.FOUND).location(URI.create("https://movies.test.host/movies/Gremlins/1984")));

        server.expect(requestTo("https://movies.test.host/movies/Gremlins/1984"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withSuccess(gson.toJson(moviesServiceResponse), MediaType.APPLICATION_JSON));

        String url = "/reviews";
        JsonObject response = post(url, payload).getAsJsonObject();

        JsonObject movie = response.getAsJsonObject("movie");
        JsonObject review = response.getAsJsonObject("review");

        assertThat(movie.get("id").getAsInt(), equalTo(movieId));
        assertThat(review.get("id"), notNullValue());

        server.verify();
    }

}
