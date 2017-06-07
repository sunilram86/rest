package com.galvanize;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "movies.url=https://movies.example.host",
        "movies.token=abc123",
})
public class ConfigTest extends GalvanizeApiTestHelpers {

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

        server.expect(requestTo("https://movies.example.host/movies/find?title=Gremlins&year=1984"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withSuccess(this.gson.toJson(moviesServiceResponse), MediaType.APPLICATION_JSON));

        post("/reviews", payload).getAsJsonObject();

        server.verify();
    }

}
