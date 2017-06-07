package com.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivitiesTest {

    @Autowired
    private TestRestTemplate template;

    private String getJSON(String path) throws Exception {
        URL url = this.getClass().getResource(path);
        return new String(Files.readAllBytes(Paths.get(url.getFile())));
    }

    @Test
    public void itSimplifiesActivitiesWithDetails() throws Exception {
        String requestJson = getJSON("/request1.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/vnd.galvanize.detailed+json");

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = template.exchange(
                "/activities/simplify",
                HttpMethod.POST,
                request,
                String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonArray responseList = new JsonParser().parse(response.getBody()).getAsJsonArray();
        assertThat(responseList.size(), equalTo(2));

        JsonObject firstObject = responseList.get(0).getAsJsonObject();
        assertThat(firstObject.get("userId").getAsInt(), equalTo(1467));
        assertThat(firstObject.get("user").getAsString(), equalTo("someuser"));
        assertThat(firstObject.get("email").getAsString(), equalTo("personal@example.com"));
        assertThat(firstObject.get("date").getAsString(), equalTo("2017-04-02 01:32"));
        assertThat(firstObject.get("statusText").getAsString(), equalTo("Just went snowboarding today!"));

        JsonObject secondObject = responseList.get(1).getAsJsonObject();
        assertThat(secondObject.get("userId").getAsInt(), equalTo(98732));
        assertThat(secondObject.get("user").getAsString(), equalTo("otheruser"));
        assertThat(secondObject.get("email").getAsString(), equalTo("otherprimary@example.com"));
        assertThat(secondObject.get("date").getAsString(), equalTo("2016-04-02 01:32"));
        assertThat(secondObject.get("statusText").getAsString(), equalTo("Great times!"));
    }

    @Test
    public void itSimplifiesActivitiesAsCompact() throws Exception {
        String requestJson = getJSON("/request2.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/vnd.galvanize.compact+json");

        HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = template.exchange(
                "/activities/simplify",
                HttpMethod.POST,
                request,
                String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonArray responseList = new JsonParser().parse(response.getBody()).getAsJsonArray();
        assertThat(responseList.size(), equalTo(3));

        JsonObject firstObject = responseList.get(0).getAsJsonObject();
        assertThat(firstObject.get("userId"), nullValue());
        assertThat(firstObject.get("user").getAsString(), equalTo("userblahblah"));
        assertThat(firstObject.get("email"), nullValue());
        assertThat(firstObject.get("date").getAsString(), equalTo("2017-04-02 01:32"));
        assertThat(firstObject.get("statusText").getAsString(), equalTo("Just went snowboarding today!"));

        JsonObject secondObject = responseList.get(1).getAsJsonObject();
        assertThat(secondObject.get("userId"), nullValue());
        assertThat(secondObject.get("user").getAsString(), equalTo("otheruser"));
        assertThat(secondObject.get("email"), nullValue());
        assertThat(secondObject.get("date").getAsString(), equalTo("2017-04-02 01:32"));
        assertThat(secondObject.get("statusText").getAsString(), equalTo("Great times!"));

        JsonObject thirdObject = responseList.get(2).getAsJsonObject();
        assertThat(thirdObject.get("userId"), nullValue());
        assertThat(thirdObject.get("user").getAsString(), equalTo("thirdz"));
        assertThat(thirdObject.get("email"), nullValue());
    }

}
