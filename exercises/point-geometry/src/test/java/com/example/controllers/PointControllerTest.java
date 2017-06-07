package com.example.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PointController.class)
public class PointControllerTest {
    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldComputeStatisticsForAPairOfPoints() throws Exception {
        MockHttpServletRequestBuilder request = post("/points/statistics")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{\"x\":3, \"y\":4},{\"x\":7, \"y\":8}]");

        this.mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.distance", is(closeTo(5.65, 0.1))))
                .andExpect(jsonPath("$.slope", is(closeTo(1, 0.1))))
                .andExpect(jsonPath("$.intercept", is(closeTo(1, 0.1))));
    }
}