package com.example.controller;

import com.example.controller.CategoryController;
import com.example.model.Category;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    CategoryController categoryController;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before() {
        categoryController.idCounter = 0;
        categoryController.map.clear();
    }

    @Test
    public void postCategory() throws Exception {
        Category category = new Category("Plumbus");
        ObjectMapper mapper = new ObjectMapper();
        String serializedCategory = mapper.writeValueAsString(category);

        mvc.perform(MockMvcRequestBuilders.post("/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(serializedCategory)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(equalTo("1")));
    }

    @Test
    public void getCategories() throws Exception {
        Category expectedCategory = new Category("Plumbus");
        categoryController.postCategory(expectedCategory);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/categories")
                .accept(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Category> actualCategories = mapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<List<Category>>(){}
                );

        Assert.assertEquals(1, actualCategories.size());
        Assert.assertEquals(expectedCategory.getName(), actualCategories.get(0).getName());
    }
}
