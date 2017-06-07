package com.example.controller;

import com.example.model.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class CategoryController {

    int idCounter = 0;
    final Map<Integer,Category> map = new HashMap<>();

    @PostMapping(path="/categories")
    public int postCategory(@RequestBody Category category) {
        category.setId(++idCounter);
        map.put(category.getId(), category);
        return category.getId();
    }

    @GetMapping(path="/categories")
    public Collection<Category> getCategories() {
        return map.values();
    }

}
