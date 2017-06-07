package com.galvanize;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfessionalsCreateTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProfessionalRepository professionalRepository;

    @Autowired
    private FollowRepository followRepository;

    @Before
    public void clearTables(){
        followRepository.deleteAll();
        professionalRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateWithAnonymousUser() throws Exception {

        // example of a simple way to construct a JSON payload
        JsonObject payload = new JsonObject();
        payload.addProperty("name", "New Name");
        payload.addProperty("company", "New Company");
        payload.addProperty("title", "New Title");
        payload.addProperty("email", "new@example.com");
        payload.addProperty("password", "new password");
        payload.addProperty("publicProfile", true);
        payload.addProperty("role", "PROFESSIONAL");

        RequestBuilder request = post("/professionals")
                .contentType(MediaType.APPLICATION_JSON)
                .with(anonymous())
                .content(payload.toString());

        mvc.perform(request).andExpect(status().isUnauthorized());
    }

}
