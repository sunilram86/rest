package com.galvanize;

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
import org.springframework.web.util.UriTemplate;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShowTests {

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
    public void getShowAsAnonymous() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        RequestEntity<?> request = RequestEntity
                .get(new UriTemplate("/professionals/{id}").expand(petra.getId()))
                .build();

        ResponseEntity<String> response = app.exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void getShowPublicAsOtherUnconnectedProfessional() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        Professional other = new Professional();
        other.setName("Other Ollie");
        other.setTitle("CEO");
        other.setCompany("Ooolala Co");
        other.setPublicProfile(true);
        other.setEmail("other@example.com");
        other.setPassword("password1234");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        RequestEntity<?> request = RequestEntity
                .get(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonObject professional = body.getAsJsonObject();
        assertThat(professional.get("id").getAsLong(), equalTo(other.getId()));
        assertThat(professional.has("name"), equalTo(true));
        assertThat(professional.has("title"), equalTo(true));
        assertThat(professional.has("company"), equalTo(true));
        assertThat(professional.has("email"), equalTo(true));

        assertThat(professional.has("publicProfile"), equalTo(false));
        assertThat(professional.has("role"), equalTo(false));
        assertThat(professional.has("password"), equalTo(false));
    }

    @Test
    public void getShowPrivateAsOtherUnconnectedProfessional() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        Professional other = new Professional();
        other.setName("Other Ollie");
        other.setTitle("CEO");
        other.setCompany("Ooolala Co");
        other.setPublicProfile(false);
        other.setEmail("other@example.com");
        other.setPassword("password1234");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        RequestEntity<?> request = RequestEntity
                .get(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void getShowPrivateAsOtherConnectedProfessional() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        Professional other = new Professional();
        other.setName("Other Ollie");
        other.setTitle("CEO");
        other.setCompany("Ooolala Co");
        other.setPublicProfile(false);
        other.setEmail("other@example.com");
        other.setPassword("password1234");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        Follow follow = new Follow();
        follow.setProfessional(petra);
        follow.setFollower(other);
        followRepository.save(follow);

        RequestEntity<?> request = RequestEntity
                .get(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonObject professional = body.getAsJsonObject();
        assertThat(professional.get("id").getAsLong(), equalTo(other.getId()));
        assertThat(professional.has("name"), equalTo(true));
        assertThat(professional.has("title"), equalTo(true));
        assertThat(professional.has("company"), equalTo(true));
        assertThat(professional.has("email"), equalTo(true));

        assertThat(professional.has("publicProfile"), equalTo(false));
        assertThat(professional.has("role"), equalTo(false));
        assertThat(professional.has("password"), equalTo(false));
    }

    @Test
    public void getShowPrivateAsSelfProfessional() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(false);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        RequestEntity<?> request = RequestEntity
                .get(new UriTemplate("/professionals/{id}").expand(petra.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonObject professional = body.getAsJsonObject();
        assertThat(professional.get("id").getAsLong(), equalTo(petra.getId()));
        assertThat(professional.has("name"), equalTo(true));
        assertThat(professional.has("title"), equalTo(true));
        assertThat(professional.has("company"), equalTo(true));
        assertThat(professional.has("email"), equalTo(true));
        assertThat(professional.has("publicProfile"), equalTo(true));

        assertThat(professional.has("role"), equalTo(false));
        assertThat(professional.has("password"), equalTo(false));
    }

    @Test
    public void getShowAsAdmin() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(false);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("ADMIN");
        repo.save(petra);

        Professional other = new Professional();
        other.setName("Other Ollie");
        other.setTitle("CEO");
        other.setCompany("Ooolala Co");
        other.setPublicProfile(false);
        other.setEmail("other@example.com");
        other.setPassword("password1234");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        RequestEntity<?> request = RequestEntity
                .get(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonObject professional = body.getAsJsonObject();
        assertThat(professional.get("id").getAsLong(), equalTo(other.getId()));
        assertThat(professional.has("name"), equalTo(true));
        assertThat(professional.has("title"), equalTo(true));
        assertThat(professional.has("company"), equalTo(true));
        assertThat(professional.has("email"), equalTo(true));
        assertThat(professional.has("publicProfile"), equalTo(true));
        assertThat(professional.has("role"), equalTo(true));

        assertThat(professional.has("password"), equalTo(false));
    }

}
