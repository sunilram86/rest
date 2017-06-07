package com.galvanize;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProfessionalRepository extends CrudRepository<Professional, Long> {

    List<Professional> findAll();

    Professional findByEmail(String email);
}
