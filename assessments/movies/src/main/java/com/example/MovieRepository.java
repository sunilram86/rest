package com.example;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, Long> {

    Movie findByTitleAndYear(String title, int year);

}
