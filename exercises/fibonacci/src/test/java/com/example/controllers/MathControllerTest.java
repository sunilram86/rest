package com.example.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@WebMvcTest(MathController.class)
public class MathControllerTest {
    @Autowired
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void shouldComputeTheFibonacci() throws Exception {
        this.mvc.perform(get("/math/fibonacci/15"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.x", is(15)))
                .andExpect(jsonPath("$.fibonacci", is(610)));
    }
}
