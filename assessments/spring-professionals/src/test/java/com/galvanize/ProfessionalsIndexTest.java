package com.galvanize;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.anonymous;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfessionalsIndexTest {

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
    public void testIndexWithAnonymousUser() throws Exception {

        // example of a one way to populate the database for a test
        Professional professional1 = new Professional();
        professional1.setName("Public Petra");
        professional1.setTitle("CEO");
        professional1.setCompany("Widget Co");
        professional1.setPublicProfile(true);
        professional1.setEmail("peta@example.com");
        professional1.setPassword("password1234");
        professional1.setRole("PROFESSIONAL");
        professionalRepository.save(professional1);

        Professional professional2 = new Professional();
        professional2.setName("Private Pete");
        professional2.setTitle("CEO");
        professional2.setCompany("Acme Security");
        professional2.setPublicProfile(false);
        professional2.setEmail("pete@example.com");
        professional2.setPassword("password");
        professional2.setRole("PROFESSIONAL");
        professionalRepository.save(professional2);

        // note the `.doesNotExist` matcher - useful for checking the fields that come back
        // note also the `.hasSize` matcher - useful for checking the rows that come back
        mvc.perform(get("/professionals").with(anonymous()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].id", equalTo(professional1.getId().intValue())))
                .andExpect(jsonPath("$.[0].name", equalTo("Public Petra")))
                .andExpect(jsonPath("$.[0].company", equalTo("Widget Co")))
                .andExpect(jsonPath("$.[0].title", equalTo("CEO")))
                .andExpect(jsonPath("$.[0].publicProfile").doesNotExist())
                .andExpect(jsonPath("$.[0].email").doesNotExist())
                .andExpect(jsonPath("$.[0].password").doesNotExist())
                .andExpect(jsonPath("$.[0].role").doesNotExist());
    }

}
