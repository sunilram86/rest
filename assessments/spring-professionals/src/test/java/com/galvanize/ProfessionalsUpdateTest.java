package com.galvanize;

import com.google.gson.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfessionalsUpdateTest {

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
    public void testUpdateWithProfessionalUser() throws Exception {

        // insert the professional who is going to authenticate
        Professional professional = new Professional();
        professional.setName("Public Petra");
        professional.setTitle("CEO");
        professional.setCompany("Widget Co");
        professional.setPublicProfile(false);
        professional.setEmail("peta@example.com");
        professional.setPassword("password1234");
        professional.setRole("PROFESSIONAL");
        professionalRepository.save(professional);

        // setup the request body
        JsonObject payload = new JsonObject();
        payload.addProperty("name", "New Name");
        payload.addProperty("company", "New Company");
        payload.addProperty("title", "New Title");
        payload.addProperty("email", "new@example.com");
        payload.addProperty("password", "new password");
        payload.addProperty("publicProfile", true);
        payload.addProperty("role", "ADMIN"); // attempt to change a field that's not allowed

        RequestBuilder request = patch("/professionals/{id}", professional.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .with( user(professional) ) // set the @AuthenticationPrinciple (current user)
                .content(payload.toString());

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(professional.getId().intValue())))
                .andExpect(jsonPath("$.name", equalTo("New Name")))
                .andExpect(jsonPath("$.company", equalTo("New Company")))
                .andExpect(jsonPath("$.title", equalTo("New Title")))
                .andExpect(jsonPath("$.publicProfile", equalTo(true)))
                .andExpect(jsonPath("$.email", equalTo("new@example.com")))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());

        // ensure that only the specified fields were updated
        Professional professionalAfterUpdate = professionalRepository.findOne(professional.getId());
        assertThat(professionalAfterUpdate.getRole(), equalTo("PROFESSIONAL"));
        assertThat(professionalAfterUpdate.getPassword(), equalTo("new password"));
    }

}
