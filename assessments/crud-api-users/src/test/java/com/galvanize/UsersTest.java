package com.galvanize;

import com.galvanize.Repository.UserRespository;
import com.galvanize.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest

public class UsersTest {
    @Autowired
    private UserRespository repository;

    @Autowired
    private MockMvc mvc;


    public String loadDataAndReturnurl() {

        User u = new User();
        u.setEmail("email1@email.com");
        u.setPassword("password");
        User newuser = repository.save(u);
        Integer i = newuser.getId();
        String url = String.format("/users/%s", i);

        return url;


    }

    @Test
    @Transactional
    @Rollback
    public void testgetlUserById() throws Exception {
        String url = loadDataAndReturnurl();


        this.mvc.perform(
                get(url)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email", is("email1@email.com")));
    }


    @Test
    @Transactional
    @Rollback
    public void testcreateUser() throws Exception {

        String json = getJSON("/data1.json");
        this.mvc.perform(
                post("/users")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("topike5@example.com")));
    }


    @Test
    public void testupdateUserEmailAndPasswordbyID() throws Exception {

        String json1 = getJSON("/data3.json");

        String url = loadDataAndReturnurl();


        this.mvc.perform(
                patch(url)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john@example.com")));
    }


    @Test
    public void testupdateUserEmailbyID() throws Exception {

        String json1 = getJSON("/data2.json");

        String url = loadDataAndReturnurl();


        this.mvc.perform(
                patch(url)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("john1@example.com")));
    }


    @Test
    public void testgetAllUsers() throws Exception {

        this.mvc.perform(
                get("/users")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users[0].email", is("john@example.com")));
    }


    @Test
    public void testdeleteUserById() throws Exception {
        String url = loadDataAndReturnurl();


        this.mvc.perform(
                delete(url)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count", is(2)));
    }


    @Test
    public void testauthenticateUser_true() throws Exception {

        String json1 = getJSON("/data4.json");

        this.mvc.perform(
                post("/users/authenticate")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(true)));
    }

    @Test
    public void testauthenticateUser_false() throws Exception {

        String json1 = getJSON("/data5.json");

        this.mvc.perform(
                post("/users/authenticate")
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(json1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated", is(false)));
    }

    private String getJSON(String path) throws Exception {
        URL url = this.getClass().getResource(path);
        return new String(Files.readAllBytes(Paths.get(url.getFile())));
    }
}
