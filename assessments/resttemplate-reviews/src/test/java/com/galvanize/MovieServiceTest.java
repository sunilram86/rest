package com.galvanize;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
        "movies.url=http://movies.test.host",
        "movies.token=abc123",
})
@AutoConfigureMockMvc

public class MovieServiceTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    RestTemplate restTemplate;
    private MockRestServiceServer mockRestServiceServer;
    @Autowired
    private MovieRepository repository;

    @Autowired
    private MockMvc mvc;


    @Before
    public void setupService() {

        this.mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        this.repository.deleteAll();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();


    }

    @Test
    public void test_postReviews_with_GetRestemplateFor200_Success() throws Exception {


        String payload = getJSON("/data2.json");

        mockRestServiceServer.expect(requestTo("http://movies.test.host/movies/find?title=Gremlins&year=1984"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withSuccess(this.getJSON("/movies.json"), MediaType.APPLICATION_JSON));

        this.mvc.perform(
                post("/reviews")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.movie.year", is(1984)))
                .andExpect(jsonPath("$.review.reviewer", is("Hercules Mulligan")))
                .andExpect(jsonPath("$.review.id", notNullValue()));

        mockRestServiceServer.verify();

    }

    @Test
    public void test_postReviews_with_GetFor404_PostForReDirect_GetForSuccess() throws Exception {


        String payload = getJSON("/data3.json");

        mockRestServiceServer.expect(requestTo("http://movies.test.host/movies/find?title=SuperMan&year=1986"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withStatus(HttpStatus.NOT_FOUND));


        mockRestServiceServer.expect(requestTo("http://movies.test.host/movies"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andExpect(MockRestRequestMatchers.jsonPath("$.title", CoreMatchers.equalTo("SuperMan")))
                .andExpect(MockRestRequestMatchers.jsonPath("$.year", CoreMatchers.equalTo(1986)))
                .andRespond(withStatus(HttpStatus.FOUND).location(URI.create("https://movies.test.host/movies/Superman/1986")));

        mockRestServiceServer.expect(requestTo("https://movies.test.host/movies/Superman/1986"))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withSuccess(this.getJSON("/movies.json"), MediaType.APPLICATION_JSON));

        this.mvc.perform(
                post("/reviews")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.movie.year", is(1984)))
                .andExpect(jsonPath("$.review.reviewer", is("Sam Mike")))
                .andExpect(jsonPath("$.review.id", notNullValue()));

        mockRestServiceServer.verify();

    }


    @Test
    public void test_getReviews_with_GetFor404_PostForReDirect_GetForSuccess() throws Exception {

        Review review = new Review();
        review.setMovieId(1);
        review.setComment("super");
        review.setReviewer("Subbu");
        review.setStarRating(4.0);

        Review db=  this.repository.save(review);
        Long i= db.getId();

        String url = String.format("http://movies.test.host/movies/%s", i);

        mockRestServiceServer.expect(requestTo(url))
                .andExpect(header("Authorization", "Bearer abc123"))
                .andRespond(withSuccess(this.getJSON("/movies.json"), MediaType.APPLICATION_JSON));

        String queryurl = String.format("/reviews/%s", i);


        this.mvc.perform(
                get(queryurl)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.movie.year", is(1984)))
                .andExpect(jsonPath("$.review.reviewer", is("Subbu")))
                .andExpect(jsonPath("$.review.id", is(1)));

        mockRestServiceServer.verify();

    }

    private String getJSON(String path) throws Exception {
        URL url = this.getClass().getResource(path);
        return new String(Files.readAllBytes(Paths.get(url.getFile())));
    }


}