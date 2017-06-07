package com.galvanize;

import org.springframework.data.repository.CrudRepository;

public interface MovieRepository extends CrudRepository<Review, Long> {


}