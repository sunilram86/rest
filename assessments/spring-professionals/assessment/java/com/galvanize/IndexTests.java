package com.galvanize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IndexTests {

    @Autowired
    private TestRestTemplate app;

    @Autowired
    private ProfessionalRepository repo;

    @Autowired
    private FollowRepository followRepository;

    @Before
    public void clearDb() {
        followRepository.deleteAll();
        repo.deleteAll();
    }

    @Test
    public void getIndexAsAnonymousUser() {
        Professional professional = new Professional();
        professional.setName("Public Petra");
        professional.setTitle("CEO");
        professional.setCompany("Widget Co");
        professional.setPublicProfile(true);
        professional.setEmail("peta@example.com");
        professional.setPassword("password1234");
        professional.setRole("PROFESSIONAL");
        repo.save(professional);

        Professional pete = new Professional();
        pete.setName("Private Pete");
        pete.setTitle("CEO");
        pete.setCompany("Acme Security");
        pete.setPublicProfile(false);
        pete.setEmail("pete@example.com");
        pete.setPassword("password");
        pete.setRole("PROFESSIONAL");
        repo.save(pete);

        RequestEntity<?> request = RequestEntity.get(URI.create("/professionals")).build();

        ResponseEntity<String> response = app.exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonArray professionals = body.getAsJsonArray();
        assertThat(professionals.size(), equalTo(1));

        for (JsonElement e : professionals) {
            JsonObject record = e.getAsJsonObject();
            assertThat(record.has("id"), equalTo(true));
            assertThat(record.has("name"), equalTo(true));
            assertThat(record.has("title"), equalTo(true));
            assertThat(record.has("company"), equalTo(true));
            assertThat(record.has("email"), equalTo(false));
            assertThat(record.has("role"), equalTo(false));
            assertThat(record.has("publicProfile"), equalTo(false));
            assertThat(record.has("password"), equalTo(false));
        }
    }

    @Test
    public void getIndexAsProfessionalUser() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("peta@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        Professional pete = new Professional();
        pete.setName("Private Pete");
        pete.setTitle("CEO");
        pete.setCompany("Acme Security");
        pete.setPublicProfile(false);
        pete.setEmail("pete@example.com");
        pete.setPassword("password");
        pete.setRole("PROFESSIONAL");
        repo.save(pete);

        Professional cait = new Professional();
        cait.setName("Connected Caitlin");
        cait.setTitle("CEO");
        cait.setCompany("Cars R Us");
        cait.setPublicProfile(false);
        cait.setEmail("cait@example.com");
        cait.setPassword("password");
        cait.setRole("PROFESSIONAL");
        repo.save(cait);

        Professional other = new Professional();
        other.setName("Other ProfessionalView");
        other.setTitle("CEO");
        other.setCompany("Not Your Business");
        other.setPublicProfile(false);
        other.setEmail("other@example.com");
        other.setPassword("password");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        Follow follow = new Follow();
        follow.setProfessional(pete);
        follow.setFollower(cait);
        followRepository.save(follow);

        RequestEntity<?> request = RequestEntity
                .get(URI.create("/professionals"))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("pete@example.com", "password").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonArray professionals = body.getAsJsonArray();
        assertThat(professionals.size(), equalTo(2));

        for (JsonElement e : professionals) {
            JsonObject professional = e.getAsJsonObject();
            assertThat(professional.has("id"), equalTo(true));
            assertThat(professional.has("name"), equalTo(true));
            assertThat(professional.has("title"), equalTo(true));
            assertThat(professional.has("company"), equalTo(true));
            assertThat(professional.has("email"), equalTo(true));
            assertThat(professional.has("role"), equalTo(false));
            assertThat(professional.has("publicProfile"), equalTo(false));
            assertThat(professional.has("password"), equalTo(false));
        }
    }

    @Test
    public void getIndexAsAdminUser() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("peta@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        Professional pete = new Professional();
        pete.setName("Private Pete");
        pete.setTitle("CEO");
        pete.setCompany("Acme Security");
        pete.setPublicProfile(false);
        pete.setEmail("pete@example.com");
        pete.setPassword("password");
        pete.setRole("ADMIN");
        repo.save(pete);

        Professional cait = new Professional();
        cait.setName("Connected Caitlin");
        cait.setTitle("CEO");
        cait.setCompany("Cars R Us");
        cait.setPublicProfile(false);
        cait.setEmail("cait@example.com");
        cait.setPassword("password");
        cait.setRole("PROFESSIONAL");
        repo.save(cait);

        Professional other = new Professional();
        other.setName("Other ProfessionalView");
        other.setTitle("CEO");
        other.setCompany("Not Your Business");
        other.setPublicProfile(false);
        other.setEmail("other@example.com");
        other.setPassword("password");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        Follow follow = new Follow();
        follow.setProfessional(pete);
        follow.setFollower(cait);
        followRepository.save(follow);

        RequestEntity<?> request = RequestEntity
                .get(URI.create("/professionals"))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("pete@example.com", "password").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonArray professionals = body.getAsJsonArray();
        assertThat(professionals.size(), equalTo(4));

        for (JsonElement e : professionals) {
            JsonObject professional = e.getAsJsonObject();
            assertThat(professional.has("id"), equalTo(true));
            assertThat(professional.has("name"), equalTo(true));
            assertThat(professional.has("title"), equalTo(true));
            assertThat(professional.has("company"), equalTo(true));
            assertThat(professional.has("email"), equalTo(true));
            assertThat(professional.has("role"), equalTo(true));
            assertThat(professional.has("publicProfile"), equalTo(true));
            assertThat(professional.has("password"), equalTo(false));
        }
    }

}
