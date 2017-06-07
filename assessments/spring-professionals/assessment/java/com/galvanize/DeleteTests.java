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
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriTemplate;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteTests {

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
    public void deleteAsAnonymous() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("PROFESSIONAL");
        repo.save(petra);

        JsonObject body = new JsonObject();
        body.addProperty("name", "Someone");

        RequestEntity<?> request = RequestEntity
                .delete(new UriTemplate("/professionals/{id}").expand(petra.getId()))
                .build();

        ResponseEntity<String> response = app.exchange(request, String.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void deleteAsProfessionalToSelf() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("ADMIN");
        repo.save(petra);

        RequestEntity<?> request = RequestEntity
                .delete(new UriTemplate("/professionals/{id}").expand(petra.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

    @Test
    public void deleteOtherAsProfessional() {
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
                .delete(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void deleteAsAdmin() {
        Professional petra = new Professional();
        petra.setName("Public Petra");
        petra.setTitle("CEO");
        petra.setCompany("Widget Co");
        petra.setPublicProfile(true);
        petra.setEmail("petra@example.com");
        petra.setPassword("password1234");
        petra.setRole("ADMIN");
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
                .delete(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .build();

        ResponseEntity<String> response = app.withBasicAuth("petra@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
    }

}
