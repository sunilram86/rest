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
public class UpdateTests {

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
    public void patchUpdateAsAnonymous() {
        Professional professional = new Professional();
        professional.setName("Public Petra");
        professional.setTitle("CEO");
        professional.setCompany("Widget Co");
        professional.setPublicProfile(true);
        professional.setEmail("admin@example.com");
        professional.setPassword("password1234");
        professional.setRole("PROFESSIONAL");
        repo.save(professional);

        JsonObject body = new JsonObject();
        body.addProperty("name", "Someone");

        RequestEntity<String> request = RequestEntity
                .patch(new UriTemplate("/professionals/{id}").expand(professional.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(body.toString());

        ResponseEntity<String> response = app.exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.UNAUTHORIZED));
    }

    @Test
    public void patchUpdateAsProfessionalToSelf() {
        Professional professional = new Professional();
        professional.setName("Public Petra");
        professional.setTitle("CEO");
        professional.setCompany("Widget Co");
        professional.setPublicProfile(true);
        professional.setEmail("professional@example.com");
        professional.setPassword("password1234");
        professional.setRole("PROFESSIONAL");
        repo.save(professional);

        JsonObject payload = new JsonObject();
        payload.addProperty("id", professional.getId());
        payload.addProperty("name", "updated name");
        payload.addProperty("title", "updated title");
        payload.addProperty("company", "updated company");
        payload.addProperty("publicProfile", true);
        payload.addProperty("email", "updated@example.com");
        payload.addProperty("password", "updated password");
        payload.addProperty("role", "ADMIN");

        RequestEntity<String> request = RequestEntity
                .patch(new UriTemplate("/professionals/{id}").expand(professional.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString());

        ResponseEntity<String> response = app.withBasicAuth("professional@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonObject updated = body.getAsJsonObject();
        assertThat(updated.get("name").getAsString(), equalTo("updated name"));
        assertThat(updated.get("title").getAsString(), equalTo("updated title"));
        assertThat(updated.get("company").getAsString(), equalTo("updated company"));
        assertThat(updated.get("email").getAsString(), equalTo("updated@example.com"));
        assertThat(updated.get("publicProfile").getAsBoolean(), equalTo(true));
        assertThat(updated.has("role"), equalTo(false));
        assertThat(updated.has("password"), equalTo(false));

        // it does _not_ allow users to change their ids
        assertThat(updated.get("id").getAsLong(), equalTo(professional.getId()));

        // it does _not_ allow users to change their own roles
        assertThat(repo.findOne(professional.getId()).getRole(), equalTo("PROFESSIONAL"));
    }

    @Test
    public void patchUpdateOtherAsProfessional() {
        Professional professional = new Professional();
        professional.setName("Public Petra");
        professional.setTitle("CEO");
        professional.setCompany("Widget Co");
        professional.setPublicProfile(true);
        professional.setEmail("professional@example.com");
        professional.setPassword("password1234");
        professional.setRole("PROFESSIONAL");
        repo.save(professional);

        Professional other = new Professional();
        other.setName("Other Ollie");
        other.setTitle("CEO");
        other.setCompany("Ooolala Co");
        other.setPublicProfile(true);
        other.setEmail("other@example.com");
        other.setPassword("password1234");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        JsonObject payload = new JsonObject();
        payload.addProperty("id", other.getId());
        payload.addProperty("name", "other");
        payload.addProperty("title", "other");
        payload.addProperty("company", "other");
        payload.addProperty("publicProfile", true);
        payload.addProperty("email", "otherother@example.com");
        payload.addProperty("password", "other");

        RequestEntity<String> request = RequestEntity
                .patch(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString());

        ResponseEntity<String> response = app.withBasicAuth("professional@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void patchUpdateAsAdmin() {
        Professional admin = new Professional();
        admin.setName("Public Petra");
        admin.setTitle("CEO");
        admin.setCompany("Widget Co");
        admin.setPublicProfile(true);
        admin.setEmail("admin@example.com");
        admin.setPassword("password1234");
        admin.setRole("ADMIN");
        repo.save(admin);

        Professional other = new Professional();
        other.setName("Other Ollie");
        other.setTitle("CEO");
        other.setCompany("Ooolala Co");
        other.setPublicProfile(false);
        other.setEmail("other@example.com");
        other.setPassword("password1234");
        other.setRole("PROFESSIONAL");
        repo.save(other);

        JsonObject payload = new JsonObject();
        payload.addProperty("id", other.getId());
        payload.addProperty("name", "updated name");
        payload.addProperty("title", "updated title");
        payload.addProperty("company", "updated company");
        payload.addProperty("publicProfile", true);
        payload.addProperty("email", "updated@example.com");
        payload.addProperty("password", "updated password");
        payload.addProperty("role", "ADMIN");

        RequestEntity<String> request = RequestEntity
                .patch(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload.toString());

        ResponseEntity<String> response = app.withBasicAuth("admin@example.com", "password1234").exchange(
                request,
                String.class
        );

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

        RequestEntity<?> showRequest = RequestEntity
                .get(new UriTemplate("/professionals/{id}").expand(other.getId()))
                .build();

        response = app.withBasicAuth("admin@example.com", "password1234").exchange(
                showRequest,
                String.class
        );

        JsonElement body = new JsonParser().parse(response.getBody());
        JsonObject professional = body.getAsJsonObject();
        assertThat(professional.get("id").getAsLong(), equalTo(other.getId()));
        assertThat(professional.get("name").getAsString(), equalTo("updated name"));
        assertThat(professional.get("title").getAsString(), equalTo("updated title"));
        assertThat(professional.get("company").getAsString(), equalTo("updated company"));
        assertThat(professional.get("email").getAsString(), equalTo("updated@example.com"));
        assertThat(professional.get("publicProfile").getAsBoolean(), equalTo(true));
        assertThat(professional.get("role").getAsString(), equalTo("ADMIN"));

        assertThat(repo.findOne(other.getId()).getPassword(), equalTo("updated password"));
    }

}
